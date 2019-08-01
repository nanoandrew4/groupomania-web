package com.greenapper.services.impl.campaigns;

import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.services.CampaignManagerProfileService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Service
public class CouponCampaignService extends DefaultCampaignService {

	@Resource
	private Validator couponCampaignFormValidator;

	@Resource
	private CampaignManagerProfileService campaignManagerProfileService;

	@Override
	public void validateCampaign(final CampaignForm campaignForm, final Errors errors) {
		couponCampaignFormValidator.validate(campaignForm, errors);
	}

	@Override
	public void setDefaultsForCampaignSubtype(final CampaignForm campaignSubtype) {
		final CouponCampaignForm couponCampaign = (CouponCampaignForm) campaignSubtype;
		final CampaignManagerProfileDTO sessionProfile = campaignManagerProfileService.getProfileForCurrentUser();

		if (couponCampaign.getCampaignManagerName() == null || couponCampaign.getCampaignManagerName().trim().isEmpty())
			couponCampaign.setCampaignManagerName(sessionProfile.getName());
		if (couponCampaign.getCampaignManagerEmail() == null || couponCampaign.getCampaignManagerEmail().trim().isEmpty())
			couponCampaign.setCampaignManagerEmail(sessionProfile.getEmail());
		if (couponCampaign.getCampaignManagerAddress() == null || couponCampaign.getCampaignManagerAddress().trim().isEmpty())
			couponCampaign.setCampaignManagerAddress(sessionProfile.getAddress());
	}
}
