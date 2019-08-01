package com.greenapper.queues.campaignmanager.profile;

import com.greenapper.forms.CampaignManagerProfileForm;

public class ProfileUpdateOperation {
	private CampaignManagerProfileForm profileForm;

	private String profileUpdateUser;

	public ProfileUpdateOperation() {

	}

	public ProfileUpdateOperation(final CampaignManagerProfileForm profileForm, final String profileUpdateUser) {
		this.profileForm = profileForm;
		this.profileUpdateUser = profileUpdateUser;
	}

	public CampaignManagerProfileForm getProfileForm() {
		return profileForm;
	}

	public void setProfileForm(CampaignManagerProfileForm profileForm) {
		this.profileForm = profileForm;
	}

	public String getProfileUpdateUser() {
		return profileUpdateUser;
	}

	public void setProfileUpdateUser(String profileUpdateUser) {
		this.profileUpdateUser = profileUpdateUser;
	}
}
