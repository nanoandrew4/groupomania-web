package com.greenapper.queues.campaignmanager.password;

import com.greenapper.forms.PasswordUpdateForm;

public class PasswordUpdateOperation {
	private PasswordUpdateForm passwordUpdateForm;

	private String passwordUpdateUser;

	public PasswordUpdateOperation() {

	}

	public PasswordUpdateOperation(final PasswordUpdateForm passwordUpdateForm, final String passwordUpdateUser) {
		this.passwordUpdateForm = passwordUpdateForm;
		this.passwordUpdateUser = passwordUpdateUser;
	}

	public PasswordUpdateForm getPasswordUpdateForm() {
		return passwordUpdateForm;
	}

	public void setPasswordUpdateForm(PasswordUpdateForm passwordUpdateForm) {
		this.passwordUpdateForm = passwordUpdateForm;
	}

	public String getPasswordUpdateUser() {
		return passwordUpdateUser;
	}

	public void setPasswordUpdateUser(String passwordUpdateUser) {
		this.passwordUpdateUser = passwordUpdateUser;
	}
}
