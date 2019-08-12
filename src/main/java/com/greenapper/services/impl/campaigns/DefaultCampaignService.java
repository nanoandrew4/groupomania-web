package com.greenapper.services.impl.campaigns;

import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.factories.campaign.CampaignDTOFactory;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.logging.LogManager;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.queues.PersistenceOperationType;
import com.greenapper.queues.campaign.persist.CampaignBroadcastProducer;
import com.greenapper.queues.campaign.persist.CampaignPersistenceOperation;
import com.greenapper.queues.campaign.persist.state.CampaignStateBroadcastProducer;
import com.greenapper.queues.campaign.persist.state.CampaignStateUpdateOperation;
import com.greenapper.repositories.CampaignRepository;
import com.greenapper.services.CampaignService;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class DefaultCampaignService implements CampaignService {

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CampaignDTOFactory campaignDTOFactory;

	@Autowired
	private CampaignBroadcastProducer campaignBroadcastProducer;

	@Autowired
	private CampaignStateBroadcastProducer campaignStateBroadcastProducer;

	@Autowired
	private LogManager LOG;

	@Override
	public void createCampaign(final CampaignForm campaignForm, final Errors errors) {
		validateCampaign(campaignForm, errors);

		if (errors.hasErrors())
			throw new ValidationException("Validation errors where encountered when updating a campaign", errors);

		setDefaultsForCampaignSubtype(campaignForm);
		campaignBroadcastProducer.persistOperation(
				new CampaignPersistenceOperation(campaignForm, PersistenceOperationType.CREATE, sessionService.getSessionUser().getUsername()));
		LOG.info("Successfully enqueued campaign creation for user with id: " + sessionService.getSessionUser().getId());
	}

	@Override
	public void updateCampaign(final CampaignForm campaignForm, final Errors errors) {
		validateCampaign(campaignForm, errors);

		if (campaignForm.getId() == null)
			throw new UnknownIdentifierException("No id was found, which is necessary when updating a campaign");

		if (errors.hasErrors())
			throw new ValidationException("Validation errors where encountered when updating a campaign", errors);

		campaignBroadcastProducer.persistOperation(
				new CampaignPersistenceOperation(campaignForm, PersistenceOperationType.UPDATE, sessionService.getSessionUser().getUsername()));
		LOG.info("Successfully enqueued campaign modification for user with id: " + sessionService.getSessionUser().getId() +
				 " and campaign with id: " + campaignForm.getId());
	}

	@Override
	public void updateCampaignState(final Long id, final String state) {
		final CampaignManager campaignManager = (CampaignManager) sessionService.getSessionUser();
		if (campaignManager == null || campaignManager.getCampaigns() == null || campaignManager.getCampaigns().stream().map(Campaign::getId).noneMatch(id::equals))
			throw new UnknownIdentifierException("The user in session has no campaign with id: " + id);

		campaignStateBroadcastProducer.enqueueCampaignStateUpdateOperation(
				new CampaignStateUpdateOperation(sessionService.getSessionUser().getUsername(), id, state));
		LOG.info("Successfully enqueued campaign state modification for user with id: " + sessionService.getSessionUser().getId() +
				 " and campaign with id: " + id);
	}

	@Override
	public CampaignDTO getCampaignById(final Long id) {
		return campaignRepository.findById(id).map(campaignDTOFactory::createCampaignDTO)
				.orElseThrow(() -> new UnknownIdentifierException("Campaign with id: \'" + id + "\' could not be found"));
	}

	@Override
	public CampaignDTO getCampaignByIdForSessionUser(final Long id) {
		final Predicate<Campaign> filterByOwner = campaign -> campaign.getOwner().equals(sessionService.getSessionUser());
		return campaignRepository.findById(id).filter(filterByOwner).map(campaignDTOFactory::createCampaignDTO)
				.orElseThrow(() -> new UnknownIdentifierException("Campaign with id: \'" + id + "\' does not belong to the session user"));
	}

	@Override
	public List<CampaignDTO> getAllCampaigns() {
		return campaignRepository.findAll().stream().sorted(Comparator.comparing(Campaign::getStartDate))
				.map(campaignDTOFactory::createCampaignDTO).collect(Collectors.toList());
	}

	public void setCampaignDTOFactory(CampaignDTOFactory campaignDTOFactory) {
		this.campaignDTOFactory = campaignDTOFactory;
	}
}
