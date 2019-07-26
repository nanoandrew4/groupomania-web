package com.greenapper.forms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Form used for updating a users password")
public class PasswordUpdateForm {

	@ApiModelProperty(value = "Old password field, which will be verified before the update process begins", required = true)
	private String oldPassword;

	@ApiModelProperty(value = "New password that will replace the old one", required = true)
	private String newPassword;

	@ApiModelProperty(value = "New password confirmation, to minimize risks of typos in the password update process", required = true)
	private String confirmNewPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
}
