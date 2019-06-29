package com.greenapper.models.campaigns;

import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.CampaignManager;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Generic model for all campaign types to extend from, contains all the generic fields that should be applicable
 * to all campaign types.
 */
@Entity
@Inheritance(
		strategy = InheritanceType.JOINED
)
public abstract class Campaign {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private CampaignManager owner;

	private String title;

	private String description;

	private String campaignImageFilePath;

	private CampaignType type;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate endDate;

	private Double quantity = Double.POSITIVE_INFINITY;

	private boolean showAfterExpiration;

	private Double originalPrice;

	private Double percentDiscount;

	private Double discountedPrice;

	private CampaignState state;

	public Campaign() {}

	public Campaign(final CampaignForm campaignForm) {
		this.id = campaignForm.getId();
		this.title = campaignForm.getTitle();
		this.description = campaignForm.getDescription();
		this.campaignImageFilePath = campaignForm.getCampaignImageFilePath();
		this.type = campaignForm.getType();
		this.state = campaignForm.getState();
		this.startDate = LocalDate.parse(campaignForm.getStartDate());
		this.endDate = LocalDate.parse(campaignForm.getEndDate());
		this.quantity = Double.parseDouble(campaignForm.getQuantity());
		this.showAfterExpiration = campaignForm.isShowAfterExpiration();
		this.originalPrice = Double.parseDouble(campaignForm.getOriginalPrice());
		this.percentDiscount = parseDouble(campaignForm.getPercentDiscount());
		this.discountedPrice = parseDouble(campaignForm.getDiscountedPrice());
	}

	private Double parseDouble(final String str) {
		try {
			return Double.valueOf(str);
		} catch (NumberFormatException | NullPointerException e) {
			return null;
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Campaign))
			return false;
		if (id == null || ((Campaign) o).id == null)
			return false;
		return id.equals(((Campaign) o).id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CampaignManager getOwner() {
		return owner;
	}

	public void setOwner(CampaignManager owner) {
		this.owner = owner;
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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public boolean isShowAfterExpiration() {
		return showAfterExpiration;
	}

	public void setShowAfterExpiration(boolean showAfterExpiration) {
		this.showAfterExpiration = showAfterExpiration;
	}

	public CampaignState getState() {
		return state;
	}

	public void setState(CampaignState state) {
		if (this.state != CampaignState.ARCHIVED)
			this.state = state;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Double getPercentDiscount() {
		return percentDiscount;
	}

	public void setPercentDiscount(Double percentDiscount) {
		this.percentDiscount = percentDiscount;
	}

	public Double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getCampaignImageFilePath() {
		return campaignImageFilePath;
	}

	public void setCampaignImageFilePath(String campaignImageFilePath) {
		this.campaignImageFilePath = campaignImageFilePath;
	}
}
