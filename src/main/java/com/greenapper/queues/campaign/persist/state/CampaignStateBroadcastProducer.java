package com.greenapper.queues.campaign.persist.state;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CampaignStateBroadcastProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${groupomania.rabbitmq.campaign.queue.exchange}")
	private String campaignStateTopicExchange;

	@Value("${groupomania.rabbitmq.campaign.state.queue.routingKey}")
	private String campaignStateRoutingKey;

	public void enqueueCampaignStateUpdateOperation(final CampaignStateUpdateOperation campaignStateUpdateOperation) {
		rabbitTemplate.convertAndSend(campaignStateTopicExchange, campaignStateRoutingKey, campaignStateUpdateOperation);
	}
}
