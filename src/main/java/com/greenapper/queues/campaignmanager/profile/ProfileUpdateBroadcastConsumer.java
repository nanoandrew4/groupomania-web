package com.greenapper.queues.campaignmanager.profile;

import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.repositories.CampaignManagerProfileRepository;
import com.greenapper.repositories.CampaignManagerRepository;
import com.greenapper.services.FileSystemStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileUpdateBroadcastConsumer {

	@Autowired
	private CampaignManagerRepository campaignManagerRepository;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Autowired
	private CampaignManagerProfileRepository campaignManagerProfileRepository;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	@RabbitListener(queues = {"${groupomania.rabbitmq.user.campaignmanager.profile.queue.name}"})
	public void updateProfile(final ProfileUpdateOperation profileUpdateOperation) {
		final CampaignManager campaignManager = campaignManagerRepository.findByUsername(profileUpdateOperation.getProfileUpdateUser());
		final CampaignManagerProfile profile = campaignManagerProfileRepository.findById(campaignManager.getId()).orElseGet(CampaignManagerProfile::new);
		profile.populate(profileUpdateOperation.getProfileForm());

		final String profileImagePath = fileSystemStorageService.saveImage(campaignManager.getUsername(), profileUpdateOperation.getProfileForm().getProfileImage());
		Optional.ofNullable(profileImagePath).ifPresent(profile::setProfileImageFilePath);

		campaignManager.setCampaignManagerProfile(profile);
		campaignManagerRepository.save(campaignManager);

		LOG.info("Updated profile for user with ID: " + campaignManager.getId());
	}
}
