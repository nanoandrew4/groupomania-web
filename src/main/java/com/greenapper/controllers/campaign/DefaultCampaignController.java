package com.greenapper.controllers.campaign;

import com.greenapper.dtos.ErrorDTO;
import com.greenapper.dtos.campaigns.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.logging.LogManager;
import com.greenapper.services.CampaignService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/campaigns")
@Api(value = "/campaigns", description = "Generic campaign endpoints, applicable to all campaign types")
public class DefaultCampaignController {

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private LogManager LOG;

	/**
	 * Retrieves a campaign by ID, and sets a flag to indicate to the frontend that the accompanying form should be
	 * immutable.
	 *
	 * @param id Id of the campaign to retrieve
	 * @return Requested campaign, if visible or owned by the current user in session
	 */
	@GetMapping("/{id}")
	@ApiOperation(
			value = "Retrieves a campaign given its ID",
			notes = "The campaign is only returned if it is publicly visible, or if it belongs to the campaign manager currently in session"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Campaign retrieved successfully"),
			@ApiResponse(code = 404, message = "Campaign not found, or not publicly visible", response = ErrorDTO.class)
	})
	public CampaignDTO getCampaignById(
			@PathVariable @ApiParam(value = "ID of the campaign to retrieve") final Long id) {
		final CampaignDTO campaignDTO = campaignService.getCampaignById(id);

		if (!isCampaignUnlisted(campaignDTO))
			return campaignDTO;
		return campaignService.getCampaignByIdForSessionUser(id);
	}

	/**
	 * Returns all visible campaigns, that is, active campaigns or those that have been archived, taking into account the
	 * option of keeping it listed up to four days after the end/archival date of the campaign.
	 *
	 * @return Home page
	 */
	@GetMapping
	@ApiOperation(value = "Retrieves all visible campaigns", notes = "The returned list will be empty if no campaigns are currently visible")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Campaigns retrieved successfully")
	})
	public List<CampaignDTO> getAllVisibleCampaigns() {
		final List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
		campaigns.removeIf(this::isCampaignUnlisted);

		LOG.info("Returning publicly available campaigns");

		return campaigns;
	}

	@Secured("ROLE_CAMPAIGN_MANAGER")
	@PutMapping("/state/{id}/{state}")
	@ApiOperation(value = "Updates the state of an existing campaign to the newly supplied value")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Campaign state updated successfully"),
			@ApiResponse(code = 404, message = "Specified campaign not found", response = ErrorDTO.class)
	})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCampaignState(
			@PathVariable @ApiParam(value = "ID of the campaign whose state to modify") final Long id,
			@PathVariable @ApiParam(value = "New state to associate with the specified campaign", required = true, allowableValues = "active,inactive,archive") final String state) {
		LOG.info("Updating campaign state for campaign with id: \'" + id + "\'. New state is: \'" + state + "\'");
		campaignService.updateCampaignState(id, state);
	}

	/**
	 * Determines if a campaign should be publicly visible.
	 *
	 * @param campaignDTO Campaign for which to determine if it should be publicly visible or not
	 * @return True if the the campaign should be unlisted, false if the campaign should be publicly visible
	 */
	private boolean isCampaignUnlisted(final CampaignDTO campaignDTO) {
		return campaignDTO.getState() == CampaignState.INACTIVE || campaignDTO.getState() == CampaignState.ARCHIVED
			   || (campaignDTO.isShowAfterExpiration() && LocalDate.now().isAfter(LocalDate.parse(campaignDTO.getEndDate()).plus(4, ChronoUnit.DAYS)))
			   || (!campaignDTO.isShowAfterExpiration() && LocalDate.now().isAfter(LocalDate.parse(campaignDTO.getEndDate())));

	}
}
