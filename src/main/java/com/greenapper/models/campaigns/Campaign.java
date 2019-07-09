package com.greenapper.models.campaigns;

import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.models.CampaignManager;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Generic model for all campaign types to extend from, contains all the generic fields that should be applicable
 * to all campaign types.
 */
@Entity
@Inheritance(
		strategy = InheritanceType.JOINED
)
public class Campaign {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private CampaignManager owner;

	@NotBlank
	private String title;

	@NotBlank
	private String description;

	private String campaignImageFilePath;

	@NotNull
	private CampaignType type;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate endDate;

	@NotNull
	@Min(1)
	private Double quantity = Double.POSITIVE_INFINITY;

	private boolean showAfterExpiration;

	@NotNull
	private Double originalPrice;

	private Double percentDiscount;

	private Double discountedPrice;

	@NotNull
	private CampaignState state;

	protected Campaign() {}

	public Campaign(final CampaignForm campaignForm) {
		populate(campaignForm);
	}

	public void populate(final CampaignForm campaignForm) {
		Optional.ofNullable(campaignForm.getId()).ifPresent(this::setId);
		Optional.ofNullable(campaignForm.getTitle()).ifPresent(this::setTitle);
		Optional.ofNullable(campaignForm.getDescription()).ifPresent(this::setDescription);
		Optional.ofNullable(campaignForm.getCampaignImageFilePath()).ifPresent(this::setCampaignImageFilePath);
		Optional.ofNullable(campaignForm.getType()).ifPresent(this::setType);
		Optional.ofNullable(campaignForm.getState()).ifPresent(this::setState);
		Optional.ofNullable(campaignForm.getStartDate()).ifPresent(s -> this.setStartDate(LocalDate.parse(s)));
		Optional.ofNullable(campaignForm.getEndDate()).ifPresent(s -> this.setEndDate(LocalDate.parse(s)));
		Optional.ofNullable(campaignForm.getQuantity()).ifPresent(s -> this.setQuantity(parseDouble(s)));
		Optional.of(campaignForm.isShowAfterExpiration()).ifPresent(this::setShowAfterExpiration);
		Optional.ofNullable(campaignForm.getOriginalPrice()).ifPresent(s -> this.setOriginalPrice(parseDouble(s)));
		Optional.ofNullable(campaignForm.getPercentDiscount()).ifPresent(s -> this.setPercentDiscount(parseDouble(s)));
		Optional.ofNullable(campaignForm.getDiscountedPrice()).ifPresent(s -> this.setDiscountedPrice(parseDouble(s)));
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
