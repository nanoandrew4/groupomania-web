package com.greenapper.validators;

import com.greenapper.config.SecurityConfig;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.User;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This validator performs validations on the password update form, to ensure that the password update process integrity
 * is not compromised and that the specified password rules are followed.
 */
@Component
public class PasswordUpdateValidator implements Validator {

	@Autowired
	private SecurityConfig securityConfig;

	@Autowired
	private SessionService sessionService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(CampaignManager.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target == null) {
			errors.reject("err.password");
			return;
		}

		final PasswordUpdateForm passwordUpdateForm = (PasswordUpdateForm) target;
		final User sessionUser = sessionService.getSessionUser();
		final PasswordEncoder pwdEncoder = securityConfig.getPasswordEncoder();

		if (pwdEncoder.matches(passwordUpdateForm.getNewPassword(), sessionUser.getPassword()))
			errors.reject("err.password.samepassword");
		else if (passwordUpdateForm.getNewPassword().length() < 6)
			errors.reject("err.password.length");
		else if (!pwdEncoder.matches(passwordUpdateForm.getOldPassword(), sessionUser.getPassword()))
			errors.reject("err.password.mismatch");
	}
}
