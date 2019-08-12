package com.greenapper.factories.campaign.impl;

import com.greenapper.factories.campaign.CampaignFactory;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.logging.LogManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.CouponCampaign;
import com.greenapper.models.campaigns.OfferCampaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultCampaignFactory implements CampaignFactory {

	@Autowired
	private LogManager LOG;

	@Override
	public Optional<Campaign> createCampaignModel(final CampaignForm campaignForm) {
		if (campaignForm == null)
			return Optional.empty();

		switch (campaignForm.getType()) {
			case OFFER:
				return Optional.of(new OfferCampaign((OfferCampaignForm) campaignForm));
			case COUPON:
				return Optional.of(new CouponCampaign((CouponCampaignForm) campaignForm));
			default:
				LOG.error("Cant create campaign for campaign form with id: " + campaignForm.getId() + " and type: " + campaignForm.getType());
				return Optional.empty();
		}
	}
}
