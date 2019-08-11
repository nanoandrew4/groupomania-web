package com.greenapper.services;

import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.factories.campaign.impl.DefaultCampaignDTOFactory;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.OfferCampaign;
import com.greenapper.repositories.CampaignRepository;
import com.greenapper.services.impl.campaigns.DefaultCampaignService;
import com.greenapper.services.impl.campaigns.OfferCampaignService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCampaignServiceUnitTest {
	@Mock
	private SessionService sessionService;

	@InjectMocks
	private DefaultCampaignService campaignService = new OfferCampaignService();

	@Mock
	private CampaignRepository campaignRepository;

	@Before
	public void setup() {
		doAnswer(invocation -> {
			final CampaignManager campaignManager = new CampaignManager();
			campaignManager.setId(101L);
			campaignManager.setUsername("testuser");
			campaignManager.setPassword("password");
			campaignManager.setCampaignManagerProfile(new CampaignManagerProfile());
			return campaignManager;
		}).when(sessionService).getSessionUser();

		campaignService.setCampaignDTOFactory(new DefaultCampaignDTOFactory());
	}

	@Test
	public void getCampaignById_nonExistentCampaign_throwUnknownIdentifierException() {
		when(campaignRepository.findById(anyLong())).thenReturn(Optional.empty());

		try {
			campaignService.getCampaignById(1L);
			fail("Exception should have been thrown due to unknown identifier");
		} catch (UnknownIdentifierException ignored) {
		}
	}

	@Test
	public void getCampaignById_existentCampaign_returnCampaignSuccessfully() {
		final Optional<Campaign> campaign = Optional.of(getMinimalOfferCampaign());
		campaign.get().setOwner(new CampaignManager());

		when(campaignRepository.findById(anyLong())).thenReturn(campaign);

		final CampaignDTO campaignDTO;
		campaignDTO = campaignService.getCampaignById(1L);

		assertNotNull(campaignDTO);
		assertEquals(1L, campaignDTO.getId().longValue());
		assertEquals("Title", campaignDTO.getTitle());
		assertEquals("Description", campaignDTO.getDescription());
		assertEquals("0.5", campaignDTO.getDiscountedPrice());
		assertEquals(CampaignType.OFFER, campaignDTO.getType());
	}

	@Test
	public void getCampaignByIdForSessionUser_nonExistentCampaign_throwUnknownIdentifierException() {
		when(campaignRepository.findById(anyLong())).thenReturn(Optional.empty());

		try {
			campaignService.getCampaignByIdForSessionUser(1L);
			fail("Exception should have been thrown due to unknown identifier");
		} catch (UnknownIdentifierException ignored) {
		}
	}

	@Test
	public void getCampaignByIdForSessionUser_existentCampaignBelongingToOtherUser_throwUnknownIdentifierException() {
		final Optional<Campaign> campaign = Optional.of(getMinimalOfferCampaign());
		campaign.get().setOwner(new CampaignManager());

		when(campaignRepository.findById(anyLong())).thenReturn(campaign);

		try {
			campaignService.getCampaignByIdForSessionUser(1L);
			fail("Exception should have been thrown due to unknown identifier");
		} catch (UnknownIdentifierException ignored) {
		}
	}

	@Test
	public void getCampaignByIdForSessionUser_existentCampaign_returnSuccessfully() {
		final Optional<Campaign> campaign = Optional.of(getMinimalOfferCampaign());
		when(campaignRepository.findById(anyLong())).thenReturn(campaign);

		final CampaignDTO campaignDTO = campaignService.getCampaignByIdForSessionUser(1L);

		assertNotNull(campaignDTO);
		assertEquals(1L, campaignDTO.getId().longValue());
		assertEquals("Title", campaignDTO.getTitle());
		assertEquals("Description", campaignDTO.getDescription());
		assertEquals("0.5", campaignDTO.getDiscountedPrice());
		assertEquals(CampaignType.OFFER, campaignDTO.getType());
	}

	@Test
	public void getAllCampaigns_campaignsExist_returnStoredCampaigns() {
		final Campaign campaign = getMinimalOfferCampaign();
		when(campaignRepository.findAll()).thenReturn(Lists.newArrayList(campaign));

		final List<CampaignDTO> campaigns = campaignService.getAllCampaigns();

		assertEquals(1, campaigns.size());
		assertNotNull(campaigns.get(0));
		assertEquals(1L, campaigns.get(0).getId().longValue());
		assertEquals("Title", campaigns.get(0).getTitle());
		assertEquals("Description", campaigns.get(0).getDescription());
		assertEquals("0.5", campaigns.get(0).getDiscountedPrice());
		assertEquals(CampaignType.OFFER, campaigns.get(0).getType());
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
