package com.greenapper.queues.campaign.persist;

import com.greenapper.enums.CampaignState;
import com.greenapper.factories.campaign.CampaignFactory;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.queues.PersistenceOperationType;
import com.greenapper.repositories.CampaignRepository;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.FileSystemStorageService;
import com.greenapper.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CampaignBroadcastConsumer {

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Autowired
	private CampaignFactory campaignFactory;

	@Autowired
	private SessionService sessionService;

	private Logger LOG = LoggerFactory.getLogger(CampaignBroadcastConsumer.class);

	@RabbitListener(queues = {"${groupomania.rabbitmq.campaign.queue.name}"})
	public void receivePersistCampaignOperation(final CampaignPersistenceOperation campaignPersistenceOperation) {
		if (campaignPersistenceOperation.getOperationType() == PersistenceOperationType.CREATE)
			createCampaign(campaignPersistenceOperation);
		else
			updateCampaign(campaignPersistenceOperation);
	}

	private void createCampaign(final CampaignPersistenceOperation campaignPersistenceOperation) {
		campaignFactory.createCampaignModel(campaignPersistenceOperation.getCampaignForm()).ifPresent(campaign -> {
			campaign.setOwner((CampaignManager) sessionService.getSessionUser());
			campaign.setState(CampaignState.INACTIVE);
			campaignPersistenceOperation.getSetDefaultsForCampaign().accept(campaign);
			saveCampaign(campaign, campaignPersistenceOperation.getCampaignForm());
			LOG.info("Created campaign with ID: " + campaign.getId() + " of type: " + campaign.getType() + " for user: " + campaign.getOwner().getId());
		});
	}

	private void updateCampaign(final CampaignPersistenceOperation campaignPersistenceOperation) {
		campaignFactory.createCampaignModel(campaignPersistenceOperation.getCampaignForm()).ifPresent(campaign -> {
			campaign.setOwner((CampaignManager) sessionService.getSessionUser());
			saveCampaign(campaign, campaignPersistenceOperation.getCampaignForm());
			LOG.info("Updated campaign with ID: " + campaign.getId() + " of type: " + campaign.getType() + " for user: " + campaign.getOwner().getId());
		});
	}

	/**
	 * Saves the campaign image to the filesystem and associates it to the model if applicable, and saves the campaign
	 * in the {@link CampaignRepository} as well as storing it in the campaign list of the {@link CampaignManager}
	 * currently in session.
	 *
	 * @param campaign     Campaign model that will be persisted to the database
	 * @param campaignForm Campaign form from which the model was created, which possibly contains the
	 *                     {@link org.springframework.web.multipart.MultipartFile} that will be persisted to the file system
	 */
	private void saveCampaign(final Campaign campaign, final CampaignForm campaignForm) {
		final String imagePath = fileSystemStorageService.saveImage(campaignForm.getCampaignImage());
		Optional.ofNullable(imagePath).ifPresent(campaign::setCampaignImageFilePath);

		campaignRepository.save(campaign);
		campaignManagerService.addOrUpdateCampaignForCampaignManager(campaign);
	}
}
