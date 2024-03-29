package com.greenapper.models.campaigns;

import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Model for coupon type campaigns, which extends from the generic {@link Campaign} model.
 */
@Entity
@Table(name = "CouponCampaign")
public class CouponCampaign extends Campaign {

	private String couponDescription;

	private String campaignManagerName;

	private String campaignManagerEmail;

	private String campaignManagerAddress;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate couponStartDate;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate couponEndDate;

	public CouponCampaign() {
		this.setType(CampaignType.COUPON);
	}

	public CouponCampaign(final CouponCampaignForm couponCampaignForm) {
		populate(couponCampaignForm);
	}

	public void populate(final CouponCampaignForm couponCampaignForm) {
		super.populate(couponCampaignForm);
		Optional.ofNullable(couponCampaignForm.getCouponDescription()).ifPresent(this::setCouponDescription);
		Optional.ofNullable(couponCampaignForm.getCampaignManagerName()).ifPresent(this::setCampaignManagerName);
		Optional.ofNullable(couponCampaignForm.getCampaignManagerEmail()).ifPresent(this::setCampaignManagerEmail);
		Optional.ofNullable(couponCampaignForm.getCampaignManagerAddress()).ifPresent(this::setCampaignManagerAddress);
		Optional.ofNullable(couponCampaignForm.getCouponStartDate()).ifPresent(s -> this.setCouponStartDate(LocalDate.parse(s)));
		Optional.ofNullable(couponCampaignForm.getCouponEndDate()).ifPresent(s -> this.setCouponEndDate(LocalDate.parse(s)));
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

	public LocalDate getCouponStartDate() {
		return couponStartDate;
	}

	public void setCouponStartDate(LocalDate couponStartDate) {
		this.couponStartDate = couponStartDate;
	}

	public LocalDate getCouponEndDate() {
		return couponEndDate;
	}

	public void setCouponEndDate(LocalDate couponEndDate) {
		this.couponEndDate = couponEndDate;
	}
}
