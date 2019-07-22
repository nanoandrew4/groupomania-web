package com.greenapper.services;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.factories.campaign.impl.DefaultCampaignDTOFactory;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.OfferCampaign;
import com.greenapper.repositories.CampaignManagerRepository;
import com.greenapper.services.impl.DefaultCampaignManagerService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCampaignManagerServiceUnitTest {

	@InjectMocks
	private DefaultCampaignManagerService campaignManagerService = new DefaultCampaignManagerService();

	@Mock
	private CampaignManagerRepository campaignManagerRepository;

	@Mock
	private SessionService sessionService;

	@Before
	public void setup() {
		when(sessionService.getSessionUser()).thenReturn(generateSampleCampaignManager());
		campaignManagerService.setCampaignDTOFactory(new DefaultCampaignDTOFactory());
	}

	@Test
	public void getByUsername_nonExistentUsername_returnEmptyOptional() {
		when(campaignManagerRepository.findByUsername(anyString())).thenReturn(null);

		assertEquals(Optional.empty(), campaignManagerService.getByUsername("testusername"));
	}

	@Test
	public void getByUsername_existentUsername_returnCampaignManager() {
		when(campaignManagerRepository.findByUsername(anyString())).thenReturn(generateSampleCampaignManager());

		final CampaignManager campaignManager = campaignManagerService.getByUsername("test").orElse(null);

		assertNotNull(campaignManager);
		assertEquals(generateSampleCampaignManager(), campaignManager);
	}

	@Test
	public void addOrUpdateCampaignForCampaignManager_addNewCampaign_campaignAddedSuccessfully() {
		doAnswer(invocation -> {
			return null;
		}).when(campaignManagerRepository).save(any());

		campaignManagerService.addOrUpdateCampaignForCampaignManager(getMinimalOfferCampaign());

		final List<Campaign> campaigns = ((CampaignManager) sessionService.getSessionUser()).getCampaigns();
		assertEquals(1L, campaigns.size());
		assertEquals(getMinimalOfferCampaign(), campaigns.get(0));
	}

	@Test
	public void addOrUpdateCampaignForCampaignManager_updateCampaignWithSameId_campaignUpdatedSuccessfully() {
		doAnswer(invocation -> {
			return null;
		}).when(campaignManagerRepository).save(any());

		campaignManagerService.addOrUpdateCampaignForCampaignManager(getMinimalOfferCampaign());

		final Campaign campaign = getMinimalOfferCampaign();
		campaign.setTitle("Updated title");
		campaignManagerService.addOrUpdateCampaignForCampaignManager(campaign);

		final List<Campaign> campaigns = ((CampaignManager) sessionService.getSessionUser()).getCampaigns();
		assertEquals(1L, campaigns.size());
		assertEquals(getMinimalOfferCampaign(), campaigns.get(0));
		assertEquals("Updated title", campaigns.get(0).getTitle());
	}

	@Test
	public void getCampaigns_retrieveCampaignManagerCampaigns_retrieveCampaignsSuccessfully() {
		final Campaign campaign1 = getMinimalOfferCampaign();
		final Campaign campaign2 = getMinimalOfferCampaign();

		campaign2.setId(campaign1.getId() + 1);
		campaign2.setStartDate(campaign1.getStartDate().minus(1, ChronoUnit.DAYS));

		final CampaignManager campaignManager = (CampaignManager) sessionService.getSessionUser();
		campaignManager.setCampaigns(Lists.newArrayList(campaign1, campaign2));
		when(campaignManagerService.getSessionCampaignManager()).thenReturn(campaignManager);

		final List<CampaignDTO> campaigns = campaignManagerService.getCampaigns();

		assertEquals(2L, campaigns.size());
		assertEquals(campaign2.getId(), campaigns.get(0).getId());
		assertEquals(campaign1.getId(), campaigns.get(1).getId());
	}

	private CampaignManager generateSampleCampaignManager() {
		final CampaignManager campaignManager = new CampaignManager();
		campaignManager.setId(101L);
		campaignManager.setUsername("testuser");
		campaignManager.setPassword("password");
		campaignManager.setCampaignManagerProfile(new CampaignManagerProfile());
		campaignManager.setCampaigns(new LinkedList<>());
		return campaignManager;
	}

	private Campaign getMinimalOfferCampaign() {
		final OfferCampaign campaign = new OfferCampaign();
		campaign.setId(1L);
		campaign.setTitle("Title");
		campaign.setDescription("Description");
		campaign.setQuantity(1D);
		campaign.setStartDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
		campaign.setEndDate(LocalDate.now().plus(5, ChronoUnit.DAYS));
		campaign.setOriginalPrice(1D);
		campaign.setDiscountedPrice(0.5D);
		campaign.setState(CampaignState.INACTIVE);
		campaign.setOwner((CampaignManager) sessionService.getSessionUser());

		return campaign;
	}
}
