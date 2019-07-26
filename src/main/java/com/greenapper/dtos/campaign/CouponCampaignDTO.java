package com.greenapper.dtos.campaign;

import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.models.campaigns.CouponCampaign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "DTO that represents coupon campaigns", parent = CampaignDTO.class)
public class CouponCampaignDTO extends CampaignDTO {

	@ApiModelProperty(value = "Description for the coupon provided as part of the campaign")
	private String couponDescription;

	@ApiModelProperty(value = "Name of the contact for the campaign")
	private String campaignManagerName;

	@ApiModelProperty(value = "Email of the contact for the campaign")
	private String campaignManagerEmail;

	@ApiModelProperty(value = "Address of the contact for the campaign")
	private String campaignManagerAddress;

	@ApiModelProperty(value = "Date the coupon comes into effect", required = true)
	private String couponStartDate;

	@ApiModelProperty(value = "Date the coupon expires", required = true)
	private String couponEndDate;

	public CouponCampaignDTO() {
		this.setType(CampaignType.COUPON);
	}

	/**
	 * Creates a {@link CouponCampaignForm} with the data from the supplied {@link CouponCampaign}.
	 *
	 * @param couponCampaign Campaign model from which to copy the data
	 */
	public CouponCampaignDTO(final CouponCampaign couponCampaign) {
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
