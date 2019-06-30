package com.greenapper.controllers.campaign;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;
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

import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/campaigns")
public class DefaultCampaignController extends BaseCampaignController {

	@Autowired
	private CampaignService campaignService;

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignService.class);

	@GetMapping("/{id}")
	public String getCampaignById(final Model model, @PathVariable final Long id) {
		return super.getCampaignById(model, id);
	}

	@GetMapping
	public String getAllVisibleCampaigns(final Model model) {
		return super.getAllVisibleCampaigns(model);
	}

	@Override
	CampaignService getCampaignService() {
		return campaignService;
	}

	@Override
	CampaignForm createFormFromModel(Campaign campaign) {
		try {
			final String campaignFormClassName = CampaignForm.class.getPackage().getName() + "." + campaign.getType().displayName + "CampaignForm";
			return (CampaignForm) Class.forName(campaignFormClassName).getConstructor(campaign.getClass()).newInstance(campaign);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			LOG.error("An exception occurred when creating the campaign form", e);
			return null;
		}
	}
}
