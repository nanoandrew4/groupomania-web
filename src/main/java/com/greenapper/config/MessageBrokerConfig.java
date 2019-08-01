package com.greenapper.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
	public Queue getCampaignQueue() {
		return new Queue(campaignQueueName, false);
	}

	@Bean
	public Queue getCampaignStateQueue() {
		return new Queue(campaignStateQueueName, false);
	}

	@Bean
	public TopicExchange getTopicExchange() {
		return new TopicExchange(campaignTopicExchange);
	}

	@Bean
	public Binding getCampaignBinding() {
		return BindingBuilder.bind(getCampaignQueue()).to(getTopicExchange()).with(campaignRoutingKey);
	}

	@Bean
	public Binding getCampaignStateBinding() {
		return BindingBuilder.bind(getCampaignStateQueue()).to(getTopicExchange()).with(campaignStateRoutingKey);
	}

	@Bean
	public MessageConverter jackson2Converter() {
		return new Jackson2JsonMessageConverter(Jackson2ObjectMapperBuilder.json().build());
	}
}
