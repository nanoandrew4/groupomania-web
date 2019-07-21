package com.greenapper.dtos;

import com.greenapper.models.CampaignManagerProfile;

public class CampaignManagerProfileDTO {
	private Long campaignManagerId;

	private String name;

	private String email;

	private String address;

	private String profileImageFilePath;

	public CampaignManagerProfileDTO(final CampaignManagerProfile profile) {
		this.campaignManagerId = profile.getCampaignManager().getId();
		this.name = profile.getName();
		this.email = profile.getEmail();
		this.address = profile.getAddress();
		this.profileImageFilePath = profile.getProfileImageFilePath();
	}

	public Long getCampaignManagerId() {
		return campaignManagerId;
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
