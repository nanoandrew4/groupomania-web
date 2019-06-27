package com.greenapper.filter;

import com.greenapper.controllers.CampaignManagerController;
import com.greenapper.controllers.CampaignManagerProfileController;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.models.User;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that intercepts all calls made after login to check that the user has updated their password and profile,
 * if that data is not existent due to the user logging in for the first time, for example. While the necessary data
 * does not exist, all requests to the server will redirect to the appropriate update page.
 */
@Component
@Order(1)
public class PostRegistrationFilter implements Filter {

	@Autowired
	private SessionService sessionService;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final User sessionUser = sessionService.getSessionUser();

		String redirectUri = null;
		if (sessionUser instanceof CampaignManager)
			redirectUri = checkCampaignManagerData((CampaignManager) sessionUser);

		if (redirectUri != null && allowRedirect(((HttpServletRequest) request), redirectUri))
			redirectStrategy.sendRedirect((HttpServletRequest) request, (HttpServletResponse) response, redirectUri);
		else
			chain.doFilter(request, response);
	}

	/**
	 * Determines if the required redirect is applicable to the request. This method exempts static files, and allows
	 * login/logout to be performed, thus bypassing the filter. Also prevents infinite redirections by checking
	 * if the suggested URL redirect is the same as the requested URL.
	 *
	 * @param request     The current HTTP request
	 * @param redirectUri The suggested redirect URI, given the state of the currently logged in user.
	 * @return True if the redirect should be performed, false if the current request should be allowed to go through
	 */
	private boolean allowRedirect(final HttpServletRequest request, final String redirectUri) {
		final String requestUri = request.getRequestURI();
		return "GET".equals(request.getMethod()) &&
			   (!(requestUri.equals(redirectUri) || requestUri.equals("/login") || requestUri.equals("/logout")
				  || requestUri.contains("img") || requestUri.contains("css")));
	}

	/**
	 * Checks the {@link CampaignManager} in session to determine if the user is completely set up, or some information
	 * is required, in which case, a URI to the page where the missing info can be filled in is returned.
	 *
	 * @param sessionCampaignManager The {@link CampaignManager} currently in session
	 * @return Redirect URI to complete missing information, or null if all necessary information is present
	 */
	private String checkCampaignManagerData(final CampaignManager sessionCampaignManager) {
		if (sessionCampaignManager.isPasswordChangeRequired())
			return CampaignManagerController.PASSWORD_UPDATE_URI;

		final CampaignManagerProfile sessionProfile = sessionCampaignManager.getCampaignManagerProfile();
		if (sessionProfile == null || sessionProfile.getName() == null || sessionProfile.getEmail() == null)
			return CampaignManagerProfileController.PROFILE_UPDATE_URI;
		return null;
	}
}
