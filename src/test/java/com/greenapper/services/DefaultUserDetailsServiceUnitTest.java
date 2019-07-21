package com.greenapper.services;

import com.greenapper.models.CampaignManager;
import com.greenapper.services.impl.security.DefaultUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserDetailsServiceUnitTest {

	@InjectMocks
	private UserDetailsService userDetailsService = new DefaultUserDetailsService();

	@Mock
	private CampaignManagerService campaignManagerService;

	@Test
	public void loadUserByUsername_nonExistentUsername_throwUsernameNotFoundException() {
		when(campaignManagerService.getByUsername(anyString())).thenReturn(Optional.empty());

		try {
			userDetailsService.loadUserByUsername("unknown");
			fail("Exception should have been thrown due to username not found");
		} catch (UsernameNotFoundException ignored) {
		}
	}

	@Test
	public void loadUserByUsername_existentUsername_returnSuccessfully() {
		final CampaignManager user = new CampaignManager();
		user.setId(1L);
		user.setUsername("testuser");
		user.setPassword("password");
		user.setAuthorities(new HashSet<>());

		when(campaignManagerService.getByUsername(anyString())).thenReturn(Optional.of(user));

		final UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

		assertNotNull(userDetails);
		assertEquals("testuser", userDetails.getUsername());
		assertEquals("password", userDetails.getPassword());
	}
}
