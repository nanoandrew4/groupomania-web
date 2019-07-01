package com.greenapper.factories;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;

import java.util.Optional;

public interface CampaignFormFactory {
	Optional<CampaignForm> createCampaignForm(final Campaign campaign);
}