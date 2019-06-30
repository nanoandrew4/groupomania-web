package com.greenapper.factories.impl;

import com.greenapper.factories.CampaignFactory;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.CouponCampaign;
import com.greenapper.models.campaigns.OfferCampaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultCampaignFactory implements CampaignFactory {

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignFactory.class);

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
				LOG.warn("Cant create campaign for campaign form with id: " + campaignForm.getId() + " and type: " + campaignForm.getType());
				return Optional.empty();
		}
	}
}
