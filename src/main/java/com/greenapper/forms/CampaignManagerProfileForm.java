package com.greenapper.forms;

import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class CampaignManagerProfileForm {
	private Long id;

	private User campaignManager;

	private String name;

	private String email;

	private String address;

	private MultipartFile profileImage;

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

	public MultipartFile getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}
}

