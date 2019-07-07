package com.greenapper.controllers.campaign;

import com.greenapper.dtos.campaign.CampaignDTO;
import com.greenapper.enums.CampaignState;
import com.greenapper.services.CampaignService;
import com.greenapper.services.impl.campaigns.DefaultCampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/campaigns")
public class DefaultCampaignController {

	@Autowired
	private CampaignService campaignService;

	private Logger LOG = LoggerFactory.getLogger(DefaultCampaignService.class);

	/**
	 * Retrieves a campaign by ID, and sets a flag to indicate to the frontend that the accompanying form should be
	 * immutable.
	 *
	 * @param id Id of the campaign to retrieve
	 * @return Requested campaign, if visible or owned by the current user in session
	 */
	@GetMapping("/{id}")
	public CampaignDTO getCampaignById(@PathVariable final Long id) {
		final Optional<CampaignDTO> campaignDTO = campaignService.getCampaignById(id);

		if (campaignDTO.isPresent() && !isCampaignUnlisted(campaignDTO.get()))
			return campaignDTO.orElse(null);
		return campaignService.getCampaignByIdForSessionUser(id).orElse(null);
	}

	/**
	 * Returns all visible campaigns, that is, active campaigns or those that have been archived, taking into account the
	 * option of keeping it listed up to four days after the end/archival date of the campaign.
	 *
	 * @return Home page
	 */
	@GetMapping
	public List<CampaignDTO> getAllVisibleCampaigns() {
		final List<CampaignDTO> campaigns = campaignService.getAllCampaigns();
		campaigns.removeIf(this::isCampaignUnlisted);

		return campaigns;
	}

	@PatchMapping("/state/{id}/{state}")
	@Secured("ROLE_CAMPAIGN_MANAGER")
	public void updateCampaignState(@PathVariable final Long id, @PathVariable final String state) {
		LOG.info("Updating campaign state for campaign with id: \'" + id + "\'. New state is: \'" + state + "\'");
		campaignService.updateCampaignState(id, state);
	}

	/**
	 * Determines if a campaign should be publicly visible.
	 *
	 * @param campaign Campaign for which to determine if it should be publicly visible or not
	 * @return True if the the campaign should be unlisted, false if the campaign should be publicly visible
	 */
	private boolean isCampaignUnlisted(final CampaignDTO campaign) {
		return campaign.getState() == CampaignState.INACTIVE
			   || (campaign.isShowAfterExpiration() && LocalDate.now().isAfter(LocalDate.parse(campaign.getEndDate()).plus(4, ChronoUnit.DAYS)))
			   || (!campaign.isShowAfterExpiration() && LocalDate.now().isAfter(LocalDate.parse(campaign.getEndDate())));

	}
}
