package com.greenapper.queues.campaign.persist.state;

import com.greenapper.enums.CampaignState;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.repositories.CampaignRepository;
import com.greenapper.services.CampaignManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.Predicate;

@Component
public class CampaignStateBroadcastConsumer {

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private CampaignManagerService campaignManagerService;

	private Logger LOG = LoggerFactory.getLogger(CampaignStateBroadcastConsumer.class);

	@RabbitListener(queues = {"${groupomania.rabbitmq.campaign.state.queue.name}"})
	public void updateCampaignState(final CampaignStateUpdateOperation campaignStateUpdateOperation) {
		final Long id = campaignStateUpdateOperation.getTargetCampaignId();
		final String state = campaignStateUpdateOperation.getTargetNewState();

		final CampaignManager campaignManager = campaignManagerService.getByUsername(campaignStateUpdateOperation.getCampaignOwnerUsername()).orElse(null);

		if (campaignManager == null)
			throw new UnknownIdentifierException("The user with usernam: " + campaignStateUpdateOperation.getCampaignOwnerUsername() + " could not be retrieved for the campaign state update operation");

		final Predicate<Campaign> filterByOwner = campaign -> campaign.getOwner().getId().equals(campaignManager.getId());
		final Campaign campaign = campaignRepository.findById(id).filter(filterByOwner).orElse(null);
		if (campaign == null) {
			LOG.error("Could not update state for non-existent campaign with id: " + id);
			return;
		}

		campaign.setState(CampaignState.valueOf(state.toUpperCase()));
		if (campaign.getState() == CampaignState.ARCHIVED) {
			if (LocalDate.now().isBefore(campaign.getStartDate()))
				campaign.setStartDate(LocalDate.now());
			campaign.setEndDate(LocalDate.now());
		}
		campaignManagerService.addOrUpdateCampaignForCampaignManager(campaignManager, campaign);
		LOG.info("Updated campaign state for campaign with ID: " + campaign.getId() + " of type: " + campaign.getType()
				 + " for user: " + campaign.getOwner().getId() + " and with new state: " + campaign.getState());
	}
}
