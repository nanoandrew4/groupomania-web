package com.greenapper.exceptions;

public class NoSuchCampaignException extends RuntimeException {
	public NoSuchCampaignException() {
		super("Campaign is null");
	}

	public NoSuchCampaignException(final Long id) {
		super("No campaign was found with id: " + id);
	}
}
