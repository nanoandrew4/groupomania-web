package com.greenapper.controllers.campaign;

import com.greenapper.controllers.CampaignManagerController;
import com.greenapper.enums.CampaignState;
import com.greenapper.exceptions.InvalidCampaignTypeException;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.factories.CampaignFormFactory;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.services.CampaignService;
import com.greenapper.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Contains all endpoints related to campaigns which have no direct relation to the session user whatsoever.
 * This controller should be fine for public use, although the form retrieval methods are secured since it doesn't make
 * sense for anonymous users to request forms.
 */
public abstract class BaseCampaignController {

	@Autowired
	private CampaignFormFactory campaignFormFactory;

	@Autowired
	private SessionService sessionService;

	private Logger LOG = LoggerFactory.getLogger(BaseCampaignController.class);

	public static final String CAMPAIGN_CREATION_SUCCESS_REDIRECT = "redirect:" + CampaignManagerController.CAMPAIGNS_OVERVIEW_URI;

	abstract CampaignService getCampaignService();

	/**
	 * Retrieves a campaign by ID, and sets a flag to indicate to the frontend that the accompanying form should be
	 * immutable.
	 *
	 * @param model Model into which the campaign form and immutability flag will be written to
	 * @param id    Id of the campaign to retrieve
	 * @return Thymeleaf page to load with the returned data
	 */
	public String getCampaignById(final Model model, @PathVariable final Long id) {
		final Optional<Campaign> campaign = getCampaignService().getCampaignById(id);
		final Optional<CampaignForm> campaignForm = campaign.flatMap(campaignFormFactory::createCampaignForm);

		if (campaign.isPresent()) {
			final boolean doesCampaignBelongToSessionUser = sessionService.getSessionUser() != null
															&& campaign.get().getOwner().getId().equals(sessionService.getSessionUser().getId());

			if (doesCampaignBelongToSessionUser || !isCampaignUnlisted(campaign.get()))
				model.addAttribute("campaignForm", campaignForm.orElse(null));
		}
		model.addAttribute("readonly", true);

		final CampaignForm unboxedCampaignForm = campaignForm.orElseThrow(() -> new UnknownIdentifierException("No campaign found with id: " + id));
		return getPageForCampaignType(unboxedCampaignForm.getType().displayName.toLowerCase());
	}

	/**
	 * Begins the campaign creation/update process. First the appropriate service is fetched, based on the campaign type,
	 * and then it is passed down to it, either through {@link CampaignService#createCampaign(CampaignForm, Errors)} if
	 * the form has no ID attached, or through {@link CampaignService#updateCampaign(CampaignForm, Errors)} if there is an
	 * ID associated with the sent form (which should only happen when the campaign is edited, since otherwise the ID
	 * in the form will be null.)
	 *
	 * @param campaignForm Campaign form containing the necessary data for the creation/update process
	 * @param errors       Errors associated with the form, if any exist, they will be attached to this instance for rendering
	 *                     in the frontend
	 * @return A redirect in case the campaign was successfully created/updated to the campaign overview page, or
	 * the corresponding page for the type of campaign that was sent, alongside the errors that were encountered during validation
	 */
	public String updateCampaign(final CampaignForm campaignForm, final Errors errors) {
		if (campaignForm == null)
			throw new UnknownIdentifierException("Campaign form was null, cannot initiate update operation");

		if (campaignForm.getId() == null)
			getCampaignService().createCampaign(campaignForm, errors);
		else
			getCampaignService().updateCampaign(campaignForm, errors);

		if (!errors.hasErrors())
			return CAMPAIGN_CREATION_SUCCESS_REDIRECT;

		if (campaignForm.getType() != null)
			return getPageForCampaignType(campaignForm.getType().displayName);
		throw new InvalidCampaignTypeException(campaignForm.getId(), campaignForm.getType());
	}

	/**
	 * Returns all visible campaigns, that is, active campaigns or those that have been archived, taking into account the
	 * option of keeping it listed up to four days after the end/archival date of the campaign.
	 *
	 * @param model Model to which the campaigns will be written to
	 * @return Home page
	 */
	public String getAllVisibleCampaigns(final Model model) {
		final List<Campaign> campaigns = getCampaignService().getAllCampaigns();

		campaigns.removeIf(this::isCampaignUnlisted);
		campaigns.sort(Comparator.comparing(Campaign::getStartDate));

		model.addAttribute("campaigns", campaigns);
		return "home";
	}

	/**
	 * Determines if a campaign should be publicly visible.
	 *
	 * @param campaign Campaign for which to determine if it should be publicly visible or not
	 * @return True if the the campaign should be unlisted, false if the campaign should be publicly visible
	 */
	private boolean isCampaignUnlisted(final Campaign campaign) {
		return campaign.getState() == CampaignState.INACTIVE
			   || (campaign.isShowAfterExpiration() && LocalDate.now().isAfter(campaign.getEndDate().plus(4, ChronoUnit.DAYS)))
			   || (!campaign.isShowAfterExpiration() && LocalDate.now().isAfter(campaign.getEndDate()));

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
