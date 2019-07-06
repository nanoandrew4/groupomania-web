package com.greenapper.factories;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.models.campaigns.Campaign;

public interface CampaignDTOFactory {
	CampaignDTO createCampaignDTO(final Campaign campaign);
}
