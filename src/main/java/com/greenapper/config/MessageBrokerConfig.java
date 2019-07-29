package com.greenapper.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableRabbit
public class MessageBrokerConfig {

	@Value("${groupomania.rabbitmq.campaign.queue.name}")
	private String campaignQueueName;

	@Value("${groupomania.rabbitmq.campaign.queue.exchange}")
	private String campaignTopicExchange;

	@Value("${groupomania.rabbitmq.campaign.queue.routingKey}")
	private String campaignRoutingKey;

	@Value("${groupomania.rabbitmq.campaign.state.queue.name}")
	private String campaignStateQueueName;

	@Value("${groupomania.rabbitmq.campaign.state.queue.routingKey}")
	private String campaignStateRoutingKey;

	@Bean
	public List<Declarable> topicBindings() {
		final Queue campaignQueue = new Queue(campaignQueueName, false);
		final Queue campaignStateQueue = new Queue(campaignStateQueueName, false);

		final TopicExchange topicExchange = new TopicExchange(campaignTopicExchange);

		final Binding campaignBinding = BindingBuilder.bind(campaignQueue).to(topicExchange).with(campaignRoutingKey);
		final Binding campaignStateBinding = BindingBuilder.bind(campaignStateQueue).to(topicExchange).with(campaignStateRoutingKey);

		return Arrays.asList(campaignQueue, campaignStateQueue, topicExchange, campaignBinding, campaignStateBinding);
	}
}
