package com.greenapper.controllers;

import com.greenapper.controllers.campaign.BaseCampaignController;
import com.greenapper.controllers.campaign.OfferCampaignController;
import com.greenapper.enums.CampaignState;
import com.greenapper.factories.CampaignFormFactory;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;

/**
 * Controller that handles all operations related to the {@link CampaignManager} type. Also handles methods relating
 * to a specified managers campaigns.
 */
@Controller
public class CampaignManagerController {

	private final static String ROOT_URI = "/campaign-manager";

	public final static String PASSWORD_UPDATE_URI = ROOT_URI + "/password/update";

	public final static String PASSWORD_UPDATE_FORM = "campaign_manager/passwordUpdate";

	public final static String CAMPAIGNS_OVERVIEW_URI = ROOT_URI + "/campaigns";

	public final static String CAMPAIGN_UPDATE_URI = ROOT_URI + "/campaigns/update/{id}";

	public final static String CAMPAIGN_STATE_UPDATE_URI = ROOT_URI + "/campaigns/update/state/{id}/{state}";

	public final static String CAMPAIGN_STATE_UPDATE_SUCCESS_REDIRECT = "redirect:" + CAMPAIGNS_OVERVIEW_URI;

	public final static String CAMPAIGNS_OVERVIEW_FORM = "campaign_manager/campaignsOverview";

	public final static String PASSWORD_UPDATE_SUCCESS_REDIRECT = "redirect:" + CAMPAIGNS_OVERVIEW_URI;

	private Logger LOG = LoggerFactory.getLogger(CampaignManagerController.class);

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private CampaignFormFactory campaignFormFactory;

	/**
	 * Retrieves the password update page.
	 *
	 * @param passwordUpdateForm The password update model that will be filled in on the frontend
	 * @return The password update page
	 */
	@GetMapping(PASSWORD_UPDATE_URI)
	public String resetPassword(final PasswordUpdateForm passwordUpdateForm) {
		return PASSWORD_UPDATE_FORM;
	}

	/**
	 * Initiates the password update process, and either redirects to the users previous page if the update was
	 * successful, or returns the encountered validation errors alongside the password update page.
	 *
	 * @param passwordUpdateForm New password information to be passed on to the service layer for validation and update
	 * @param errors             Errors associated with the accompanying form, which will be populated if any validation errors are encountered in the service layer
	 * @return Redirect to the users previous page if the update was successful, or returns the encountered validation errors alongside the password update page
	 */
	@PatchMapping(PASSWORD_UPDATE_URI)
	public String updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		campaignManagerService.updatePassword(passwordUpdateForm, errors);

		if (!errors.hasErrors())
			return PASSWORD_UPDATE_SUCCESS_REDIRECT;
		else {
			return PASSWORD_UPDATE_FORM;
		}
	}

	/**
	 * Retrieves the campaign manager overview page, from which management and creation of campaigns takes place.
	 *
	 * @param model Model to which the campaigns associated with the {@link CampaignManager} in session will be attached to
	 * @return The campaign overview page
	 */
	@GetMapping(CAMPAIGNS_OVERVIEW_URI)
	public String getCampaignOverview(final Model model) {
		final List<Campaign> campaigns = campaignManagerService.getCampaigns();
		campaigns.sort(Comparator.comparing(Campaign::getStartDate));

		model.addAttribute("campaigns", campaigns);
		return CAMPAIGNS_OVERVIEW_FORM;
	}

	/**
	 * Retrieves a {@link Campaign} and converts it to a {@link CampaignForm} for editing by a {@link CampaignManager}.
	 *
	 * @param model Model to which the retrieved {@link CampaignForm} will be attached
	 * @param id    Id of the campaign to retrieve for editing
	 * @return Campaign update page for the appropriate campaign type if the request campaign exists, otherwise will redirect to the default campaign creation page
	 */
	@GetMapping(CAMPAIGN_UPDATE_URI)
	public String getCampaignForEditById(final Model model, @PathVariable final Long id) {
		final Campaign campaign = campaignService.getCampaignByIdAndSessionUser(id).orElse(null);

		if (campaign != null && campaign.getState() != CampaignState.ARCHIVED) {
			model.addAttribute("campaignForm", campaignFormFactory.createCampaignForm(campaign).orElse(null));
			return BaseCampaignController.getPageForCampaignType(campaign.getType().displayName);
		}

		return OfferCampaignController.CAMPAIGN_CREATION_URI;
	}

	/**
	 * Updates the state of the campaign associated to the supplied ID, if it exists.
	 *
	 * @param id    ID of the campaign whose state to update
	 * @param state New state for the campaign
	 * @return Redirect to the campaign manager overview page
	 */
	@PatchMapping(CAMPAIGN_STATE_UPDATE_URI)
	public String updateCampaignState(@PathVariable final Long id, @PathVariable final String state) {
		campaignService.updateCampaignState(id, state);
		return CAMPAIGN_STATE_UPDATE_SUCCESS_REDIRECT;
	}
}
