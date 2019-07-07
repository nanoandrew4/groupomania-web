package com.greenapper.controllers.campaign;

import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.services.CampaignService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/campaigns/offer")
public class OfferCampaignController {

	@Resource
	private CampaignService offerCampaignService;

	@PostMapping("/create")
	public void createCampaign(@RequestBody final OfferCampaignForm campaignForm, final Errors errors) {
		offerCampaignService.createCampaign(campaignForm, errors);
	}

	@PutMapping("/update")
	public void updateCampaign(@RequestBody final OfferCampaignForm campaignForm, final Errors errors) {
		offerCampaignService.updateCampaign(campaignForm, errors);
	}
}
