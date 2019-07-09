package com.greenapper.campaigns;

import com.greenapper.enums.CampaignState;
import com.greenapper.forms.campaigns.CouponCampaignForm;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CouponCampaignIntegrationTest {

	private CouponCampaignForm getMinimalCouponCampaignForm() {
		final CouponCampaignForm form = new CouponCampaignForm();

		form.setTitle("Title");
		form.setDescription("Description");
		form.setQuantity("1");
		form.setStartDate(String.valueOf(LocalDate.now().plus(1, ChronoUnit.DAYS)));
		form.setEndDate(String.valueOf(LocalDate.now().plus(5, ChronoUnit.DAYS)));
		form.setOriginalPrice("1");
		form.setDiscountedPrice("0.5");
		form.setState(CampaignState.INACTIVE);

		form.setCouponStartDate(form.getStartDate());
		form.setCouponEndDate(form.getEndDate());
		form.setCampaignManagerName("TestName");
		form.setCampaignManagerEmail("test@test.cm");
		form.setCampaignManagerAddress("Test address, imaginary country");

		return form;
	}
}
