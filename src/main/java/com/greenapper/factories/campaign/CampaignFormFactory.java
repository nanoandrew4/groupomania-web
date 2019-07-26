package com.greenapper.factories.campaign;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;

import java.util.Optional;

/**
 * Factory that creates a {@link CampaignForm} subtype from a {@link Campaign}.
 */
public interface CampaignFormFactory {
	Optional<CampaignForm> createCampaignForm(final Campaign campaign);
}
