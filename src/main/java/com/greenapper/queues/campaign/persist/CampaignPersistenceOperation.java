package com.greenapper.queues.campaign.persist;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.jackson.deserializers.CampaignFormDeserializer;
import com.greenapper.queues.PersistenceOperationType;

public class CampaignPersistenceOperation {

	@JsonDeserialize(using = CampaignFormDeserializer.class)
	private CampaignForm campaignForm;

	private String campaignOwnerUsername;

	private PersistenceOperationType operationType;

	public CampaignPersistenceOperation() {

	}

	public CampaignPersistenceOperation(final CampaignForm campaignForm, final PersistenceOperationType operationType,
										final String campaignOwnerUsername) {
		this.campaignForm = campaignForm;
		this.campaignOwnerUsername = campaignOwnerUsername;
		this.operationType = operationType;
	}

	public PersistenceOperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(PersistenceOperationType operationType) {
		this.operationType = operationType;
	}

	public CampaignForm getCampaignForm() {
		return campaignForm;
	}

	public void setCampaignForm(CampaignForm campaignForm) {
		this.campaignForm = campaignForm;
	}

	public String getCampaignOwnerUsername() {
		return campaignOwnerUsername;
	}

	public void setCampaignOwnerUsername(String campaignOwnerUsername) {
		this.campaignOwnerUsername = campaignOwnerUsername;
	}
}
