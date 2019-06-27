package com.greenapper.services.impl;

import com.greenapper.config.SecurityConfig;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.repositories.CampaignManagerRepository;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultCampaignManagerService implements CampaignManagerService {

	@Autowired
	private SecurityConfig securityConfig;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CampaignManagerRepository campaignManagerRepository;

	@Resource
	private Validator passwordUpdateValidator;

	@Override
	public Optional<CampaignManager> getByUsername(final String username) {
		return Optional.ofNullable(campaignManagerRepository.findByUsername(username));
	}

	@Override
	public void updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		passwordUpdateValidator.validate(passwordUpdateForm, errors);
		if (!errors.hasErrors()) {
			final CampaignManager sessionUser = getSessionCampaignManager();
			sessionUser.setPassword(securityConfig.getPasswordEncoder().encode(passwordUpdateForm.getNewPassword()));
			sessionUser.setPasswordChangeRequired(false);
			campaignManagerRepository.save(sessionUser);
		}
	}

	@Override
	public void addCampaignToCurrentUser(final Campaign campaign) {
		final CampaignManager campaignManager = getSessionCampaignManager();
		campaignManager.getCampaigns().removeIf(campaign::equals);
		campaignManager.getCampaigns().add(campaign);
		campaignManagerRepository.save(campaignManager);
	}

	@Override
	public List<Campaign> getCampaigns() {
		return getSessionCampaignManager().getCampaigns();
	}

	@Override
	public boolean isCurrentUserPasswordChangeRequired() {
		return campaignManagerRepository.findPasswordChangeRequiredById(sessionService.getSessionUser().getId());
	}

	private CampaignManager getSessionCampaignManager() {
		return (CampaignManager) sessionService.getSessionUser();
	}
}
