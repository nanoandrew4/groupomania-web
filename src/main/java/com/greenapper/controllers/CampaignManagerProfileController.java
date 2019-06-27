package com.greenapper.controllers;

import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.services.CampaignManagerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

/**
 * Controller for handling all operations regarding a {@link CampaignManager}s profile.
 */
@Controller
public class CampaignManagerProfileController {

	@Autowired
	private CampaignManagerProfileService campaignManagerProfileService;

	private final static String ROOT_URI = "/campaign-manager/profile";

	public final static String PROFILE_UPDATE_URI = ROOT_URI + "/setup";

	public final static String PROFILE_UPDATE_FORM = "campaign_manager/profileSetup";

	public final static String PROFILE_UPDATE_SUCCESS = "redirect:" + CampaignController.CAMPAIGNS_OVERVIEW_URI;

	/**
	 * Retrieves the {@link CampaignManagerProfile} associated to the {@link CampaignManager} that is in session,
	 * if the manager has a profile associated, otherwise a new profile model is returned.
	 *
	 * @param model Model to which the existing or empty campaign profile will be written
	 * @return Profile update page
	 */
	@GetMapping(PROFILE_UPDATE_URI)
	public String getCampaignManagerProfileSetup(final Model model) {
		final Optional<CampaignManagerProfile> profile = campaignManagerProfileService.getProfileForCurrentUser();
		model.addAttribute(profile.orElseGet(CampaignManagerProfile::new));
		return PROFILE_UPDATE_FORM;
	}

	/**
	 * Initiates the profile update process, by passing the supplied model down to the service layer. Will return a redirect
	 * to the previously visited page if the update was completed successfully, or the update page alongside the validation
	 * errors encountered otherwise.
	 *
	 * @param campaignManagerProfile Updated profile model to associate with the {@link CampaignManager} in session
	 * @param errors                 Errors associated to the supplied profile, which will be populated if any validation errors are encountered in the service layer
	 * @return Redirect to the previously visited page if the update completed successfully, or the update page alongside the appropriate validation errors otherwise
	 */
	@PutMapping(PROFILE_UPDATE_URI)
	public String updateProfile(final CampaignManagerProfile campaignManagerProfile, final Errors errors) {
		campaignManagerProfileService.updateProfile(campaignManagerProfile, errors);

		if (!errors.hasErrors()) {
			return PROFILE_UPDATE_SUCCESS;
		} else {
			return PROFILE_UPDATE_FORM;
		}
	}
}
