package com.greenapper.services.impl.security;

import com.greenapper.models.Authority;
import com.greenapper.models.User;
import com.greenapper.services.CampaignManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

	@Autowired
	private CampaignManagerService campaignManagerService;

	private Logger LOG = LoggerFactory.getLogger(DefaultUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = campaignManagerService.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: \'" + username + "\' does not exist"));

		final List<GrantedAuthority> userGrants = new LinkedList<>();
		if (user.getAuthorities() != null) {
			for (Authority authority : user.getAuthorities())
				userGrants.add(new SimpleGrantedAuthority(authority.getAuthority()));
		} else {
			LOG.error("User with username: " + username + " has no authorities associated");
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), userGrants);
	}

	public void setCampaignManagerService(CampaignManagerService campaignManagerService) {
		this.campaignManagerService = campaignManagerService;
	}
}
