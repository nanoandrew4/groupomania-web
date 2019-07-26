package com.greenapper.controllers;

import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.dtos.ErrorDTO;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.forms.CampaignManagerProfileForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.services.CampaignManagerProfileService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Controller for handling all operations regarding a {@link CampaignManager}s profile.
 */
@RestController
@RequestMapping("/campaign-manager/profile")
@Secured("ROLE_CAMPAIGN_MANAGER")
@Api(value = "/campaign-manager/profile", description = "Contains all endpoints regarding profiles for campaign managers")
public class CampaignManagerProfileController {

	@Autowired
	private CampaignManagerProfileService campaignManagerProfileService;

	public final static String PROFILE_UPDATE_URI = "/campaign-manager/profile";

	@GetMapping
	@ApiOperation(value = "Retrieves the profile of the campaign manager currently in session")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Campaign manager profile returned successfully"),
			@ApiResponse(code = 404, message = "Profile for campaign manager in session could not be found", response = ErrorDTO.class)
	})
	public CampaignManagerProfileDTO getCampaignManagerProfileSetup() {
		return campaignManagerProfileService.getProfileForCurrentUser();
	}

	@PutMapping
	@ApiOperation(value = "Update the user profile with the new data supplied in the body")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Profile updated successfully"),
			@ApiResponse(code = 400, message = "Validation errors were encountered during the profile update process", response = ValidationErrorDTO.class)
	})
	public void updateProfile(
			@RequestBody @ApiParam(value = "Profile form containing the data to override existing values", required = true) final CampaignManagerProfileForm profileForm,
			@ApiIgnore final Errors errors) {
		campaignManagerProfileService.updateProfile(profileForm, errors);
	}
}
