package com.greenapper.controllers;

import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.services.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Contains all endpoints related to campaigns which have no direct relation to the session user whatsoever.
 * This controller should be fine for public use, although the form retrieval methods are secured since it doesn't make
 * sense for anonymous users to request forms.
 */
@Controller
public class CampaignController {

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ApplicationContext applicationContext;

	private Logger LOG = LoggerFactory.getLogger(CampaignController.class);

	private static final String ROOT_URI = "/campaigns";

	public final static String CAMPAIGN_VIEW_URI = ROOT_URI;

	public static final String CAMPAIGNS_OVERVIEW_URI = ROOT_URI;

	public static final String CAMPAIGN_CREATION_URI = ROOT_URI + "/create";

	public static final String CAMPAIGN_CREATION_DEFAULT_REDIRECTION = "redirect:" + CAMPAIGN_CREATION_URI + "?type=" + CampaignType.OFFER.displayName;

	public static final String CAMPAIGN_CREATION_SUCCESS_REDIRECT = "redirect:" + CampaignManagerController.CAMPAIGNS_OVERVIEW_URI;

	/**
	 * Retrieves a campaign by ID, and sets a flag to indicate to the frontend that the accompanying form should be
	 * immutable.
	 *
	 * @param model Model into which the campaign form and immutability flag will be written to
	 * @param id    Id of the campaign to retrieve
	 * @return Thymeleaf page to load with the returned data
	 */
	@GetMapping(CAMPAIGN_VIEW_URI + "/{id}")
	public String getCampaignById(final Model model, @PathVariable final Long id) {
		final CampaignForm campaignForm = createCampaignFormFromCampaign(campaignService.getCampaignById(id)).orElse(null);
		model.addAttribute("campaignForm", campaignForm);
		model.addAttribute("readonly", true);

		if (campaignForm != null)
			return getPageForCampaignType(campaignForm.getType().displayName);
		return getPageForCampaignType("offer");
	}

	/**
	 * Retrieves the campaign creation/update form, and attaches it to the model.
	 *
	 * @param model Model to which the form will be attached
	 * @param type  Type of campaign form to get, should match a {@link CampaignType#displayName}, or else {@link CampaignType#OFFER} is used as a default
	 * @return The appropriate page for the campaign type, or the page for offer campaigns if the type sent was invalid
	 */
	@GetMapping(CAMPAIGN_CREATION_URI)
	public String getCampaignUpdateForm(final Model model, @RequestParam(required = false) final String type) {
		try {
			model.addAttribute("campaignForm", Class.forName(CampaignForm.class.getPackage().getName() + "." + type + "CampaignForm").getConstructor().newInstance());
			return getPageForCampaignType(type);
		} catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			if (type != null)
				LOG.error("Could not create Campaign model from type: \'" + type + "\', will use default redirect...");
		}

		return CAMPAIGN_CREATION_DEFAULT_REDIRECTION;
	}

	/**
	 * Begins the campaign creation/update process. First the appropriate service is fetched, based on the campaign type,
	 * and then it is passed down to it, either through {@link CampaignService#createCampaign(CampaignForm, Errors)} if
	 * the form has no ID attached, or through {@link CampaignService#editCampaign(CampaignForm, Errors)} if there is an
	 * ID associated with the sent form (which should only happen when the campaign is edited, since otherwise the ID
	 * in the form will be null.)
	 *
	 * @param campaignForm Campaign form containing the necessary data for the creation/update process
	 * @param errors       Errors associated with the form, if any exist, they will be attached to this instance for rendering
	 *                     in the frontend
	 * @return A redirect in case the campaign was successfully created/updated to the campaign overview page, or
	 * the corresponding page for the type of campaign that was sent, alongside the errors that were encountered during validation
	 */
	@PostMapping(CAMPAIGN_CREATION_URI)
	public String updateCampaign(final CampaignForm campaignForm, final Errors errors) {
		try {
			final CampaignService campaignService = (CampaignService) applicationContext.getBean(campaignForm.getType().displayName.toLowerCase() + "CampaignService");
			if (campaignForm.getId() == null)
				campaignService.createCampaign(campaignForm, errors);
			else
				campaignService.editCampaign(campaignForm, errors);

			if (!errors.hasErrors())
				return CAMPAIGN_CREATION_SUCCESS_REDIRECT;
		} catch (NoSuchBeanDefinitionException | NullPointerException e) {
			LOG.error("An error occurred while trying to get the service for the supplied campaign", e);
			errors.reject("err.campaign.type");
		}
		return getPageForCampaignType(campaignForm.getType().displayName);
	}

	/**
	 * Returns all visible campaigns, that is, active campaigns or those that have been archived, taking into account the
	 * option of keeping it listed up to four days after the end/archival date of the campaign.
	 *
	 * @param model Model to which the campaigns will be written to
	 * @return Home page
	 */
	@GetMapping(CAMPAIGNS_OVERVIEW_URI)
	public String getAllVisibleCampaigns(final Model model) {
		final List<Campaign> campaigns = campaignService.getAllCampaigns();

		campaigns.removeIf(campaign -> campaign.getState() == CampaignState.INACTIVE);
		campaigns.removeIf(campaign -> campaign.isShowAfterExpiration() && LocalDate.now().plus(4, ChronoUnit.DAYS).isAfter(campaign.getEndDate()));
		campaigns.removeIf(campaign -> !campaign.isShowAfterExpiration() && LocalDate.now().isAfter(campaign.getEndDate()));

		model.addAttribute("campaigns", campaigns);
		return "home";
	}

	/**
	 * Creates and populates a {@link CampaignForm} from a {@link Campaign} through the use of reflection and the passed Campaigns type.
	 *
	 * @param campaign {@link Campaign} from which the {@link CampaignForm} will be created
	 * @return Optional containing the created {@link CampaignForm}, or {@link Optional#empty()}
	 */
	public Optional<CampaignForm> createCampaignFormFromCampaign(final Campaign campaign) {
		try {
			final String fullClassName = CampaignForm.class.getPackage().getName() + "." + campaign.getType().displayName + "CampaignForm";
			final String modelClassName = Campaign.class.getPackage().getName() + "." + campaign.getType().displayName + "Campaign";
			return Optional.of((CampaignForm) Class.forName(fullClassName).getConstructor(Class.forName(modelClassName)).newInstance(campaign));
		} catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			LOG.error("Could not create campaign model for type: \'" + campaign.getType().displayName + "\'", e);
			return Optional.empty();
		}
	}

	/**
	 * Creates the path to the desired campaign page based on its type.
	 *
	 * @param type Type of the campaign whose page is to be fetched
	 * @return Path to the campaigns form, to be returned from the controller
	 */
	public static String getPageForCampaignType(final String type) {
		return "campaigns/" + type.toLowerCase() + "Campaign";
	}
}
