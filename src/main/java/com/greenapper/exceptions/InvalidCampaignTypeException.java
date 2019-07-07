package com.greenapper.exceptions;

import com.greenapper.enums.CampaignType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidCampaignTypeException extends RuntimeException {
	public InvalidCampaignTypeException(final Long id, final CampaignType campaignType) {
		super("Invalid campaign type for campaign with id: " + id + " and type: " + campaignType);
	}
}
