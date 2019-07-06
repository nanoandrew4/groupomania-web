package com.greenapper.controllers;

import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.services.CampaignManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that handles all operations related to the {@link CampaignManager} type. Also handles methods relating
 * to a specified managers campaigns.
 */
@Controller
@Secured("ROLE_CAMPAIGN_MANAGER")
public class CampaignManagerController {

	private final static String ROOT_URI = "/campaign-manager";

	public final static String PASSWORD_UPDATE_URI = ROOT_URI + "/password";

	public final static String PASSWORD_UPDATE_FORM = "campaign_manager/password";

	public final static String CAMPAIGNS_OVERVIEW_URI = ROOT_URI + "/campaigns";

	public final static String CAMPAIGN_UPDATE_URI = ROOT_URI + "/campaigns/update/{id}";

	public final static String CAMPAIGN_STATE_UPDATE_URI = ROOT_URI + "/campaigns/update/state/{id}/{state}";

	public final static String CAMPAIGN_STATE_UPDATE_SUCCESS_REDIRECT = "redirect:" + CAMPAIGNS_OVERVIEW_URI;

	public final static String CAMPAIGNS_OVERVIEW_FORM = "campaign_manager/campaignsOverview";

	public final static String PASSWORD_UPDATE_SUCCESS_REDIRECT = "redirect:" + CAMPAIGNS_OVERVIEW_URI;

	private Logger LOG = LoggerFactory.getLogger(CampaignManagerController.class);

	@Autowired
	private CampaignManagerService campaignManagerService;

	/**
	 * Initiates the password update process, and either redirects to the users previous page if the update was
	 * successful, or returns the encountered validation errors alongside the password update page.
	 *
	 * @param passwordUpdateForm New password information to be passed on to the service layer for validation and update
	 * @param errors             Errors associated with the accompanying form, which will be populated if any validation errors are encountered in the service layer
	 */
	@PatchMapping(PASSWORD_UPDATE_URI)
	public @ResponseBody
	void updatePassword(@RequestBody final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		LOG.info("Updating password for session campaign manager");
		campaignManagerService.updatePassword(passwordUpdateForm, errors);
	}

	/**
	 * Retrieves the campaign manager overview page, from which management and creation of campaigns takes place.
	 *
	 * @return The campaign overview page
	 */
	@GetMapping(CAMPAIGNS_OVERVIEW_URI)
	public @ResponseBody
	List<CampaignDTO> getCampaignManagerCampaigns() {
		LOG.info("Retrieving all campaigns for the session campaign manager");
		return campaignManagerService.getCampaigns();
	}

	/**
	 * Updates the state of the campaign associated to the supplied ID, if it exists.
	 *
	 * @param id    ID of the campaign whose state to update
	 * @param state New state for the campaign
	 */
	@PatchMapping(CAMPAIGN_STATE_UPDATE_URI)
	public @ResponseBody
	void updateCampaignState(@PathVariable final Long id, @PathVariable final String state) {
		LOG.info("Updating campaign state for campaign with id: \'" + id + "\'");
		campaignManagerService.updateCampaignState(id, state);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ValidationErrorDTO handleValidationError(final Exception e) {
		return new ValidationErrorDTO((ValidationException) e);
	}
}
