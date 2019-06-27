package campaign_manager;

import com.greenapper.Main;
import com.greenapper.controllers.CampaignManagerController;
import com.greenapper.forms.PasswordUpdateForm;
import com.greenapper.models.CampaignManager;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Main.class
)
public class CampaignManagerIntegrationTest {
	@Autowired
	private CampaignManagerController campaignManagerController;

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private SessionService sessionService;

	@Before
	public void setup() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);

		assertNotNull(campaignManager);

		sessionService.setSessionUser(campaignManager);
	}

	@Test
	public void updatePasswordOldPasswordInvalid() {
		final PasswordUpdateForm passwordUpdateForm = new PasswordUpdateForm();
		final Errors errors = new BeanPropertyBindingResult(passwordUpdateForm, "passwordUpdateForm");
		passwordUpdateForm.setOldPassword("wrongpass");
		passwordUpdateForm.setNewPassword("12345678");

		final String ret = campaignManagerController.updatePassword(passwordUpdateForm, errors);

		assertEquals(CampaignManagerController.PASSWORD_UPDATE_FORM, ret);
		assertEquals(1, errors.getErrorCount());
		assertEquals("err.password.mismatch", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void updatePasswordLessThan6Chars() {
		final PasswordUpdateForm passwordUpdateForm = new PasswordUpdateForm();
		final Errors errors = new BeanPropertyBindingResult(passwordUpdateForm, "passwordUpdateForm");
		passwordUpdateForm.setOldPassword("testing");
		passwordUpdateForm.setNewPassword("12345");

		final String ret = campaignManagerController.updatePassword(passwordUpdateForm, errors);

		assertEquals(CampaignManagerController.PASSWORD_UPDATE_FORM, ret);
		assertEquals(1, errors.getErrorCount());
		assertEquals("err.password.length", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void updatePasswordSamePassword() {
		final PasswordUpdateForm passwordUpdateForm = new PasswordUpdateForm();
		final Errors errors = new BeanPropertyBindingResult(passwordUpdateForm, "passwordUpdateForm");
		passwordUpdateForm.setOldPassword("testing");
		passwordUpdateForm.setNewPassword("testing");

		final String ret = campaignManagerController.updatePassword(passwordUpdateForm, errors);

		assertEquals(CampaignManagerController.PASSWORD_UPDATE_FORM, ret);
		assertEquals(1, errors.getErrorCount());
		assertEquals("err.password.samepassword", errors.getAllErrors().get(0).getCode());
	}

	@Test
	@DirtiesContext
	public void updatePasswordSuccessfully() {
		final PasswordUpdateForm passwordUpdateForm = new PasswordUpdateForm();
		final Errors errors = new BeanPropertyBindingResult(passwordUpdateForm, "passwordUpdateForm");
		passwordUpdateForm.setOldPassword("testing");
		passwordUpdateForm.setNewPassword("newpassword");

		final String ret = campaignManagerController.updatePassword(passwordUpdateForm, errors);

		assertEquals(CampaignManagerController.PASSWORD_UPDATE_SUCCESS_REDIRECT, ret);
		assertFalse(errors.hasErrors());
	}
}
