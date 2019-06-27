package com.greenapper.services.impl.security;

import com.greenapper.models.Authority;
import com.greenapper.models.User;
import com.greenapper.services.CampaignManagerService;
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

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = campaignManagerService.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: \'" + username + "\' does not exist"));

		final List<GrantedAuthority> userGrants = new LinkedList<>();
		for (Authority authority : user.getAuthorities())
			userGrants.add(new SimpleGrantedAuthority(authority.getAuthority()));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), userGrants);
	}
}
