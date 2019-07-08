package com.greenapper.models;

import com.greenapper.forms.CampaignManagerProfileForm;

import javax.persistence.*;
import java.util.Optional;

/**
 * Model for campaign manager profiles, which are associated to a {@link CampaignManager}.
 */
@Entity(name = "CampaignManagerProfile")
@Table(name = "CampaignManagerProfile")
public class CampaignManagerProfile {
	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private User campaignManager;

	private String name;

	private String email;

	private String address;

	private String profileImageFilePath;

	public void populate(final CampaignManagerProfileForm profileForm) {
		Optional.ofNullable(profileForm.getCampaignManager()).ifPresent(this::setCampaignManager);
		Optional.ofNullable(profileForm.getName()).ifPresent(this::setName);
		Optional.ofNullable(profileForm.getEmail()).ifPresent(this::setEmail);
		Optional.ofNullable(profileForm.getEmail()).ifPresent(this::setAddress);
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

	public String getProfileImageFilePath() {
		return profileImageFilePath;
	}

	public void setProfileImageFilePath(String profileImageFilePath) {
		this.profileImageFilePath = profileImageFilePath;
	}
}
