package com.greenapper.models;

import com.greenapper.models.campaigns.Campaign;

import javax.persistence.*;
import java.util.List;

/**
 * Model for campaign manager users, which extends from the generic {@link User} model.
 */
@Entity
@Table(name = "CampaignManager")
public class CampaignManager extends User {

	@OneToOne(mappedBy = "campaignManager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CampaignManagerProfile campaignManagerProfile;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Campaign> campaigns;

	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CampaignManager otherManager = (CampaignManager) o;
		return getId() != null || (otherManager.getId() == null && getId().equals(otherManager.getId())) ||
			   getUsername().equals(otherManager.getUsername()) ||
			   campaignManagerProfile.getEmail().equals(otherManager.getCampaignManagerProfile().getEmail());
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public CampaignManagerProfile getCampaignManagerProfile() {
		return campaignManagerProfile;
	}

	public void setCampaignManagerProfile(CampaignManagerProfile campaignManagerProfile) {
		this.campaignManagerProfile = campaignManagerProfile;
		this.campaignManagerProfile.setCampaignManager(this);
	}
}
