package com.greenapper.models.campaigns;

import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.OfferCampaignForm;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model for offer type campaigns, which extends from the generic {@link Campaign} model.
 */
@Entity
@Table(name = "OfferCampaign")
public class OfferCampaign extends Campaign {
	public OfferCampaign() {
		this.setType(CampaignType.OFFER);
	}

	public OfferCampaign(final OfferCampaignForm offerCampaignForm) {
		super(offerCampaignForm);
	}
}
