package com.greenapper.factories.campaign;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;

import java.util.Optional;

public interface CampaignFactory {
	Optional<Campaign> createCampaignModel(final CampaignForm campaignForm);
}
