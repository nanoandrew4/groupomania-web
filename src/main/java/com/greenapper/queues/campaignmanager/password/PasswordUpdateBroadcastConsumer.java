package com.greenapper.queues.campaignmanager.password;

import com.greenapper.config.SecurityConfig;
import com.greenapper.logging.LogManager;
import com.greenapper.models.CampaignManager;
import com.greenapper.repositories.CampaignManagerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordUpdateBroadcastConsumer {

	@Autowired
	private CampaignManagerRepository campaignManagerRepository;

	@Autowired
	private SecurityConfig securityConfig;

	@Autowired
	private LogManager LOG;

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
