package com.greenapper.services.impl;

import com.greenapper.models.User;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class DefaultSessionService implements SessionService {

	private User sessionUser;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Override
	public void setSessionUser() {
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
			campaignManagerService.getByUsername(username).ifPresent(this::setSessionUser);
		}
	}

	@Override
	public User getSessionUser() {
		return sessionUser;
	}

	@Override
	public void setSessionUser(final User user) {
		sessionUser = user;
	}
}
