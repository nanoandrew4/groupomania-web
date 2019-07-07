package com.greenapper.validators;

import com.greenapper.config.SecurityConfig;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.User;
import com.greenapper.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * This validator performs validations on the password update form, to ensure that the password update process integrity
 * is not compromised and that the specified password rules are followed.
 */
@Component
public class PasswordUpdateValidator extends GlobalValidator implements Validator {

	@Autowired
	private SecurityConfig securityConfig;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(PasswordUpdateForm.class);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (target == null) {
			errors.reject("err.password");
			return;
		}

		final PasswordUpdateForm passwordUpdateForm = (PasswordUpdateForm) target;
		final User sessionUser = sessionService.getSessionUser();
		final PasswordEncoder pwdEncoder = securityConfig.getPasswordEncoder();

		if (!pwdEncoder.matches(passwordUpdateForm.getOldPassword(), sessionUser.getPassword()))
			rejectWithCodeAndTranslate(messageSource, errors, "err.password.mismatch");
		else if (pwdEncoder.matches(passwordUpdateForm.getNewPassword(), sessionUser.getPassword()))
			rejectWithCodeAndTranslate(messageSource, errors, "err.password.samepassword");
		else if (passwordUpdateForm.getNewPassword().length() < 6)
			rejectWithCodeAndTranslate(messageSource, errors, "err.password.length");
	}
}
