package com.greenapper.services.impl;

import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.exceptions.NotFoundException;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.repositories.CampaignManagerProfileRepository;
import com.greenapper.repositories.CampaignManagerRepository;
import com.greenapper.services.CampaignManagerProfileService;
import com.greenapper.services.FileSystemStorageService;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class DefaultCampaignManagerProfileService implements CampaignManagerProfileService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Autowired
	private CampaignManagerRepository campaignManagerRepository;

	@Autowired
	private CampaignManagerProfileRepository campaignManagerProfileRepository;

	@Resource
	private Validator campaignManagerProfileValidator;

	@Override
	public CampaignManagerProfileDTO getProfileForCurrentUser() {
		final Long sessionUserId = sessionService.getSessionUser().getId();
		final CampaignManagerProfile profile = campaignManagerProfileRepository.findById(sessionUserId).orElse(null);

		if (profile == null)
			throw new NotFoundException("Campaign manager profile was not found, for manager with id: \'" + sessionUserId + "\'");
		return new CampaignManagerProfileDTO(profile);
	}

	@Override
	public void updateProfile(final CampaignManagerProfile updatedProfile, final Errors errors) {
		campaignManagerProfileValidator.validate(updatedProfile, errors);

		if (errors.hasErrors())
			throw new ValidationException("Profile update for campaign manager with id: \'" + updatedProfile.getId() + "\' encountered validation errors", errors);

		final Long sessionUserId = sessionService.getSessionUser().getId();
		final CampaignManager campaignManager = campaignManagerRepository.findById(sessionUserId)
				.orElseThrow(() -> new UnknownIdentifierException("Could not find user with id: \'" + sessionUserId + "\'"));
		updatedProfile.setId(sessionUserId);

		final String profileImagePath = fileSystemStorageService.saveImage(updatedProfile.getProfileImage());
		Optional.ofNullable(profileImagePath).ifPresent(updatedProfile::setProfileImageFilePath);

		campaignManager.setCampaignManagerProfile(updatedProfile);
		campaignManagerRepository.save(campaignManager);
		campaignManagerProfileRepository.save(updatedProfile);
		sessionService.setSessionUser(campaignManager);
	}
}
