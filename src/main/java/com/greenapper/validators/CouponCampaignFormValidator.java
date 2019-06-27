package com.greenapper.validators;

import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.models.campaigns.CouponCampaign;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

/**
 * This validator contains validation logic specific to the {@link CouponCampaign}, and uses {@link CampaignFormValidator}
 * to perform validation on the generic campaign fields.
 */
@Component
public class CouponCampaignFormValidator implements Validator {

	@Resource
	private Validator campaignFormValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return CouponCampaign.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		campaignFormValidator.validate(target, errors);

		final CouponCampaignForm couponCampaignForm = (CouponCampaignForm) target;
		CampaignFormValidator.rejectStringIfPresentAndTooLong(couponCampaignForm.getCouponDescription(), "err.campaign.coupon.description", errors);
		CampaignFormValidator.rejectStringIfPresentAndTooLong(couponCampaignForm.getCampaignManagerName(), "err.campaign.coupon.managerName", errors);
		CampaignFormValidator.rejectStringIfPresentAndTooLong(couponCampaignForm.getCampaignManagerEmail(), "err.campaign.coupon.managerEmail", errors);
		CampaignFormValidator.rejectStringIfPresentAndTooLong(couponCampaignForm.getCampaignManagerAddress(), "err.campaign.coupon.managerAddress", errors);
		CampaignFormValidator.rejectDateIfEmptyOrBeforeNow(couponCampaignForm.getCouponStartDate(), "err.campaign.coupon.couponStartDate", errors);
		CampaignFormValidator.rejectDateIfEmptyOrBeforeNow(couponCampaignForm.getCouponEndDate(), "err.campaign.coupon.couponEndDate", errors);

		CampaignFormValidator.rejectDateIfEqualOrAfterOtherDate(couponCampaignForm.getCouponStartDate(), couponCampaignForm.getCouponEndDate(), "err.campaign.coupon.startDateAfterEndDate", errors);
		CampaignFormValidator.rejectDateIfBeforeOtherDate(couponCampaignForm.getCouponStartDate(), couponCampaignForm.getStartDate(), "err.campaign.coupon.couponStartDateBeforeCampaign", errors);
		CampaignFormValidator.rejectDateIfAfterOtherDate(couponCampaignForm.getCouponEndDate(), couponCampaignForm.getEndDate(), "err.campaign.coupon.couponEndDateBeforeCampaign", errors);

		if (couponCampaignForm.getDiscountedPrice() == null && couponCampaignForm.getPercentDiscount() == null && (couponCampaignForm.getCouponDescription() == null || couponCampaignForm.getCouponDescription().trim().isEmpty()))
			errors.reject("err.campaign.coupon.discountedPriceOrPercentOrDescription");
	}
}
