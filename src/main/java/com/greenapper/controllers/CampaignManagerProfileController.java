package com.greenapper.controllers;

import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.services.CampaignManagerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling all operations regarding a {@link CampaignManager}s profile.
 */
@RestController
@RequestMapping("/campaign-manager/profile")
@Secured("ROLE_CAMPAIGN_MANAGER")
public class CampaignManagerProfileController {

	@Autowired
	private CampaignManagerProfileService campaignManagerProfileService;

	public final static String PROFILE_UPDATE_URI = "/campaign-manager/profile/update";

	@GetMapping
	public CampaignManagerProfileDTO getCampaignManagerProfileSetup() {
		return campaignManagerProfileService.getProfileForCurrentUser();
	}

	/**
	 * Initiates the profile update process, by passing the supplied model down to the service layer. Will return a redirect
	 * to the previously visited page if the update was completed successfully, or the update page alongside the validation
	 * errors encountered otherwise.
	 *
	 * @param campaignManagerProfile Updated profile model to associate with the {@link CampaignManager} in session
	 * @param errors                 Errors associated to the supplied profile, which will be populated if any validation errors are encountered in the service layer
	 */
	@PutMapping("/update")
	public void updateProfile(final CampaignManagerProfile campaignManagerProfile, final Errors errors) {
		campaignManagerProfileService.updateProfile(campaignManagerProfile, errors);
	}
}
