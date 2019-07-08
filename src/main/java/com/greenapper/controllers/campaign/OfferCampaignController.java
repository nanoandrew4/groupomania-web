package com.greenapper.controllers.campaign;

import com.greenapper.dtos.ErrorDTO;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.services.CampaignService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@RequestMapping("/campaigns/offer")
@Api(value = "/campaigns/offer", description = "Endpoints to do with offer campaigns")
public class OfferCampaignController {

	@Resource
	private CampaignService offerCampaignService;

	@Secured("ROLE_CAMPAIGN_MANAGER")
	@PostMapping("/create")
	@ApiOperation(value = "Create an offer campaign for the campaign manager currently in session")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Offer campaign created correctly"),
			@ApiResponse(code = 400, message = "Validation errors occurred while creating the campaign", response = ValidationErrorDTO.class)
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void createCampaign(
			@RequestBody @ApiParam(value = "Offer campaign form with required data to create the campaign", required = true) final OfferCampaignForm campaignForm,
			@ApiIgnore final Errors errors) {
		offerCampaignService.createCampaign(campaignForm, errors);
	}

	@Secured("ROLE_CAMPAIGN_MANAGER")
	@PutMapping("/update")
	@ApiOperation(value = "Update an existing campaign with the newly supplied data", notes = "The id must always be present in the request")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Campaign updated successfully"),
			@ApiResponse(code = 400, message = "Validation errors occurred while updating the campaign", response = ValidationErrorDTO.class),
			@ApiResponse(code = 404, message = "Campaign to update could not be found", response = ErrorDTO.class)
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCampaign(
			@RequestBody @ApiParam(value = "Offer campaign form with the fields that you wish to update", required = true) final OfferCampaignForm campaignForm,
			@ApiIgnore final Errors errors) {
		offerCampaignService.updateCampaign(campaignForm, errors);
	}
}
