package com.greenapper.services;

import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import org.springframework.validation.Errors;

import java.util.Optional;

/**
 * Service that contains all the business logic for {@link CampaignManagerProfile} entities.
 */
public interface CampaignManagerProfileService {
	/**
	 * Updates the profile for the {@link CampaignManager} in session with the supplied {@link CampaignManagerProfile},
	 * after validating that the fields contain valid data.
	 *
	 * @param updatedProfile Profile containing the desired data
	 * @param errors         Errors instance into which to write any validations errors that appear during validation
	 */
	void updateProfile(final CampaignManagerProfile updatedProfile, final Errors errors);

	/**
	 * Retrieves the {@link CampaignManagerProfile} for the {@link CampaignManager} currently in session, wrapped in an
	 * Optional.
	 *
	 * @return The profile for the manager in session, wrapped in an optional
	 */
	Optional<CampaignManagerProfile> getProfileForCurrentUser();
}
