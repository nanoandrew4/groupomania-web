package com.greenapper.forms.campaigns;

import com.greenapper.enums.CampaignType;
import com.greenapper.models.campaigns.OfferCampaign;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "Form that represents a coupon campaign", parent = CampaignForm.class)
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
