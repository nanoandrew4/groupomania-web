package com.greenapper.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.greenapper.enums.CampaignState;
import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Custom deserializer for {@link CampaignForm} subclasses, since the backend returns {@link CampaignForm} instances,
 * and they can't be deserializer by Jackson by default.
 */
public class CampaignFormDeserializer extends StdDeserializer<CampaignForm> {

	private Logger LOG = LoggerFactory.getLogger(CampaignFormDeserializer.class);

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
		Optional.ofNullable(rootNode.get("title")).map(JsonNode::asText).ifPresent(campaignForm::setTitle);
		Optional.ofNullable(rootNode.get("description")).map(JsonNode::asText).ifPresent(campaignForm::setDescription);
		Optional.ofNullable(rootNode.get("campaignImageFilePath")).map(JsonNode::asText).ifPresent(campaignForm::setCampaignImageFilePath);
		Optional.ofNullable(rootNode.get("state")).map(JsonNode::asText).map(CampaignState::valueOf).ifPresent(campaignForm::setState);
		Optional.ofNullable(rootNode.get("startDate")).map(JsonNode::asText).ifPresent(campaignForm::setStartDate);
		Optional.ofNullable(rootNode.get("endDate")).map(JsonNode::asText).ifPresent(campaignForm::setEndDate);
		Optional.ofNullable(rootNode.get("quantity")).map(JsonNode::asText).ifPresent(campaignForm::setQuantity);
		Optional.ofNullable(rootNode.get("showAfterExpiration")).map(JsonNode::asBoolean).ifPresent(campaignForm::setShowAfterExpiration);
		Optional.ofNullable(rootNode.get("originalPrice")).map(JsonNode::asText).ifPresent(campaignForm::setOriginalPrice);
		Optional.ofNullable(rootNode.get("percentDiscount")).map(JsonNode::asText).ifPresent(campaignForm::setPercentDiscount);
		Optional.ofNullable(rootNode.get("discountedPrice")).map(JsonNode::asText).ifPresent(campaignForm::setDiscountedPrice);
	}

	private void populateCouponCampaign(final CouponCampaignForm campaignDTO, final JsonNode rootNode) {
		Optional.ofNullable(rootNode.get("couponDescription")).map(JsonNode::asText).ifPresent(campaignDTO::setCouponDescription);
		Optional.ofNullable(rootNode.get("campaignManagerName")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignManagerName);
		Optional.ofNullable(rootNode.get("campaignManagerEmail")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignManagerEmail);
		Optional.ofNullable(rootNode.get("campaignManagerAddress")).map(JsonNode::asText).ifPresent(campaignDTO::setCampaignManagerAddress);
		Optional.ofNullable(rootNode.get("couponStartDate")).map(JsonNode::asText).ifPresent(campaignDTO::setCouponStartDate);
		Optional.ofNullable(rootNode.get("couponEndDate")).map(JsonNode::asText).ifPresent(campaignDTO::setCouponEndDate);
	}
}
