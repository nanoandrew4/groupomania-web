package com.greenapper.campaigns;

import com.greenapper.Application;
import com.greenapper.controllers.campaign.CouponCampaignController;
import com.greenapper.enums.CampaignState;
import com.greenapper.exceptions.ValidationException;
import com.greenapper.factories.campaign.CampaignFormFactory;
import com.greenapper.forms.campaigns.CouponCampaignForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.User;
import com.greenapper.models.campaigns.Campaign;
import com.greenapper.models.campaigns.CouponCampaign;
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
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Application.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CouponCampaignIntegrationTest {

	@Autowired
	private CouponCampaignController couponCampaignController;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private CampaignFormFactory campaignFormFactory;

	@Before
	public void setup() {
		final User user = campaignManagerService.getByUsername("admin").orElse(null);
		assertNotNull(user);
		sessionService.setSessionUser(user);
	}

	@Test
	public void createCampaignWithoutTitle() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setTitle(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.title"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithoutDescription() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setDescription(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.description"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithoutType() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setType(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.type"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithNegativeQuantity() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setQuantity("-1");
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.quantity"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithNullCampaignOriginalPrice() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setOriginalPrice(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.originalPrice"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithNegativeCampaignOriginalPrice() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setOriginalPrice("-1");
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.originalPrice", "err.campaign.discountedPriceLargerThanOriginal");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithNullStartDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setStartDate(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDate"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithPastStartDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setStartDate(String.valueOf(LocalDate.now().minus(5, ChronoUnit.DAYS)));
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.startDate"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithNullEndDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setEndDate(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			performStandardKOAssertions(errors, Collections.singletonList("err.campaign.endDate"), initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithPastEndDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setEndDate(String.valueOf(LocalDate.now().minus(5, ChronoUnit.DAYS)));
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.endDate", "err.campaign.startDateAfterEndDate", "err.campaign.coupon.couponEndDateBeforeCampaign");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithStartDateAfterEndDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setStartDate(String.valueOf(LocalDate.parse(campaignForm.getEndDate()).plus(5, ChronoUnit.DAYS)));
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.startDateAfterEndDate", "err.campaign.coupon.couponStartDateBeforeCampaign");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithStartDateEqualingEndDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setEndDate(campaignForm.getStartDate());
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.startDateAfterEndDate", "err.campaign.coupon.couponEndDateBeforeCampaign");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithCouponStartDateBeforeCampaignStartDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setCouponStartDate(LocalDate.parse(campaignForm.getStartDate()).minus(1, ChronoUnit.DAYS).toString());
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.coupon.couponStartDateBeforeCampaign");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithCouponEndDateAfterCampaignEndDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setCouponEndDate(LocalDate.parse(campaignForm.getEndDate()).plus(1, ChronoUnit.DAYS).toString());
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.coupon.couponEndDateBeforeCampaign");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithCouponStartDateAfterCampaignEndDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setCouponStartDate(LocalDate.parse(campaignForm.getEndDate()).plus(1, ChronoUnit.DAYS).toString());
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.coupon.startDateAfterEndDate");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithCouponEndDateBeforeCampaignStartDate() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setCouponEndDate(LocalDate.parse(campaignForm.getStartDate()).minus(1, ChronoUnit.DAYS).toString());
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.coupon.startDateAfterEndDate");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	public void createCampaignWithoutDiscountedPriceAndPercentDiscountAndCouponDescription() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setDiscountedPrice(null);
		campaignForm.setPercentDiscount(null);
		campaignForm.setCouponDescription(null);
		try {
			couponCampaignController.createCampaign(campaignForm, errors);
			fail("ValidationException should have been thrown");
		} catch (ValidationException e) {
			final List<String> expectedErrorCodes = Lists.newArrayList("err.campaign.coupon.discountedPriceOrPercentOrDescription");
			performStandardKOAssertions(errors, expectedErrorCodes, initCampaignCount);
		}
	}

	@Test
	@DirtiesContext
	public void createValidCampaign() {
		final long initCampaignCount = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();

		final CouponCampaignForm campaignForm = getMinimalCouponCampaignForm();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		couponCampaignController.createCampaign(campaignForm, errors);

		assertFalse(errors.hasErrors());
		assertFalse(((CampaignManager) sessionService.getSessionUser()).getCampaigns().isEmpty());
		assertEquals(initCampaignCount + 1, ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size());
	}

	@Test
	public void updateCampaign() {
		final CouponCampaignForm campaignForm = getCampaignFormToUpdate();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setTitle("Updated campaign title");
		campaignForm.setDescription("Updated campaign description");
		campaignForm.setOriginalPrice("12345");
		campaignForm.setStartDate(LocalDate.now().toString());
		campaignForm.setEndDate(LocalDate.now().plus(1, ChronoUnit.DAYS).toString());
		campaignForm.setQuantity("5");
		campaignForm.setState(CampaignState.ARCHIVED);
		campaignForm.setShowAfterExpiration(true);
		campaignForm.setCouponStartDate(campaignForm.getStartDate());
		campaignForm.setCouponEndDate(campaignForm.getEndDate());

		couponCampaignController.updateCampaign(campaignForm, errors);

		final CouponCampaignForm updatedForm = getCampaignFormToUpdate();

		assertFalse(errors.hasErrors());
		assertEquals("Updated campaign title", updatedForm.getTitle());
		assertEquals("Updated campaign description", updatedForm.getDescription());
		assertEquals("12345.0", updatedForm.getOriginalPrice());
		assertEquals("5.0", updatedForm.getQuantity());
		assertEquals(CampaignState.ARCHIVED, campaignForm.getState());
		assertEquals(LocalDate.now().toString(), campaignForm.getStartDate());
		assertEquals(LocalDate.now().plus(1, ChronoUnit.DAYS).toString(), campaignForm.getEndDate());
		assertTrue(campaignForm.isShowAfterExpiration());
	}

	@Test
	public void updateCampaignInvalidOriginalPriceAndStartDate() {
		final long expectedCampaigns = ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size();
		final CouponCampaignForm campaignForm = getCampaignFormToUpdate();
		final Errors errors = new BeanPropertyBindingResult(campaignForm, "campaignForm");

		campaignForm.setOriginalPrice("newprice");
		campaignForm.setStartDate(LocalDate.now().minus(1, ChronoUnit.DAYS).toString());

		try {
			couponCampaignController.updateCampaign(campaignForm, errors);
			fail("Exception should have been thrown due to invalid price and start date");
		} catch (ValidationException e) {
			final List<String> expectedErrors = Lists.newArrayList("err.campaign.originalPrice", "err.campaign.startDate");
			performStandardKOAssertions(errors, expectedErrors, expectedCampaigns);
		}
	}

	private void performStandardKOAssertions(final Errors errors, final List<String> expectedErrorCodes, final long initUserCampaignCount) {
		assertEquals(initUserCampaignCount, ((CampaignManager) sessionService.getSessionUser()).getCampaigns().size());
		assertEquals(expectedErrorCodes.size(), errors.getErrorCount());
		for (String errorCode : expectedErrorCodes) {
			boolean errorCodeMatches = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).anyMatch(errorCode::equals);
			if (!errorCodeMatches)
				System.err.println("\n\n" + errorCode + " not found in: " + errors.getAllErrors());
			assertTrue(errorCodeMatches);
		}
	}

	private CouponCampaignForm getCampaignFormToUpdate() {
		final CampaignManager sessionManager = (CampaignManager) sessionService.getSessionUser();
		final Predicate<Campaign> filterByType = campaign -> campaign instanceof CouponCampaign;
		final CouponCampaign campaignToTest = (CouponCampaign) sessionManager.getCampaigns().stream().filter(filterByType)
				.min(Comparator.comparing(Campaign::getId)).orElse(null);

		if (campaignToTest == null)
			fail("No coupon campaigns available for update");

		final CouponCampaignForm campaignForm = (CouponCampaignForm) campaignFormFactory.createCampaignForm(campaignToTest).orElse(null);

		if (campaignForm == null)
			fail("Campaign to update was not found");
		return campaignForm;
	}

	private CouponCampaignForm getMinimalCouponCampaignForm() {
		final CouponCampaignForm form = new CouponCampaignForm();

		form.setTitle("Title");
		form.setDescription("Description");
		form.setQuantity("1");
		form.setStartDate(String.valueOf(LocalDate.now().plus(1, ChronoUnit.DAYS)));
		form.setEndDate(String.valueOf(LocalDate.now().plus(5, ChronoUnit.DAYS)));
		form.setOriginalPrice("1");
		form.setDiscountedPrice("0.5");
		form.setState(CampaignState.INACTIVE);

		form.setCouponStartDate(form.getStartDate());
		form.setCouponEndDate(form.getEndDate());
		form.setCampaignManagerName("TestName");
		form.setCampaignManagerEmail("test@test.cm");
		form.setCampaignManagerAddress("Test address, imaginary country");

		return form;
	}
}
