package com.greenapper.validators;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;

public class GlobalValidator {
	protected static void rejectWithCodeAndTranslate(final MessageSource messageSource, final Errors errors, final String code) {
		errors.reject(messageSource.getMessage(code, null, LocaleContextHolder.getLocale()));
	}
}
