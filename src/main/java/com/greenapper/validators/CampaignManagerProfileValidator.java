package com.greenapper.validators;

import com.greenapper.models.CampaignManagerProfile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * This validator contains all the validation logic for {@link CampaignManagerProfile}.
 */
@Component
public class CampaignManagerProfileValidator implements Validator {

	/**
	 * RFC5322 compliant email regex. Copied from: https://emailregex.com/
	 */
	private final Pattern VALID_EMAIL_ADDRESS_REGEX =
			Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");


	@Override
	public boolean supports(Class<?> clazz) {
		return CampaignManagerProfile.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target == null) {
			errors.reject("err.profile");
			return;
		}

		final CampaignManagerProfile updatedProfile = (CampaignManagerProfile) target;

		if (updatedProfile.getName() == null || updatedProfile.getName().trim().isEmpty())
			errors.reject("err.profile.name");
		if (updatedProfile.getEmail() == null || updatedProfile.getEmail().trim().isEmpty() || !VALID_EMAIL_ADDRESS_REGEX.matcher(updatedProfile.getEmail()).find())
			errors.reject("err.profile.email");
		if (updatedProfile.getProfileImage() != null && updatedProfile.getProfileImage().getSize() > 0 && (updatedProfile.getProfileImage().getContentType() == null ||
																										   !updatedProfile.getProfileImage().getContentType().contains("image")))
			errors.reject("err.profile.imageFormat");
	}
}
