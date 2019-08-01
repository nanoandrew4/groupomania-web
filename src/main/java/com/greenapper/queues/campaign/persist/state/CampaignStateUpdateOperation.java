package com.greenapper.queues.campaign.persist.state;

import com.greenapper.models.CampaignManager;

public class CampaignStateUpdateOperation {
	private Long targetCampaignId;

	private String targetNewState;

	private CampaignManager campaignOwner;

	public CampaignStateUpdateOperation() {

	}

	public CampaignStateUpdateOperation(final CampaignManager campaignOwner, final Long targetCampaignId, final String targetNewState) {
		this.campaignOwner = campaignOwner;
		this.targetCampaignId = targetCampaignId;
		this.targetNewState = targetNewState;
	}

	public String getTargetNewState() {
		return targetNewState;
	}

	public void setTargetNewState(String targetNewState) {
		this.targetNewState = targetNewState;
	}

	public Long getTargetCampaignId() {
		return targetCampaignId;
	}

	public void setTargetCampaignId(Long targetCampaignId) {
		this.targetCampaignId = targetCampaignId;
	}

	public CampaignManager getCampaignOwner() {
		return campaignOwner;
	}

	public void setCampaignOwner(CampaignManager campaignOwner) {
		this.campaignOwner = campaignOwner;
	}
}
