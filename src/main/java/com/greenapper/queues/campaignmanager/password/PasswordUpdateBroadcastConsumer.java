package com.greenapper.queues.campaignmanager.password;

import com.greenapper.config.SecurityConfig;
import com.greenapper.models.CampaignManager;
import com.greenapper.repositories.CampaignManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordUpdateBroadcastConsumer {

	@Autowired
	private CampaignManagerRepository campaignManagerRepository;

	@Autowired
	private SecurityConfig securityConfig;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	@RabbitListener(queues = {"${groupomania.rabbitmq.user.campaignmanager.password.queue.name}"})
	public void updatePassword(final PasswordUpdateOperation passwordUpdateOperation) {
		final String hashedPassword = securityConfig.getPasswordEncoder().encode(passwordUpdateOperation.getPasswordUpdateForm().getNewPassword());

		final CampaignManager campaignManager = campaignManagerRepository.findByUsername(passwordUpdateOperation.getPasswordUpdateUser());
		campaignManager.setPassword(hashedPassword);
		campaignManager.setPasswordChangeRequired(false);
		campaignManagerRepository.save(campaignManager);

		LOG.info("Updated password for user with ID: " + campaignManager.getId());
	}
}
