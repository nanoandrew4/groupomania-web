package com.greenapper.queues.campaignmanager.password;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordUpdateBroadcastProducer {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${groupomania.rabbitmq.user.queue.exchange}")
	private String campaignManagerTopicExchange;

	@Value("${groupomania.rabbitmq.user.campaignmanager.password.queue.routingKey}")
	private String campaignManagerRoutingKey;

	public void persistOperation(final PasswordUpdateOperation passwordUpdateOperation) {
		rabbitTemplate.convertAndSend(campaignManagerTopicExchange, campaignManagerRoutingKey, passwordUpdateOperation);
	}
}
