package com.greenapper.services.impl;

import com.greenapper.config.SecurityConfig;
import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.factories.CampaignDTOFactory;
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
import java.util.function.Predicate;
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
	public void addCampaignToCampaignManager(final Campaign campaign) {
		final CampaignManager campaignManager = getSessionCampaignManager();
		campaignManager.getCampaigns().removeIf(campaign::equals);
		campaignManager.getCampaigns().add(campaign);
		campaignManagerRepository.save(campaignManager);
	}

	@Override
	public List<CampaignDTO> getCampaigns() {
		return getSessionCampaignManager().getCampaigns().stream().map(campaignDTOFactory::createCampaignDTO).collect(Collectors.toList());
	}

	@Override
	public void updateCampaignState(final Long id, final String newState) {
		final Predicate<Campaign> filterById = campaign -> campaign.getId().equals(id);
		final Campaign campaign = getSessionCampaignManager().getCampaigns().stream().filter(filterById).findFirst().orElse(null);

		if (campaign == null)
			throw new UnknownIdentifierException("The campaign with id: \'" + id + " \' could not be found");

		campaign.setState(CampaignState.valueOf(newState));
		campaignManagerRepository.save(getSessionCampaignManager());
	}

	@Override
	public boolean isCurrentUserPasswordChangeRequired() {
		return campaignManagerRepository.findPasswordChangeRequiredById(sessionService.getSessionUser().getId());
	}

	private CampaignManager getSessionCampaignManager() {
		return (CampaignManager) sessionService.getSessionUser();
	}
}
