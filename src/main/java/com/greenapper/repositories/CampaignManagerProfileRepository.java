package com.greenapper.repositories;

import com.greenapper.models.CampaignManagerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing {@link CampaignManagerProfile} entities in the DB.
 */
public interface CampaignManagerProfileRepository extends JpaRepository<CampaignManagerProfile, Long> {
}
