package com.greenapper.dtos.campaign;

import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.models.campaigns.OfferCampaign;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "DTO that represents offer campaigns", parent = CampaignDTO.class)
public class OfferCampaignDTO extends CampaignDTO {
	public OfferCampaignDTO() {
		this.setType(CampaignType.OFFER);
	}

	/**
	 * Creates a {@link OfferCampaignForm} from an existing {@link OfferCampaign}.
	 *
	 * @param offerCampaign Campaign model from which to copy the data
	 */
	public OfferCampaignDTO(final OfferCampaign offerCampaign) {
		super(offerCampaign);
	}
}
