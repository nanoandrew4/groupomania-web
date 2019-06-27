package com.greenapper.repositories;

import com.greenapper.models.CampaignManager;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing {@link CampaignManager} entities in the DB.
 */
public interface CampaignManagerRepository extends JpaRepository<CampaignManager, Long> {
	CampaignManager findByUsername(final String username);

	boolean findPasswordChangeRequiredById(final Long userId);
}
