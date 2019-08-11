package com.greenapper.services.impl;

import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.factories.campaign.CampaignDTOFactory;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.queues.campaignmanager.password.PasswordUpdateBroadcastProducer;
import com.greenapper.queues.campaignmanager.password.PasswordUpdateOperation;
import com.greenapper.repositories.CampaignManagerRepository;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private SessionService sessionService;

	@Autowired
	private CampaignManagerRepository campaignManagerRepository;

	@Resource
	private Validator passwordUpdateValidator;

	@Autowired
	private CampaignDTOFactory campaignDTOFactory;

	@Autowired
	private PasswordUpdateBroadcastProducer passwordUpdateBroadcastProducer;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public Optional<CampaignManager> getByUsername(final String username) {
		return Optional.ofNullable(campaignManagerRepository.findByUsername(username));
	}

	@Override
	public void updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors) {
		passwordUpdateValidator.validate(passwordUpdateForm, errors);

		if (errors.hasErrors())
			throw new ValidationException("Password update encountered validation errors", errors);

		passwordUpdateBroadcastProducer.persistOperation(new PasswordUpdateOperation(passwordUpdateForm, sessionService.getSessionUser().getUsername()));

		LOG.info("Enqueued password update operation for user with ID: " + sessionService.getSessionUser().getId());
	}

	@Override
	public void addOrUpdateCampaignForCampaignManager(final CampaignManager campaignManager, final Campaign campaign) {
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
