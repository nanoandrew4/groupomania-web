package com.greenapper.forms.campaigns;

import com.greenapper.enums.CampaignType;
import com.greenapper.models.campaigns.OfferCampaign;

/**
 * Form for {@link OfferCampaign} campaigns. Contains all necessary fields to create a {@link OfferCampaign}, with numeric
 * fields and dates being stored as strings so they can be validated prior to being converted to campaign models.
 */
public class OfferCampaignForm extends CampaignForm {
	public OfferCampaignForm() {
		this.setType(CampaignType.OFFER);
	}

	/**
	 * Creates a {@link OfferCampaignForm} from an existing {@link OfferCampaign}.
	 *
	 * @param offerCampaign Campaign model from which to copy the data
	 */
	public OfferCampaignForm(final OfferCampaign offerCampaign) {
		super(offerCampaign);
	}
}
