package com.greenapper.campaigns;

import com.greenapper.Application;
import com.greenapper.config.MessageBrokerConfig;
import com.greenapper.controllers.campaign.DefaultCampaignController;
import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.exceptions.UnknownIdentifierException;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Application.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
@WithUserDetails(value = "admin")
public class DefaultCampaignIntegrationTest {

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private DefaultCampaignController defaultCampaignController;

	@Autowired
	private MessageBrokerConfig messageBrokerConfig;

	@Before
	public void setup() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);

		assertNotNull(campaignManager);

		sessionService.setSessionUser(campaignManager);
	}

	@Test
	public void getVisibleCampaignById() {
		try {
			final CampaignDTO visibleCampaign = defaultCampaignController.getCampaignById(1L);
			assertNotNull(visibleCampaign);
		} catch (Exception e) {
			fail("No exception should have been throw : " + e.getMessage());
		}
	}

	@Test
	public void getInvisibleCampaignByIdAsOwner() {
		try {
			final CampaignDTO visibleCampaign = defaultCampaignController.getCampaignById(3L);
			assertNotNull(visibleCampaign);
		} catch (Exception e) {
			fail("No exception should have been throw : " + e.getMessage());
		}
	}

	@Test
	@DirtiesContext
	@WithAnonymousUser
	public void getInvisibleCampaignByIdAsAnonymous() {
		sessionService.setSessionUser(new CampaignManager());
		try {
			defaultCampaignController.getCampaignById(3L);
			fail("Exception should have been thrown due to invisible campaign");
		} catch (Exception e) {
			assertTrue(e instanceof UnknownIdentifierException);
		}
	}

	@Test
	public void getAllVisibleCampaigns() {
		final List<CampaignDTO> visibleCampaigns = defaultCampaignController.getAllVisibleCampaigns();

		assertEquals(2, visibleCampaigns.size());
	}

	@Test
	public void updateCampaignStatusAsOwner() {
		final List<Campaign> userCampaigns = ((CampaignManager) sessionService.getSessionUser()).getCampaigns();
		final Predicate<Campaign> filterById = campaign -> 1L == campaign.getId();
		final CampaignState initState = userCampaigns.stream().filter(filterById).findFirst().map(Campaign::getState).orElse(null);

		defaultCampaignController.updateCampaignState(1L, "archived");

		messageBrokerConfig.sleepWhileOperationsPending(messageBrokerConfig.campaignStateQueue());
		sessionService.setSessionUser(campaignManagerService.getByUsername("admin").orElse(null));

		final CampaignState finalState = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().stream().filter(filterById).findFirst().map(Campaign::getState).orElse(null);
		assertNotEquals(initState, finalState);
		assertEquals(CampaignState.ACTIVE, initState);
		assertEquals(CampaignState.ARCHIVED, finalState);
	}

	@Test
	public void updateCampaignStatusAsOwnerAndInvalidId() {
		try {
			defaultCampaignController.updateCampaignState(11L, "active");
			fail("Exception should have been thrown due to invalid campaign ID");
		} catch (Exception e) {
			assertTrue(e instanceof UnknownIdentifierException);
		}
	}

	@Test
	@DirtiesContext
	public void updateCampaignStatusAsAnonymous() {
		sessionService.setSessionUser(new CampaignManager());
		try {
			defaultCampaignController.updateCampaignState(1L, "active");
			fail("Exception should have been thrown due to invalid campaign ID");
		} catch (Exception e) {
			assertTrue(e instanceof UnknownIdentifierException);
		}
	}
}
