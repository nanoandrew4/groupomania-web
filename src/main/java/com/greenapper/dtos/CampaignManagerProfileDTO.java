package com.greenapper.dtos;

import com.greenapper.models.CampaignManagerProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO to represent a campaign managers profile")
public class CampaignManagerProfileDTO {

	@ApiModelProperty(value = "ID of the campaign manager this profile is associated with", required = true)
	private Long campaignManagerId;

	@ApiModelProperty(value = "Name of the campaign manager this profile belongs to", required = true)
	private String name;

	@ApiModelProperty(value = "Email of the campaign manager this profile belongs to", required = true)
	private String email;

	@ApiModelProperty(value = "Physical address of the campaign manager this profile belongs to")
	private String address;

	@ApiModelProperty(value = "Path to send to the /images endpoint to retrieve the profile picture of this user")
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
