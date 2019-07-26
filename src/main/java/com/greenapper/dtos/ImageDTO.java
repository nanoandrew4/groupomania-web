package com.greenapper.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO used as a wrapper for a byte array that represents an image")
public class ImageDTO {

	@ApiModelProperty(value = "Byte array representing an image", required = true)
	private byte[] image;

	public ImageDTO(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
