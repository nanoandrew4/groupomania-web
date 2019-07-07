package com.greenapper.controllers.campaign;

import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.services.CampaignService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/campaigns/coupon")
public class CouponCampaignController {

	@Resource
	private CampaignService couponCampaignService;

	@PostMapping("/create")
	public void createCampaign(@RequestBody final CouponCampaignForm campaignForm, final Errors errors) {
		couponCampaignService.createCampaign(campaignForm, errors);
	}

	@PutMapping("/update")
	public void updateCampaign(@RequestBody final CouponCampaignForm campaignForm, final Errors errors) {
		couponCampaignService.updateCampaign(campaignForm, errors);
	}
}
