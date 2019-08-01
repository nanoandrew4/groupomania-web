package com.greenapper.dtos.campaigns;

import com.greenapper.enums.CampaignType;
import com.greenapper.models.campaigns.OfferCampaign;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "DTO that represents offer campaigns", parent = CampaignDTO.class)
public class OfferCampaignDTO extends CampaignDTO {
	public OfferCampaignDTO() {
		this.setType(CampaignType.OFFER);
	}

	/**
	 * Creates a {@link com.greenapper.dtos.campaigns.OfferCampaignDTO} from an existing {@link OfferCampaign}.
	 *
	 * @param offerCampaign Campaign model from which to copy the data
	 */
	public OfferCampaignDTO(final OfferCampaign offerCampaign) {
		super(offerCampaign);
	}
}
