package com.greenapper.dtos;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Exception thrown by OAuth. This model exists only for documentation purposes")
public class OAuthExceptionDTO {
	public String error, error_description;
}
