package com.greenapper.validators;

import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.campaigns.Campaign;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * This validator contains validation logic applicable to all {@link Campaign} subtypes. All campaign subclass validators
 * should first call the {{@link #validate(Object, Errors)}} method in this class in order to perform validation
 * on the generic campaign fields, and then carry out validation on the campaign subclass fields in a custom validator
 * designed for the specific campaign subclass.
 */
@Component
public class CampaignFormValidator implements Validator {

	private static final Long MAX_STRING_LENGTH = 255L;

	@Override
	public boolean supports(Class<?> clazz) {
		return CampaignForm.class.equals(clazz);
	}

	public static void rejectIfNull(final Object value, final String errorCode, final Errors errors) {
		if (value == null)
			errors.reject(errorCode);
	}

	public static Double parseDouble(final String doubleStr) {
		try {
			return Double.valueOf(doubleStr);
		} catch (NumberFormatException | NullPointerException e) {
			return null;
		}
	}

	public static void rejectIfNumberPresentAndInvalid(final String valueStr, final String errorCode, final Errors errors) {
		final Double value = parseDouble(valueStr);
		if (valueStr != null && valueStr.trim().length() > 0 && value == null)
			errors.reject(errorCode);
	}

	public static void rejectIfNumberNullOrNegative(final String valueStr, final String errorCode, final Errors errors) {
		final Double value = parseDouble(valueStr);
		if (value == null || value < 0)
			errors.reject(errorCode);
	}

	public static void rejectStringIfPresentAndTooLong(final String value, final String errorCode, final Errors errors) {
		if (value != null && value.length() > MAX_STRING_LENGTH)
			errors.reject(errorCode);
	}

	public static void rejectStringIfEmptyOrTooLong(final String value, final String errorCode, final Errors errors) {
		if (value == null || value.trim().isEmpty() || value.length() > MAX_STRING_LENGTH)
			errors.reject(errorCode);
	}

	private static LocalDate parseDate(final String date) {
		try {
			return LocalDate.parse(date);
		} catch (DateTimeParseException | NullPointerException e) {
			return null;
		}
	}

	public static void rejectDateIfEmptyOrBeforeNow(final String dateStr, final String errorCode, final Errors errors) {
		final LocalDate date = parseDate(dateStr);
		if (date == null || date.isBefore(LocalDate.now()))
			errors.reject(errorCode);
	}

	public static void rejectDateIfEqualOrBeforeOtherDate(final String dateStr1, final String dateStr2, final String errorCode, final Errors errors) {
		final LocalDate date1 = parseDate(dateStr1);
		final LocalDate date2 = parseDate(dateStr2);
		if (date1 != null && date2 != null && date1.isBefore(date2.plus(1, ChronoUnit.DAYS)))
			errors.reject(errorCode);
	}

	public static void rejectDateIfEqualOrAfterOtherDate(final String dateStr1, final String dateStr2, final String errorCode, final Errors errors) {
		final LocalDate date1 = parseDate(dateStr1);
		final LocalDate date2 = parseDate(dateStr2);
		if (date1 != null && date2 != null && date1.isAfter(date2.minus(1, ChronoUnit.DAYS)))
			errors.reject(errorCode);
	}

	public static void rejectDateIfBeforeOtherDate(final String dateStr1, final String dateStr2, final String errorCode, final Errors errors) {
		final LocalDate date1 = parseDate(dateStr1);
		final LocalDate date2 = parseDate(dateStr2);
		if (date1 != null && date2 != null && date1.isBefore(date2))
			errors.reject(errorCode);
	}

	public static void rejectDateIfAfterOtherDate(final String dateStr1, final String dateStr2, final String errorCode, final Errors errors) {
		final LocalDate date1 = parseDate(dateStr1);
		final LocalDate date2 = parseDate(dateStr2);
		if (date1 != null && date2 != null && date1.isAfter(date2))
			errors.reject(errorCode);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target == null) {
			errors.reject("err.campaign");
			return;
		}

		final CampaignForm campaignForm = (CampaignForm) target;

		rejectStringIfEmptyOrTooLong(campaignForm.getTitle(), "err.campaign.title", errors);
		rejectStringIfEmptyOrTooLong(campaignForm.getDescription(), "err.campaign.description", errors);
		rejectStringIfEmptyOrTooLong(campaignForm.getType().toString(), "err.campaign.type", errors);
		rejectIfNumberNullOrNegative(campaignForm.getQuantity(), "err.campaign.quantity", errors);
		rejectIfNumberNullOrNegative(campaignForm.getOriginalPrice(), "err.campaign.originalPrice", errors);
		rejectIfNumberPresentAndInvalid(campaignForm.getPercentDiscount(), "err.campaign.percentDiscount", errors);
		rejectIfNumberPresentAndInvalid(campaignForm.getDiscountedPrice(), "err.campaign.discountedPrice", errors);
		rejectDateIfEmptyOrBeforeNow(campaignForm.getStartDate(), "err.campaign.startDate", errors);
		rejectDateIfEmptyOrBeforeNow(campaignForm.getEndDate(), "err.campaign.endDate", errors);
		rejectDateIfEqualOrAfterOtherDate(campaignForm.getStartDate(), campaignForm.getEndDate(), "err.campaign.startDateAfterEndDate", errors);

		if (campaignForm.getCampaignImage() != null && campaignForm.getCampaignImage().getSize() > 0 &&
			(campaignForm.getCampaignImage().getContentType() == null || !campaignForm.getCampaignImage().getContentType().contains("image")))
			errors.reject("err.campaign.imageFormat");

		final Double discountedPrice = parseDouble(campaignForm.getDiscountedPrice());
		final Double originalPrice = parseDouble(campaignForm.getOriginalPrice());
		final Double percentDiscount = parseDouble(campaignForm.getPercentDiscount());
		if (discountedPrice != null && originalPrice != null && discountedPrice > originalPrice)
			errors.reject("err.campaign.discountedPriceLargerThanOriginal");

		if (percentDiscount != null && (percentDiscount > 100 || percentDiscount < 0))
			errors.reject("err.campaign.percentDiscountInvalid");
	}
}
