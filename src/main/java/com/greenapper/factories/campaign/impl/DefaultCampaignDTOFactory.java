package com.greenapper.factories.campaign.impl;

import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.dtos.campaigns.CouponCampaignDTO;
import com.greenapper.dtos.campaigns.OfferCampaignDTO;
import com.greenapper.factories.campaign.CampaignDTOFactory;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.CouponCampaign;
import com.greenapper.models.campaigns.OfferCampaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultCampaignDTOFactory implements CampaignDTOFactory {

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignDTOFactory.class);

	@Override
	public CampaignDTO createCampaignDTO(final Campaign campaign) {
		if (campaign == null)
			return null;

		switch (campaign.getType()) {
			case OFFER:
				return new OfferCampaignDTO((OfferCampaign) campaign);
			case COUPON:
				return new CouponCampaignDTO((CouponCampaign) campaign);
			default:
				LOG.error("Cant create campaign DTO for campaign with id: " + campaign.getId() + " and type: " + campaign.getType());
				return null;
		}
	}
}