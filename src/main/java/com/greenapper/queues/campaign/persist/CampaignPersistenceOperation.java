package com.greenapper.queues.campaign.persist;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.jackson.deserializers.CampaignFormDeserializer;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.queues.PersistenceOperationType;

import java.util.function.Consumer;

public class CampaignPersistenceOperation {

	@JsonDeserialize(using = CampaignFormDeserializer.class)
	private CampaignForm campaignForm;

	private String campaignOwnerUsername;

	private PersistenceOperationType operationType;

	private Consumer<Campaign> setDefaultsForCampaign;

	public CampaignPersistenceOperation() {

	}

	public CampaignPersistenceOperation(final CampaignForm campaignForm, final PersistenceOperationType operationType,
										final Consumer<Campaign> setDefaultsForCampaign, final String campaignOwnerUsername) {
		this.campaignForm = campaignForm;
		this.campaignOwnerUsername = campaignOwnerUsername;
		this.operationType = operationType;
		this.setDefaultsForCampaign = setDefaultsForCampaign;
	}

	public Consumer<Campaign> getSetDefaultsForCampaign() {
		return setDefaultsForCampaign;
	}

	public void setSetDefaultsForCampaign(Consumer<Campaign> setDefaultsForCampaign) {
		this.setDefaultsForCampaign = setDefaultsForCampaign;
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
