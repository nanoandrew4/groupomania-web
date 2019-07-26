package com.greenapper.factories.campaign;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;

import java.util.Optional;

/**
 * Factory that creates a {@link Campaign} subtype from a {@link CampaignForm}.
 */
public interface CampaignFactory {
	Optional<Campaign> createCampaignModel(final CampaignForm campaignForm);
}
