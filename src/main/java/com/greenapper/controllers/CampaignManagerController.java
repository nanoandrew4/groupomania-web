package com.greenapper.controllers;

import com.greenapper.dtos.OAuthExceptionDTO;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.services.CampaignManagerService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Controller that handles all operations related to the {@link CampaignManager} type. Also handles methods relating
 * to a specified managers campaigns.
 */
@RestController
@Secured("ROLE_CAMPAIGN_MANAGER")
@Api(value = "/campaign-manager", description = "Contains all endpoints related to the campaign manager user type")
public class CampaignManagerController {

	private final static String ROOT_URI = "/campaign-manager";

	public final static String PASSWORD_UPDATE_URI = ROOT_URI + "/password";

	public final static String CAMPAIGNS_OVERVIEW_URI = ROOT_URI + "/campaigns";

	private Logger LOG = LoggerFactory.getLogger(CampaignManagerController.class);

	@Autowired
	private CampaignManagerService campaignManagerService;

	@PutMapping(PASSWORD_UPDATE_URI)
	@ApiOperation(value = "Endpoint which serves to update the password of campaign manager")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Password updated successfully"),
			@ApiResponse(code = 400, message = "A validation error was encountered during the update, more details in the response body", response = ValidationErrorDTO.class),
			@ApiResponse(code = 401, message = "Authentication error", response = OAuthExceptionDTO.class)
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updatePassword(
			@RequestBody @ApiParam(value = "Password update form", required = true) final PasswordUpdateForm passwordUpdateForm,
			@ApiIgnore final Errors errors) {
		LOG.info("Updating password for session campaign manager");
		campaignManagerService.updatePassword(passwordUpdateForm, errors);
	}

	@GetMapping(CAMPAIGNS_OVERVIEW_URI)
	@ApiOperation(value = "Retrieve all campaigns belonging to the campaign manager currently in session",
				  notes = "If no campaigns exist, an empty list is returned")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returned appropriate campaigns"),
			@ApiResponse(code = 401, message = "Authentication error", response = OAuthExceptionDTO.class)
	})
	public List<CampaignDTO> getCampaignManagerCampaigns() {
		LOG.info("Retrieving all campaigns for the session campaign manager");
		return campaignManagerService.getCampaigns();
	}
}
