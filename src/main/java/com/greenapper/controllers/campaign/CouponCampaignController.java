package com.greenapper.controllers.campaign;

import com.greenapper.dtos.ErrorDTO;
import com.greenapper.dtos.ValidationErrorDTO;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.services.CampaignService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@RestController
@RequestMapping("/campaigns/coupon")
@Api(value = "/campaigns/coupon", description = "Endpoints to do with coupon campaigns")
public class CouponCampaignController {

	@Resource
	private CampaignService couponCampaignService;

	@Secured("ROLE_CAMPAIGN_MANAGER")
	@PostMapping("/create")
	@ApiOperation(value = "Create an coupon campaign for the campaign manager currently in session")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Coupon campaign created correctly"),
			@ApiResponse(code = 400, message = "Validation errors occurred while creating the campaign", response = ValidationErrorDTO.class)
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void createCampaign(
			@RequestBody @ApiParam(value = "Coupon campaign form with required data to create the campaign", required = true) final CouponCampaignForm campaignForm,
			@ApiIgnore final Errors errors) {
		couponCampaignService.createCampaign(campaignForm, errors);
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
			@RequestBody @ApiParam(value = "Coupon campaign form with the fields that you wish to update", required = true) final CouponCampaignForm campaignForm,
			@ApiIgnore final Errors errors) {
		couponCampaignService.updateCampaign(campaignForm, errors);
	}
}
