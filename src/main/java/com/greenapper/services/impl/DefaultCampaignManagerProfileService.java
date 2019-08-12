package com.greenapper.services.impl;

import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.exceptions.NotFoundException;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.forms.CampaignManagerProfileForm;
import com.greenapper.logging.LogManager;
import com.greenapper.queues.campaignmanager.profile.ProfileUpdateBroadcastProducer;
import com.greenapper.queues.campaignmanager.profile.ProfileUpdateOperation;
import com.greenapper.repositories.CampaignManagerProfileRepository;
import com.greenapper.services.CampaignManagerProfileService;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Service
public class DefaultCampaignManagerProfileService implements CampaignManagerProfileService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CampaignManagerProfileRepository campaignManagerProfileRepository;

	@Resource
	private Validator campaignManagerProfileValidator;

	@Autowired
	private ProfileUpdateBroadcastProducer profileUpdateBroadcastProducer;

	@Autowired
	private LogManager LOG;

	@Override
	public CampaignManagerProfileDTO getProfileForCurrentUser() {
		final Long sessionUserId = sessionService.getSessionUser().getId();
		return campaignManagerProfileRepository.findById(sessionUserId).map(CampaignManagerProfileDTO::new)
				.orElseThrow(() -> new NotFoundException("Campaign manager profile was not found, for manager with id: \'" + sessionUserId + "\'"));
	}

	@Override
	public void updateProfile(final CampaignManagerProfileForm updatedProfile, final Errors errors) {
		campaignManagerProfileValidator.validate(updatedProfile, errors);

		if (errors.hasErrors())
			throw new ValidationException("Profile update for campaign manager with id: \'" + updatedProfile.getId() + "\' encountered validation errors", errors);

		profileUpdateBroadcastProducer.persistOperation(new ProfileUpdateOperation(updatedProfile, sessionService.getSessionUser().getUsername()));

		LOG.info("Enqueued profile update operation for user with ID: " + sessionService.getSessionUser().getId());
	}
}
