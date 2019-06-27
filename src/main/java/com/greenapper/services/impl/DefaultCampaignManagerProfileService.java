package com.greenapper.services.impl;

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
	public Optional<CampaignManagerProfile> getProfileForCurrentUser() {
		return campaignManagerProfileRepository.findById(sessionService.getSessionUser().getId());
	}

	@Override
	public void updateProfile(final CampaignManagerProfile updatedProfile, final Errors errors) {
		campaignManagerProfileValidator.validate(updatedProfile, errors);
		if (!errors.hasErrors()) {
			final Optional<CampaignManager> campaignManager = campaignManagerRepository.findById(sessionService.getSessionUser().getId());
			if (campaignManager.isPresent()) {
				updatedProfile.setId(campaignManager.get().getId());

				final String profileImagePath = fileSystemStorageService.saveImage(updatedProfile.getProfileImage());
				Optional.ofNullable(profileImagePath).ifPresent(updatedProfile::setProfileImageFilePath);

				campaignManager.get().setCampaignManagerProfile(updatedProfile);
				campaignManagerRepository.save(campaignManager.get());
				campaignManagerProfileRepository.save(updatedProfile);
				sessionService.setSessionUser(campaignManager.get());
			}
		}
	}
}
