package campaigns;

import com.greenapper.Main;
import com.greenapper.controllers.CampaignController;
import com.greenapper.enums.CampaignType;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.User;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.support.BindingAwareModelMap;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Main.class
)
public class CampaignIntegrationTest {

	@Autowired
	private CampaignController campaignController;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Before
	public void setup() {
		final User user = campaignManagerService.getByUsername("admin").orElse(null);
		assertNotNull(user);
		sessionService.setSessionUser(user);
	}

	@Test
	public void getCampaignCreationFormWithEmptyTypeParam() {
		final Model model = new BindingAwareModelMap();
		final String ret = campaignController.getCampaignUpdateForm(model, "");

		assertEquals(CampaignController.CAMPAIGN_CREATION_DEFAULT_REDIRECTION, ret);
	}

	@Test
	public void getCampaignCreationFormForOfferType() {
		final Model model = new BindingAwareModelMap();
		final String ret = campaignController.getCampaignUpdateForm(model, "Offer");

		assertEquals("campaigns/offerCampaign", ret);
		assertEquals(CampaignType.OFFER, ((CampaignForm) model.asMap().get("campaign")).getType());
	}

	@Test
	public void getCampaignCreationFormForCouponType() {
		final Model model = new BindingAwareModelMap();
		final String ret = campaignController.getCampaignUpdateForm(model, "Coupon");

		assertEquals("campaigns/couponCampaign", ret);
		assertEquals(CampaignType.COUPON, ((CampaignForm) model.asMap().get("campaign")).getType());
	}

	@Test
	public void createCampaignWithoutTitle() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setTitle(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.title"), ret);
	}

	@Test
	public void createCampaignWithoutDescription() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setDescription(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.description"), ret);
	}

	@Test
	public void createCampaignWithoutType() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setType(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.type"), ret);
	}

	@Test
	public void createCampaignWithNegativeQuantity() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setQuantity("-1");
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.quantity"), ret);
	}

	@Test
	public void createCampaignWithNullCampaignOriginalPrice() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setOriginalPrice(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.originalPrice"), ret);
	}

	@Test
	public void createCampaignWithNegativeCampaignOriginalPrice() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setOriginalPrice("-1");
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.originalPrice"), ret);
	}

	@Test
	public void createCampaignWithNullStartDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setStartDate(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDate"), ret);
	}

	@Test
	public void createCampaignWithPastStartDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setStartDate(String.valueOf(LocalDate.now().minus(5, ChronoUnit.DAYS)));
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDate"), ret);
	}

	@Test
	public void createCampaignWithNullEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setEndDate(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.endDate"), ret);
	}

	@Test
	public void createCampaignWithPastEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setEndDate(String.valueOf(LocalDate.now().minus(5, ChronoUnit.DAYS)));
		final String ret = campaignController.updateCampaign(campaign, errors);

		final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.endDate", "err.campaign.startDateAfterEndDate");
		performStandardKOAssertions(errors, expectedErrorCodes, ret);
	}

	@Test
	public void createCampaignWithStartDateAfterEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setStartDate(String.valueOf(LocalDate.parse(campaign.getEndDate()).plus(5, ChronoUnit.DAYS)));
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDateAfterEndDate"), ret);
	}

	@Test
	public void createCampaignWithStartDateEqualingEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setEndDate(campaign.getStartDate());
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDateAfterEndDate"), ret);
	}

	@Test
	public void createCampaignWithoutDiscountedPriceAndPercentDiscount() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setDiscountedPrice(null);
		campaign.setPercentDiscount(null);
		final String ret = campaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.discountedPriceOrPercent"), ret);
	}

	@Test
	@DirtiesContext
	public void createValidCampaign() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		final String ret = campaignController.updateCampaign(campaign, errors);

		assertFalse(errors.hasErrors());
		assertEquals(CampaignController.CAMPAIGN_CREATION_SUCCESS_REDIRECT, ret);

		assertFalse(((CampaignManager) sessionService.getSessionUser()).getCampaigns().isEmpty());
	}

	private void performStandardKOAssertions(final Errors errors, final List<String> expectedErrorCodes, final String actualReturnString) {
		assertEquals(CampaignController.getPageForCampaignType("offer"), actualReturnString);
		assertEquals(expectedErrorCodes.size(), errors.getErrorCount());
		for (String errorCode : expectedErrorCodes)
			assertTrue(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).anyMatch(errorCode::equals));
	}

	private CampaignForm getMinimalCampaign() {
		return getMinimalCampaign(new OfferCampaignForm());
	}

	private CampaignForm getMinimalCampaign(final CampaignForm campaign) {
		campaign.setTitle("Title");
		campaign.setDescription("Description");
		campaign.setQuantity("1");
		campaign.setStartDate(String.valueOf(LocalDate.now().plus(1, ChronoUnit.DAYS)));
		campaign.setEndDate(String.valueOf(LocalDate.now().plus(5, ChronoUnit.DAYS)));
		campaign.setOriginalPrice("1");
		campaign.setDiscountedPrice("0.5");

		return campaign;
	}
}
