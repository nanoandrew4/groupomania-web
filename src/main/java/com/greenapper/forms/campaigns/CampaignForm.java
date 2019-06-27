package com.greenapper.forms.campaigns;

import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.models.campaigns.Campaign;
import org.springframework.web.multipart.MultipartFile;

/**
 * Generic campaign form to be sent from the frontend to the backend. Numeric and date fields are stored as strings,
 * so that validations can be performed in the backend.
 */
public abstract class CampaignForm {
	private Long id;

	private String title;

	private String description;

	private MultipartFile campaignImage;

	private String campaignImageFilePath;

	private CampaignType type;

	private CampaignState state;

	private String startDate;

	private String endDate;

	private String quantity = String.valueOf(Double.POSITIVE_INFINITY);

	private boolean showAfterExpiration;

	private String originalPrice;

	private String percentDiscount;

	private String discountedPrice;

	public CampaignForm() {
	}

	public CampaignForm(final Campaign campaign) {
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

	public MultipartFile getCampaignImage() {
		return campaignImage;
	}

	public void setCampaignImage(MultipartFile campaignImage) {
		this.campaignImage = campaignImage;
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
