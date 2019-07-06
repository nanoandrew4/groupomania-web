package com.greenapper.services;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

public interface CampaignManagerService {
	Optional<CampaignManager> getByUsername(final String username);

	void addCampaignToCampaignManager(final Campaign campaign);

	void updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors);

	List<CampaignDTO> getCampaigns();

	void updateCampaignState(final Long id, final String newState);

	boolean isCurrentUserPasswordChangeRequired();
}