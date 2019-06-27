package com.greenapper.services.impl.campaigns;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Service
@Primary
public class OfferCampaignService extends DefaultCampaignService {

	@Resource
	private Validator offerCampaignFormValidator;

	@Override
	public void validateCampaign(final CampaignForm campaign, final Errors errors) {
		offerCampaignFormValidator.validate(campaign, errors);
	}

	@Override
	public void setDefaultsForCampaignSubtype(final Campaign campaignSubtype) {
	}
}
