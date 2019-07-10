package com.greenapper.dtos;

import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.models.User;

public class CampaignManagerProfileDTO {
	private User campaignManager;

	private String name;

	private String email;

	private String address;

	private String profileImageFilePath;

	public CampaignManagerProfileDTO(final CampaignManagerProfile profile) {
		this.campaignManager = profile.getCampaignManager();
		this.name = profile.getName();
		this.email = profile.getEmail();
		this.address = profile.getAddress();
		this.profileImageFilePath = profile.getProfileImageFilePath();
	}

	public CampaignManager getCampaignManager() {
		return (CampaignManager) campaignManager;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getProfileImageFilePath() {
		return profileImageFilePath;
	}
}
