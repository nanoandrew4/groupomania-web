package com.greenapper.controllers.campaign;

import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.services.CampaignService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping
public class CouponCampaignController extends BaseCampaignController {

	@Resource
	private CampaignService couponCampaignService;

	public final static String ROOT_URI = "/campaigns/coupon";

	public static final String CAMPAIGN_CREATION_URI = ROOT_URI + "/create";

	@Override
	CampaignService getCampaignService() {
		return couponCampaignService;
	}

	@GetMapping(CAMPAIGN_CREATION_URI)
	public String getCampaignUpdateForm(@ModelAttribute("campaignForm") final CouponCampaignForm campaignForm) {
		return getPageForCampaignType(campaignForm.getType().displayName.toLowerCase());
	}

	@PostMapping(CAMPAIGN_CREATION_URI)
	public String updateCampaign(@ModelAttribute("campaignForm") final CouponCampaignForm campaignForm, final Errors errors) {
		return super.updateCampaign(campaignForm, errors);
	}
}
