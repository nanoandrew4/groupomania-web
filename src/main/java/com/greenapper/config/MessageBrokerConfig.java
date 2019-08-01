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

	@Value("${groupomania.rabbitmq.user.queue.exchange}")
	private String usersTopicExchange;

	@Value("${groupomania.rabbitmq.user.campaignmanager.password.queue.name}")
	private String userPasswordQueueName;

	@Value("${groupomania.rabbitmq.user.campaignmanager.password.queue.routingKey}")
	private String userPasswordRoutingKey;

	@Value("${groupomania.rabbitmq.user.campaignmanager.profile.queue.name}")
	private String userProfileQueueName;

	@Value("${groupomania.rabbitmq.user.campaignmanager.profile.queue.routingKey}")
	private String userProfileRoutingKey;

	@Bean
	public Queue campaignQueue() {
		return new Queue(campaignQueueName, false);
	}

	@Bean
	public Queue campaignStateQueue() {
		return new Queue(campaignStateQueueName, false);
	}

	@Bean
	public Queue usersCampaignManagerQueue() {
		return new Queue(userPasswordQueueName, false);
	}

	@Bean
	public Queue usersCampaignManagerProfileQueue() {
		return new Queue(userProfileQueueName, false);
	}

	@Bean
	public TopicExchange usersTopicExchange() {
		return new TopicExchange(usersTopicExchange);
	}

	@Bean
	public TopicExchange campaignTopicExchange() {
		return new TopicExchange(campaignTopicExchange);
	}

	@Bean
	public Binding campaignBinding() {
		return BindingBuilder.bind(campaignQueue()).to(campaignTopicExchange()).with(campaignRoutingKey);
	}

	@Bean
	public Binding campaignStateBinding() {
		return BindingBuilder.bind(campaignStateQueue()).to(campaignTopicExchange()).with(campaignStateRoutingKey);
	}

	@Bean
	public Binding usersCampaignManagerBinding() {
		return BindingBuilder.bind(usersCampaignManagerQueue()).to(usersTopicExchange()).with(userPasswordRoutingKey);
	}

	@Bean
	public Binding usersProfileBinding() {
		return BindingBuilder.bind(usersCampaignManagerProfileQueue()).to(usersTopicExchange()).with(userProfileRoutingKey);
	}

	@Bean
	public MessageConverter jackson2Converter() {
		return new Jackson2JsonMessageConverter(Jackson2ObjectMapperBuilder.json().build());
	}
}
