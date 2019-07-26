package com.greenapper.forms;

import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.models.User;
import io.swagger.annotations.ApiModelProperty;

import java.util.Optional;

public class CampaignManagerProfileForm {

	@ApiModelProperty(value = "ID of this profile", required = true)
	private Long id;

	@ApiModelProperty(value = "Campaign manager this profile is associated with", required = true)
	private User campaignManager;

	@ApiModelProperty(value = "Name of the campaign manager this profile belongs to", required = true)
	private String name;

	@ApiModelProperty(value = "Email of the campaign manager this profile belongs to", required = true)
	private String email;

	@ApiModelProperty(value = "Physical address of the campaign manager this profile belongs to")
	private String address;

	@ApiModelProperty(value = "Image to associate as the users profile image")
	private ImageForm profileImage;

	private CampaignManagerProfileForm() {
	}

	public CampaignManagerProfileForm(final CampaignManagerProfile profile) {
		Optional.ofNullable(profile.getName()).ifPresent(this::setName);
		Optional.ofNullable(profile.getEmail()).ifPresent(this::setEmail);
		Optional.ofNullable(profile.getAddress()).ifPresent(this::setAddress);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CampaignManager getCampaignManager() {
		return (CampaignManager) campaignManager;
	}

	public void setCampaignManager(User campaignManager) {
		this.campaignManager = campaignManager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ImageForm getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(ImageForm profileImage) {
		this.profileImage = profileImage;
	}
}

