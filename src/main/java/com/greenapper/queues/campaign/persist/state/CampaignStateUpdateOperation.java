package com.greenapper.queues.campaign.persist.state;

public class CampaignStateUpdateOperation {
	private Long targetCampaignId;

	private String targetNewState;

	private String campaignOwnerUsername;

	public CampaignStateUpdateOperation() {

	}

	public CampaignStateUpdateOperation(final String campaignOwnerUsername, final Long targetCampaignId, final String targetNewState) {
		this.campaignOwnerUsername = campaignOwnerUsername;
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

	public String getCampaignOwnerUsername() {
		return campaignOwnerUsername;
	}

	public void setCampaignOwnerUsername(String campaignOwnerUsername) {
		this.campaignOwnerUsername = campaignOwnerUsername;
	}
}
