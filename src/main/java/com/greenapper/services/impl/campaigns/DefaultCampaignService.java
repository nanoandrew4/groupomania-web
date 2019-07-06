package com.greenapper.services.impl.campaigns;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.factories.CampaignDTOFactory;
import com.greenapper.factories.CampaignFactory;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.repositories.CampaignRepository;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.CampaignService;
import com.greenapper.services.FileSystemStorageService;
import com.greenapper.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class DefaultCampaignService implements CampaignService {

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private FileSystemStorageService fileSystemStorageService;

	@Autowired
	private CampaignFactory campaignFactory;

	@Autowired
	private CampaignDTOFactory campaignDTOFactory;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	public DefaultCampaignService() {

	}

	@Override
	public void createCampaign(final CampaignForm campaignForm, final Errors errors) {
		validateCampaign(campaignForm, errors);
		if (!errors.hasErrors()) {
			campaignFactory.createCampaignModel(campaignForm).ifPresent(campaign -> {
				campaign.setOwner(getSessionCampaignManager());
				campaign.setState(CampaignState.INACTIVE);
				setDefaultsForCampaignSubtype(campaign);
				saveCampaign(campaign, campaignForm);
				LOG.info("Created campaign with ID: " + campaign.getId() + " of type: " + campaign.getType() + " for user: " + campaign.getOwner().getId());
			});
		}
	}

	@Override
	public void updateCampaign(final CampaignForm campaignForm, final Errors errors) {
		validateCampaign(campaignForm, errors);
		if (!errors.hasErrors()) {
			campaignFactory.createCampaignModel(campaignForm).ifPresent(campaign -> {
				campaign.setOwner(getSessionCampaignManager());
				saveCampaign(campaign, campaignForm);
				LOG.info("Updated campaign with ID: " + campaign.getId() + " of type: " + campaign.getType() + " for user: " + campaign.getOwner().getId());
			});
		}
	}

	@Override
	public void updateCampaignState(final Long id, final String state) {
		campaignRepository.findById(id).ifPresent(campaign -> {
			campaign.setState(CampaignState.valueOf(state.toUpperCase()));
			if (campaign.getState() == CampaignState.ARCHIVED) {
				if (LocalDate.now().isBefore(campaign.getStartDate()))
					campaign.setStartDate(LocalDate.now());
				campaign.setEndDate(LocalDate.now());
			}
			campaignManagerService.addCampaignToCampaignManager(campaign);
			LOG.info("Created campaign state for campaign with ID: " + campaign.getId() + " of type: " + campaign.getType()
					 + " for user: " + campaign.getOwner().getId() + " and with new state: " + campaign.getState());
		});
	}

	@Override
	public Optional<CampaignDTO> getCampaignById(final Long id) {
		return campaignRepository.findById(id).map(campaignDTOFactory::createCampaignDTO);
	}

	@Override
	public Optional<CampaignDTO> getCampaignByIdForSessionUser(final Long id) {
		final Predicate<Campaign> filterByOwner = campaign -> campaign.getOwner().getId().equals(sessionService.getSessionUser().getId());
		return campaignRepository.findById(id).filter(filterByOwner).map(campaignDTOFactory::createCampaignDTO);
	}

	@Override
	public List<CampaignDTO> getAllCampaigns() {
		return campaignRepository.findAll().stream().map(campaignDTOFactory::createCampaignDTO).collect(Collectors.toList());
	}

	/**
	 * Saves the campaign image to the filesystem and associates it to the model if applicable, and saves the campaign
	 * in the {@link CampaignRepository} as well as storing it in the campaign list of the {@link CampaignManager}
	 * currently in session.
	 *
	 * @param campaign     Campaign model that will be persisted to the database
	 * @param campaignForm Campaign form from which the model was created, which possibly contains the
	 *                     {@link org.springframework.web.multipart.MultipartFile} that will be persisted to the file system
	 */
	private void saveCampaign(final Campaign campaign, final CampaignForm campaignForm) {
		final String imagePath = fileSystemStorageService.saveImage(campaignForm.getCampaignImage());
		Optional.ofNullable(imagePath).ifPresent(campaign::setCampaignImageFilePath);

		campaignRepository.save(campaign);
		campaignManagerService.addCampaignToCampaignManager(campaign);
	}

	private CampaignManager getSessionCampaignManager() {
		return (CampaignManager) sessionService.getSessionUser();
	}
}
