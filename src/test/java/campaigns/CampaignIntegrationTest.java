package campaigns;

import com.greenapper.Main;
import com.greenapper.controllers.campaign.BaseCampaignController;
import com.greenapper.controllers.campaign.CouponCampaignController;
import com.greenapper.controllers.campaign.DefaultCampaignController;
import com.greenapper.controllers.campaign.OfferCampaignController;
import com.greenapper.enums.CampaignState;
import com.greenapper.exceptions.InvalidCampaignTypeException;
import com.greenapper.forms.campaigns.CampaignForm;
import com.greenapper.forms.campaigns.CouponCampaignForm;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

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
@TestPropertySource(locations = "classpath:application-test.properties")
public class CampaignIntegrationTest {

	@Autowired
	private DefaultCampaignController defaultCampaignController;

	@Autowired
	private OfferCampaignController offerCampaignController;

	@Autowired
	private CouponCampaignController couponCampaignController;

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
	public void getCampaignCreationFormForOfferType() {
		final String ret = offerCampaignController.getCampaignUpdateForm(new OfferCampaignForm());

		assertEquals("campaigns/offerCampaign", ret);
	}

	@Test
	public void getCampaignCreationFormForCouponType() {
		final String ret = couponCampaignController.getCampaignUpdateForm(new CouponCampaignForm());

		assertEquals("campaigns/couponCampaign", ret);
	}

	@Test
	public void createCampaignWithoutTitle() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setTitle(null);
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.title"), ret);
	}

	@Test
	public void createCampaignWithoutDescription() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setDescription(null);
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.description"), ret);
	}

	@Test
	public void createCampaignWithoutType() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setType(null);
		try {
			final String ret = defaultCampaignController.updateCampaign(campaign, errors);
			fail("Expected InvalidCampaignTypeException to be thrown");
		} catch (InvalidCampaignTypeException ignored) {
		}
	}

	@Test
	public void createCampaignWithNegativeQuantity() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setQuantity("-1");
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.quantity"), ret);
	}

	@Test
	public void createCampaignWithNullCampaignOriginalPrice() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setOriginalPrice(null);
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.originalPrice"), ret);
	}

	@Test
	public void createCampaignWithNegativeCampaignOriginalPrice() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setOriginalPrice("-1");
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Lists.newArrayList("err.campaign.originalPrice", "err.campaign.discountedPriceLargerThanOriginal"), ret);
	}

	@Test
	public void createCampaignWithNullStartDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setStartDate(null);
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDate"), ret);
	}

	@Test
	public void createCampaignWithPastStartDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setStartDate(String.valueOf(LocalDate.now().minus(5, ChronoUnit.DAYS)));
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDate"), ret);
	}

	@Test
	public void createCampaignWithNullEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setEndDate(null);
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.endDate"), ret);
	}

	@Test
	public void createCampaignWithPastEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setEndDate(String.valueOf(LocalDate.now().minus(5, ChronoUnit.DAYS)));
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.endDate", "err.campaign.startDateAfterEndDate");
		performStandardKOAssertions(errors, expectedErrorCodes, ret);
	}

	@Test
	public void createCampaignWithStartDateAfterEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setStartDate(String.valueOf(LocalDate.parse(campaign.getEndDate()).plus(5, ChronoUnit.DAYS)));
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDateAfterEndDate"), ret);
	}

	@Test
	public void createCampaignWithStartDateEqualingEndDate() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setEndDate(campaign.getStartDate());
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDateAfterEndDate"), ret);
	}

	@Test
	public void createCampaignWithoutDiscountedPriceAndPercentDiscount() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		campaign.setDiscountedPrice(null);
		campaign.setPercentDiscount(null);
		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		performStandardKOAssertions(errors, Collections.singletonList("err.campaign.offer.discountedPriceOrPercent"), ret);
	}

	@Test
	@DirtiesContext
	public void createValidCampaign() {
		final CampaignForm campaign = getMinimalCampaign();
		final Errors errors = new BeanPropertyBindingResult(campaign, "campaign");

		final String ret = defaultCampaignController.updateCampaign(campaign, errors);

		assertFalse(errors.hasErrors());
		assertEquals(BaseCampaignController.CAMPAIGN_CREATION_SUCCESS_REDIRECT, ret);

		assertFalse(((CampaignManager) sessionService.getSessionUser()).getCampaigns().isEmpty());
	}

	private void performStandardKOAssertions(final Errors errors, final List<String> expectedErrorCodes, final String actualReturnString) {
		assertEquals(BaseCampaignController.getPageForCampaignType("offer"), actualReturnString);
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
		campaign.setState(CampaignState.INACTIVE);

		return campaign;
	}
}
