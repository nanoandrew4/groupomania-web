package com.greenapper.queues.campaign.persist.state;

public class CampaignStateUpdateOperation {
	private Long targetCampaignId;

	private String targetNewState;

	public CampaignStateUpdateOperation(final Long targetCampaignId, final String targetNewState) {
		this.targetCampaignId = targetCampaignId;
		this.targetNewState = targetNewState;
	}

	public String getTargetNewState() {
		return targetNewState;
	}

	public Long getTargetCampaignId() {
		return targetCampaignId;
	}
}
