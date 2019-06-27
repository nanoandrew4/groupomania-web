package com.greenapper.filter;

import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * Intercepts all requests and sets the authenticated user in session so that it can be retrieved from memory
 * throughout the call by use of the {@link SessionService}, without having to always go to the DB to fetch it.
 */
@Component
@Order(0)
public class SessionFilter implements Filter {
	@Autowired
	private SessionService sessionService;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (sessionService.getSessionUser() == null)
			sessionService.setSessionUser();

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
