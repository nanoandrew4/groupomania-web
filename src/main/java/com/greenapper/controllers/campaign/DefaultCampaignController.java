package com.greenapper.controllers.campaign;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.services.CampaignService;
import com.greenapper.services.impl.campaigns.DefaultCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/campaigns")
public class DefaultCampaignController extends BaseCampaignController {

	@Autowired
	private CampaignService campaignService;

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignService.class);

	@GetMapping("/{id}")
	public @ResponseBody
	CampaignDTO getCampaignById(@PathVariable final Long id) {
		return super.getCampaignById(id);
	}

	@GetMapping
	public String getAllVisibleCampaigns(final Model model) {
		return super.getAllVisibleCampaigns(model);
	}

	@Override
	CampaignService getCampaignService() {
		return campaignService;
	}
}
