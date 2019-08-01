package com.greenapper.queues.campaignmanager.profile;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileUpdateBroadcastProducer {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${groupomania.rabbitmq.user.queue.exchange}")
	private String campaignManagerTopicExchange;

	@Value("${groupomania.rabbitmq.user.campaignmanager.profile.queue.routingKey}")
	private String campaignManagerProfileRoutingKey;

	public void persistOperation(final ProfileUpdateOperation profileUpdateOperation) {
		rabbitTemplate.convertAndSend(campaignManagerTopicExchange, campaignManagerProfileRoutingKey, profileUpdateOperation);
	}
}
