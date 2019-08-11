package com.greenapper.queues.campaign.persist;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CampaignBroadcastProducer {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${groupomania.rabbitmq.campaign.queue.exchange}")
	private String campaignTopicExchange;

	@Value("${groupomania.rabbitmq.campaign.queue.routingKey}")
	private String campaignRoutingKey;

	public void persistOperation(final CampaignPersistenceOperation campaignPersistenceOperation) {
		rabbitTemplate.convertAndSend(campaignTopicExchange, campaignRoutingKey, campaignPersistenceOperation);
	}
}
