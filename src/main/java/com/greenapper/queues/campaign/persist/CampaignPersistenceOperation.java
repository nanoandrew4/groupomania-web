package com.greenapper.queues.campaign.persist;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.queues.PersistenceOperationType;

import java.util.function.Consumer;

public class CampaignPersistenceOperation {

	private Campaign campaign;

	private CampaignForm campaignForm;

	private PersistenceOperationType operationType;

	private Consumer<Campaign> setDefaultsForCampaign;

	public CampaignPersistenceOperation(final CampaignForm campaignForm, final PersistenceOperationType operationType,
										final Consumer<Campaign> setDefaultsForCampaign) {
		this.campaignForm = campaignForm;
		this.operationType = operationType;
		this.setDefaultsForCampaign = setDefaultsForCampaign;
	}

	public Consumer<Campaign> getSetDefaultsForCampaign() {
		return setDefaultsForCampaign;
	}

	public PersistenceOperationType getOperationType() {
		return operationType;
	}

	public CampaignForm getCampaignForm() {
		return campaignForm;
	}

	public Campaign getCampaign() {
		return campaign;
	}
}
