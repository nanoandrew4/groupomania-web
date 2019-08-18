package com.greenapper.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.forms.ImageForm;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.logging.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

/**
 * Custom deserializer for {@link CampaignForm} subclasses, since the backend returns {@link CampaignForm} instances,
 * and they can't be deserializer by Jackson by default.
 */
public class CampaignFormDeserializer extends StdDeserializer<CampaignForm> {

	@Autowired
	private LogManager LOG;

	public CampaignFormDeserializer() {
		super(CampaignForm.class);
	}

	@Override
	public CampaignForm deserialize(final JsonParser p, DeserializationContext ctxt) throws IOException {
		final JsonNode rootNode = p.getCodec().readTree(p);

		final CampaignType type = CampaignType.valueOf(rootNode.get("type").asText());

		final CampaignForm campaignForm;
		switch (type) {
			case OFFER:
				campaignForm = new OfferCampaignForm();
				break;
			case COUPON:
				campaignForm = new CouponCampaignForm();
				populateCouponCampaign((CouponCampaignForm) campaignForm, rootNode);
				break;
			default:
				LOG.error("No handling provided for campaign with type: " + type);
				campaignForm = null;
		}

		if (campaignForm != null)
			commonPopulate(campaignForm, rootNode);

		return campaignForm;
	}

	private void commonPopulate(final CampaignForm campaignForm, final JsonNode rootNode) {
		Optional.ofNullable(rootNode.get("id")).map(JsonNode::asLong).ifPresent(campaignForm::setId);
		Optional.ofNullable(rootNode.get("title").asText(null)).ifPresent(campaignForm::setTitle);
		Optional.ofNullable(rootNode.get("description").asText(null)).ifPresent(campaignForm::setDescription);
		Optional.ofNullable(rootNode.get("campaignImageFilePath").asText(null)).ifPresent(campaignForm::setCampaignImageFilePath);
		Optional.ofNullable(rootNode.get("state").asText(null)).map(CampaignState::valueOf).ifPresent(campaignForm::setState);
		Optional.ofNullable(rootNode.get("startDate").asText(null)).ifPresent(campaignForm::setStartDate);
		Optional.ofNullable(rootNode.get("endDate").asText(null)).ifPresent(campaignForm::setEndDate);
		Optional.ofNullable(rootNode.get("quantity").asText(null)).ifPresent(campaignForm::setQuantity);
		Optional.ofNullable(rootNode.get("showAfterExpiration")).map(JsonNode::asBoolean).ifPresent(campaignForm::setShowAfterExpiration);
		Optional.ofNullable(rootNode.get("originalPrice").asText(null)).ifPresent(campaignForm::setOriginalPrice);
		Optional.ofNullable(rootNode.get("percentDiscount").asText(null)).ifPresent(campaignForm::setPercentDiscount);
		Optional.ofNullable(rootNode.get("discountedPrice").asText(null)).ifPresent(campaignForm::setDiscountedPrice);

		final JsonNode campaignImageNode = rootNode.get("campaignImage");
		if (!campaignImageNode.isNull())
			campaignForm.setCampaignImage(populateImageForm(campaignImageNode));
	}

	private ImageForm populateImageForm(final JsonNode campaignImageNode) {
		final ImageForm imageForm = new ImageForm();
		imageForm.setName(campaignImageNode.get("name").asText());
		imageForm.setType(campaignImageNode.get("type").asText());
		imageForm.setSize(campaignImageNode.get("size").asLong());
		try {
			imageForm.setBytes(campaignImageNode.get("bytes").binaryValue());
			return imageForm;
		} catch (IOException e) {
			LOG.error("Error decoding bytes from ImageForm", e);
			return null;
		}
	}

	private void populateCouponCampaign(final CouponCampaignForm campaignDTO, final JsonNode rootNode) {
		Optional.ofNullable(rootNode.get("couponDescription").asText(null)).ifPresent(campaignDTO::setCouponDescription);
		Optional.ofNullable(rootNode.get("campaignManagerName").asText(null)).ifPresent(campaignDTO::setCampaignManagerName);
		Optional.ofNullable(rootNode.get("campaignManagerEmail").asText(null)).ifPresent(campaignDTO::setCampaignManagerEmail);
		Optional.ofNullable(rootNode.get("campaignManagerAddress").asText(null)).ifPresent(campaignDTO::setCampaignManagerAddress);
		Optional.ofNullable(rootNode.get("couponStartDate").asText(null)).ifPresent(campaignDTO::setCouponStartDate);
		Optional.ofNullable(rootNode.get("couponEndDate").asText(null)).ifPresent(campaignDTO::setCouponEndDate);
	}
}
