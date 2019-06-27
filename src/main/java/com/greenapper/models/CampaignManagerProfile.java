package com.greenapper.models;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

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

	@Transient
	private MultipartFile profileImage;

	private String profileImageFilePath;

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

	public String getProfileImageFilePath() {
		return profileImageFilePath;
	}

	public void setProfileImageFilePath(String profileImageFilePath) {
		this.profileImageFilePath = profileImageFilePath;
	}
}
