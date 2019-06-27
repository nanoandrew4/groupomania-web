package com.greenapper.services;

import com.greenapper.models.User;

/**
 * Service that handles user sessions.
 */
public interface SessionService {

	/**
	 * Sets the user in session, by fetching the username from the Spring Security session and then retrieving it
	 * by username so it can be stored in memory.
	 */
	void setSessionUser();

	/**
	 * Returns the user in session.
	 *
	 * @return The user currently in session, or null if there is none
	 */
	User getSessionUser();

	/**
	 * Sets the user in session to the supplied user.
	 *
	 * @param user User to set in session
	 */
	void setSessionUser(final User user);
}
