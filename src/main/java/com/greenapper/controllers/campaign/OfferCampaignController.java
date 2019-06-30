package com.greenapper.controllers.campaign;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.OfferCampaign;
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
public class OfferCampaignController extends BaseCampaignController {

	@Resource
	private CampaignService offerCampaignService;

	public final static String ROOT_URI = "/campaigns/offer";

	public static final String CAMPAIGN_CREATION_URI = ROOT_URI + "/create";

	@Override
	CampaignService getCampaignService() {
		return offerCampaignService;
	}

	@Override
	CampaignForm createFormFromModel(Campaign campaign) {
		return new OfferCampaignForm((OfferCampaign) campaign);
	}

	@GetMapping(CAMPAIGN_CREATION_URI)
	public String getCampaignUpdateForm(@ModelAttribute("campaignForm") final OfferCampaignForm campaignForm) {
		return getPageForCampaignType(campaignForm.getType().displayName.toLowerCase());
	}

	@PostMapping(CAMPAIGN_CREATION_URI)
	public String updateCampaign(@ModelAttribute("campaignForm") final OfferCampaignForm campaignForm, final Errors errors) {
		return super.updateCampaign(campaignForm, errors);
	}
}
