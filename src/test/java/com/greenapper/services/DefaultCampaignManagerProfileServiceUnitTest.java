package com.greenapper.services;

import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.exceptions.NotFoundException;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.repositories.CampaignManagerProfileRepository;
import com.greenapper.services.impl.DefaultCampaignManagerProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCampaignManagerProfileServiceUnitTest {

	@InjectMocks
	private DefaultCampaignManagerProfileService campaignManagerProfileService;

	@Mock
	private SessionService sessionService;

	@Mock
	private CampaignManagerProfileRepository campaignManagerProfileRepository;

	@Before
	public void setup() {
		doAnswer(invocation -> {
			final CampaignManager campaignManager = new CampaignManager();
			campaignManager.setId(101L);
			campaignManager.setUsername("testuser");
			campaignManager.setPassword("password");

			final CampaignManagerProfile profile = new CampaignManagerProfile();
			profile.setName("testName");
			profile.setEmail("test@test.com");
			profile.setAddress("This is a test address, imaginary city, imaginary country, postalcode");
			campaignManager.setCampaignManagerProfile(profile);
			return campaignManager;
		}).when(sessionService).getSessionUser();
	}

	@Test
	public void getProfileForCurrentUser_profileDoesNotExist_throwNotFoundException() {
		when(campaignManagerProfileRepository.findById(anyLong())).thenReturn(Optional.empty());

		try {
			campaignManagerProfileService.getProfileForCurrentUser();
			fail("Profile retrieval for user with no profile should cause NotFoundException");
		} catch (NotFoundException ignored) {
		}
	}

	@Test
	public void getProfileForCurrentUser_profileExists_returnProfile() {
		final CampaignManagerProfile profile = ((CampaignManager) sessionService.getSessionUser()).getCampaignManagerProfile();
		when(campaignManagerProfileRepository.findById(anyLong())).thenReturn(Optional.ofNullable(profile));

		final CampaignManagerProfileDTO profileDTO = campaignManagerProfileService.getProfileForCurrentUser();

		assertNotNull(profileDTO);
		assertEquals("testName", profileDTO.getName());
		assertEquals("test@test.com", profileDTO.getEmail());
		assertEquals("This is a test address, imaginary city, imaginary country, postalcode", profileDTO.getAddress());
	}
}
