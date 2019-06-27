package com.greenapper.repositories;

import com.greenapper.models.campaigns.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing {@link Campaign} entities and its subclasses in the DB.
 */
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
