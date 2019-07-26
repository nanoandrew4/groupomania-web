package com.greenapper.dtos.campaign;

import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.models.campaigns.Campaign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a campaign object, this DTO is extended from so that the campaign subclasses can implemented their own DTO",
		  subTypes = {OfferCampaignDTO.class, CouponCampaignDTO.class})
public class CampaignDTO {
	@ApiModelProperty(value = "Campaign identifier", notes = "Is unique across all campaigns", required = true)
	private Long id;

	@ApiModelProperty(value = "Campaign title", required = true)
	private String title;

	@ApiModelProperty(value = "Campaign description", required = true)
	private String description;

	@ApiModelProperty(value = "Path to use when calling the /images endpoint to retrieve the image associated to this campaign")
	private String campaignImageFilePath;

	@ApiModelProperty(value = "The type of campaign", required = true)
	private CampaignType type;

	@ApiModelProperty(value = "The campaign state", required = true)
	private CampaignState state;

	@ApiModelProperty(value = "The date that the campaign begins", required = true)
	private String startDate;

	@ApiModelProperty(value = "The date that the campaign ends", required = true)
	private String endDate;

	@ApiModelProperty(value = "The quantity of discounts available", required = true)
	private String quantity = String.valueOf(Double.POSITIVE_INFINITY);

	@ApiModelProperty(value = "Whether the campaign should be shown up to 4 days after it ends or not", required = true)
	private boolean showAfterExpiration;

	@ApiModelProperty(value = "Original price for the item or service the campaign is providing a discount or offer for", required = true)
	private String originalPrice;

	@ApiModelProperty(value = "Percent discount over the original price")
	private String percentDiscount;

	@ApiModelProperty(value = "Discounted price for the item or service the campaign is providing the discount for")
	private String discountedPrice;

	protected CampaignDTO() {
	}

	public CampaignDTO(final Campaign campaign) {
		this.id = campaign.getId();
		this.title = campaign.getTitle();
		this.description = campaign.getDescription();
		this.campaignImageFilePath = campaign.getCampaignImageFilePath();
		this.type = campaign.getType();
		this.state = campaign.getState();
		this.startDate = String.valueOf(campaign.getStartDate());
		this.endDate = String.valueOf(campaign.getEndDate());
		this.quantity = String.valueOf(campaign.getQuantity());
		this.showAfterExpiration = campaign.isShowAfterExpiration();
		this.originalPrice = String.valueOf(campaign.getOriginalPrice());
		this.percentDiscount = String.valueOf(campaign.getPercentDiscount()).replace("null", "");
		this.discountedPrice = String.valueOf(campaign.getDiscountedPrice()).replace("null", "");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CampaignType getType() {
		return type;
	}

	public void setType(CampaignType type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public boolean isShowAfterExpiration() {
		return showAfterExpiration;
	}

	public void setShowAfterExpiration(boolean showAfterExpiration) {
		this.showAfterExpiration = showAfterExpiration;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPercentDiscount() {
		return percentDiscount;
	}

	public void setPercentDiscount(String percentDiscount) {
		this.percentDiscount = percentDiscount;
	}

	public String getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(String discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getCampaignImageFilePath() {
		return campaignImageFilePath;
	}

	public void setCampaignImageFilePath(String campaignImageFilePath) {
		this.campaignImageFilePath = campaignImageFilePath;
	}

	public CampaignState getState() {
		return state;
	}

	public void setState(CampaignState state) {
		this.state = state;
	}
}
