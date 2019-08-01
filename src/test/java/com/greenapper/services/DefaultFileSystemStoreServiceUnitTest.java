package com.greenapper.services;

import com.greenapper.forms.ImageForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.services.impl.DefaultFileSystemStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class DefaultFileSystemStoreServiceUnitTest {

	@InjectMocks
	private DefaultFileSystemStorageService fileSystemStorageService = new DefaultFileSystemStorageService();

	@Mock
	private SessionService sessionService;

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
	}

	@Test
	public void generateFileNameForStorage_checkForInvalidCharacters_noInvalidCharactersFound() {
		final long seed = System.currentTimeMillis();
		final Random random = new Random(seed);
		for (int i = 0; i < 1000; i++) {
			final ImageForm imageForm = new ImageForm();
			final byte[] bytes = new byte[1024];
			random.nextBytes(bytes);
			imageForm.setBytes(bytes);

			final String generatedFileName = fileSystemStorageService.generateFileNameForStorage(sessionService.getSessionUser().getUsername(), imageForm, "png");

			if (generatedFileName.contains(" "))
				fail("Generated file name with seed: " + seed + " contains a space, which is illegal");
			if (generatedFileName.contains("+"))
				fail("Generated file name with seed: " + seed + " contains a plus symbol, which is illegal");
			if (generatedFileName.split("\\.").length != 2)
				fail("Generated file name with seed: " + seed + " contains a period, which is illegal");
		}
	}
}
