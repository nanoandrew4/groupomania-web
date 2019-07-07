package com.greenapper.controllers;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.services.CampaignManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that handles all operations related to the {@link CampaignManager} type. Also handles methods relating
 * to a specified managers campaigns.
 */
@RestController
@Secured("ROLE_CAMPAIGN_MANAGER")
public class CampaignManagerController {

	private final static String ROOT_URI = "/campaign-manager";

	public final static String PASSWORD_UPDATE_URI = ROOT_URI + "/password";

	public final static String CAMPAIGNS_OVERVIEW_URI = ROOT_URI + "/campaigns";

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
	public void updatePassword(@RequestBody final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		LOG.info("Updating password for session campaign manager");
		campaignManagerService.updatePassword(passwordUpdateForm, errors);
	}

	/**
	 * Retrieves the campaign manager overview page, from which management and creation of campaigns takes place.
	 *
	 * @return The campaign overview page
	 */
	@GetMapping(CAMPAIGNS_OVERVIEW_URI)
	public List<CampaignDTO> getCampaignManagerCampaigns() {
		LOG.info("Retrieving all campaigns for the session campaign manager");
		return campaignManagerService.getCampaigns();
	}
}
