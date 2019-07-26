package com.greenapper.services;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

/**
 * Service that contains all the business logic for {@link CampaignManager} entities.
 */
public interface CampaignManagerService {

	/**
	 * Attempts to fetch a {@link CampaignManager} from the database by username.
	 *
	 * @param username Username to fetch a campaign manager by
	 * @return The campaign manager with the given username, or {@link Optional#empty()}
	 */
	Optional<CampaignManager> getByUsername(final String username);

	/**
	 * Adds to or updates a campaign in the campaign list for the {@link CampaignManager} currently in session.
	 *
	 * @param campaign Campaign to add if it doesn't exist, or update if it is already present in the list of campaigns
	 *                 associated to the session campaign manager
	 */
	void addOrUpdateCampaignForCampaignManager(final Campaign campaign);

	/**
	 * Attempts to update the password for the {@link CampaignManager} currently in session.
	 *
	 * @param passwordUpdateForm Password update form to be used for the password update process
	 * @param errors Errors instance to which to write validation errors, if any were to arise
	 */
	void updatePassword(final PasswordUpdateForm passwordUpdateForm, final Errors errors);

	/**
	 * Retrieves a list of campaigns for the {@link CampaignManager} currently in session.
	 *
	 * @return List of campaign associated to the session campaign manager
	 */
	List<CampaignDTO> getCampaigns();

	/**
	 * Returns whether the password of the campaign manager currently in session needs changing or not
	 */
	boolean isCurrentUserPasswordChangeRequired();
}