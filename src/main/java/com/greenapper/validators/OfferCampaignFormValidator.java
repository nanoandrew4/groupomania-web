package com.greenapper.validators;

import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.models.campaigns.OfferCampaign;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

/**
 * This validator contains validation logic specific to the {@link OfferCampaign}, and uses {@link CampaignFormValidator}
 * to perform validation on the generic campaign fields.
 */
@Component
public class OfferCampaignFormValidator implements Validator {

	@Resource
	private Validator campaignFormValidator;

	@Override
	public boolean supports(Class<?> clazz) {
		return OfferCampaign.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final OfferCampaignForm offerCampaignForm = (OfferCampaignForm) target;

		campaignFormValidator.validate(offerCampaignForm, errors);

		if (CampaignFormValidator.parseDouble(offerCampaignForm.getDiscountedPrice()) == null && CampaignFormValidator.parseDouble(offerCampaignForm.getPercentDiscount()) == null)
			errors.reject("err.campaign.offer.discountedPriceOrPercent");
	}
}
