package com.greenapper.services;

import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.campaigns.Campaign;
import org.springframework.validation.Errors;

import java.util.List;

/**
 * Service that contains all business logic pertaining to {@link Campaign} and its subclasses.
 */
public interface CampaignService {

	/**
	 * Creates a new {@link Campaign} entity in the database from the supplied {@link CampaignForm}, after validations
	 * have been performed on the supplied form to ensure the integrity of the data.
	 *
	 * @param campaignForm Form from which to create the campaign entity
	 * @param errors       Errors instance into which to write any validation errors that arise
	 */
	void createCampaign(final CampaignForm campaignForm, final Errors errors);

	/**
	 * Modifies an existing {@link Campaign} entity, with the values supplied in the {@link CampaignForm}, after
	 * validations have been performed on the supplied form to ensure the integrity of the data.
	 *
	 * @param campaignForm Form with which to update the existing campaign entity
	 * @param errors       Errors instance into which to write any validation errors that arise
	 */
	void editCampaign(final CampaignForm campaignForm, final Errors errors);

	/**
	 * Updates the state of the campaign matching the supplied ID with the supplied state. Must be a case insensitive
	 * version of {@link CampaignType}.
	 *
	 * @param id    ID of the campaign whose state to update
	 * @param state New state to assign to the campaign, if it exists
	 */
	void updateCampaignState(final Long id, final String state);

	/**
	 * Performs the validation of the supplied {@link CampaignForm}. This method exists so that campaign subtypes can
	 * override the validation and inject their own custom validation.
	 *
	 * @param campaignForm Form containing the data to validate
	 * @param errors       Errors instance into which to write any validation errors that arise
	 */
	void validateCampaign(final CampaignForm campaignForm, final Errors errors);

	/**
	 * This method allows campaign subtypes to set default values for their fields before entering validation and
	 * persisting the entity, such as fields extracted from other entities (like the users name, or email).
	 *
	 * @param campaign Campaign to which to assign the default values deemed necessary by the implementation of this method
	 */
	void setDefaultsForCampaignSubtype(final Campaign campaign);

	/**
	 * Retrieves a {@link Campaign} given its ID, or null if it does not exist.
	 *
	 * @param id ID of the campaign to retrieve
	 * @return The retrieved campaign, or null if it was not found
	 */
	Campaign getCampaignById(final Long id);

	/**
	 * Retrieves a {@link Campaign} by ID, if it is associated to the {@link CampaignManager} currently in session.
	 *
	 * @param id ID of the campaign to retrieve
	 * @return The retrieved campaign, or null if it was not found
	 */
	Campaign getCampaignByIdAndSessionUser(final Long id);

	/**
	 * Retrieves all the {@link Campaign}s for the {@link CampaignManager} in session.
	 *
	 * @return A list of the campaigns associated to the manager in session
	 */
	List<Campaign> getAllCampaignsForCurrentUser();

	/**
	 * Retrieves all stored campaigns.
	 *
	 * @return All stored campaigns
	 */
	List<Campaign> getAllCampaigns();
}
