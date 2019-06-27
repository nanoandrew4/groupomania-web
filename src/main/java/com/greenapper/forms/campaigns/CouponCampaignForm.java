package com.greenapper.forms.campaigns;

import com.greenapper.enums.CampaignType;
import com.greenapper.models.campaigns.CouponCampaign;

/**
 * Form for {@link CouponCampaign} campaigns. Contains all fields necessary to create a {@link CouponCampaign},
 * with numeric and date fields being stored as strings for validation prior to being converted.
 */
public class CouponCampaignForm extends CampaignForm {
	private String couponDescription;

	private String campaignManagerName;

	private String campaignManagerEmail;

	private String campaignManagerAddress;

	private String couponStartDate;

	private String couponEndDate;

	public CouponCampaignForm() {
		this.setType(CampaignType.COUPON);
	}

	/**
	 * Creates a {@link CouponCampaignForm} with the data from the supplied {@link CouponCampaign}.
	 *
	 * @param couponCampaign Campaign model from which to copy the data
	 */
	public CouponCampaignForm(final CouponCampaign couponCampaign) {
		super(couponCampaign);
		this.couponDescription = couponCampaign.getCouponDescription();
		this.campaignManagerName = couponCampaign.getCampaignManagerName();
		this.campaignManagerEmail = couponCampaign.getCampaignManagerEmail();
		this.campaignManagerAddress = couponCampaign.getCampaignManagerAddress();
		this.couponStartDate = String.valueOf(couponCampaign.getCouponStartDate());
		this.couponEndDate = String.valueOf(couponCampaign.getCouponEndDate());
	}

	public String getCouponDescription() {
		return couponDescription;
	}

	public void setCouponDescription(String couponDescription) {
		this.couponDescription = couponDescription;
	}

	public String getCampaignManagerName() {
		return campaignManagerName;
	}

	public void setCampaignManagerName(String campaignManagerName) {
		this.campaignManagerName = campaignManagerName;
	}

	public String getCampaignManagerEmail() {
		return campaignManagerEmail;
	}

	public void setCampaignManagerEmail(String campaignManagerEmail) {
		this.campaignManagerEmail = campaignManagerEmail;
	}

	public String getCampaignManagerAddress() {
		return campaignManagerAddress;
	}

	public void setCampaignManagerAddress(String campaignManagerAddress) {
		this.campaignManagerAddress = campaignManagerAddress;
	}

	public String getCouponStartDate() {
		return couponStartDate;
	}

	public void setCouponStartDate(String couponStartDate) {
		this.couponStartDate = couponStartDate;
	}

	public String getCouponEndDate() {
		return couponEndDate;
	}

	public void setCouponEndDate(String couponEndDate) {
		this.couponEndDate = couponEndDate;
	}
}
