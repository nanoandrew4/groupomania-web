package com.greenapper.services.impl;

import com.greenapper.config.SecurityConfig;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.factories.campaign.CampaignDTOFactory;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@Autowired
	private CampaignDTOFactory campaignDTOFactory;

	@Override
	public Optional<CampaignManager> getByUsername(final String username) {
		return Optional.ofNullable(campaignManagerRepository.findByUsername(username));
	}

	@Override
	public void updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		passwordUpdateValidator.validate(passwordUpdateForm, errors);

		if (errors.hasErrors())
			throw new ValidationException("Password update encountered validation errors", errors);

		final CampaignManager sessionUser = getSessionCampaignManager();
		sessionUser.setPassword(securityConfig.getPasswordEncoder().encode(passwordUpdateForm.getNewPassword()));
		sessionUser.setPasswordChangeRequired(false);
		campaignManagerRepository.save(sessionUser);
	}

	@Override
	public void addOrUpdateCampaignForCampaignManager(final Campaign campaign) {
		final CampaignManager campaignManager = getSessionCampaignManager();
		campaignManager.getCampaigns().removeIf(campaign::equals);
		campaignManager.getCampaigns().add(campaign);
		campaignManagerRepository.save(campaignManager);
	}

	@Override
	public List<CampaignDTO> getCampaigns() {
		return getSessionCampaignManager().getCampaigns().stream().sorted(Comparator.comparing(Campaign::getStartDate))
				.map(campaignDTOFactory::createCampaignDTO).collect(Collectors.toList());
	}

	@Override
	public boolean isCurrentUserPasswordChangeRequired() {
		return campaignManagerRepository.findPasswordChangeRequiredById(sessionService.getSessionUser().getId());
	}

	public CampaignManager getSessionCampaignManager() {
		return (CampaignManager) sessionService.getSessionUser();
	}

	public void setCampaignDTOFactory(CampaignDTOFactory campaignDTOFactory) {
		this.campaignDTOFactory = campaignDTOFactory;
	}
}
