package com.greenapper.factories.campaign.impl;

import com.greenapper.factories.campaign.CampaignFormFactory;
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
public class DefaultCampaignFormFactory implements CampaignFormFactory {

	@Autowired
	private LogManager LOG;

	@Override
	public Optional<CampaignForm> createCampaignForm(final Campaign campaign) {
		if (campaign == null)
			return Optional.empty();

		switch (campaign.getType()) {
			case OFFER:
				return Optional.of(new OfferCampaignForm((OfferCampaign) campaign));
			case COUPON:
				return Optional.of(new CouponCampaignForm((CouponCampaign) campaign));
			default:
				LOG.warn("Cant create campaign form for campaign with id: " + campaign.getId() + " and type: " + campaign.getType());
				return Optional.empty();
		}
	}
}
