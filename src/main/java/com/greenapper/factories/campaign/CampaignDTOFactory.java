package com.greenapper.factories.campaign;

import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.models.campaigns.Campaign;

/**
 * Factory that creates a {@link CampaignDTO} subclass from a {@link Campaign}.
 */
public interface CampaignDTOFactory {
	CampaignDTO createCampaignDTO(final Campaign campaign);
}
