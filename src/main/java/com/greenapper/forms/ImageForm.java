package com.greenapper.forms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Form containing image data for upload and storage")
public class ImageForm {

	@ApiModelProperty(value = "File name of the image", required = true)
	private String name;

	@ApiModelProperty(value = "Type of the image", required = true)
	private String type;

	@ApiModelProperty(value = "Image size in bytes", required = true)
	private Long size;

	@ApiModelProperty(value = "Byte array representing the image", required = true)
	private byte[] bytes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
