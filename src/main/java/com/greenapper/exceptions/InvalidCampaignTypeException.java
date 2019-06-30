package com.greenapper.exceptions;

import com.greenapper.enums.CampaignType;

public class InvalidCampaignTypeException extends RuntimeException {
	public InvalidCampaignTypeException(final Long id, final CampaignType campaignType) {
		super("Invalid campaign type for campaign with id: " + id + " and type: " + campaignType);
	}
}
