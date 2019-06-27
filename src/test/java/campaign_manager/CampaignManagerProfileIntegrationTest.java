package campaign_manager;

import com.greenapper.Main;
import com.greenapper.controllers.CampaignManagerProfileController;
import com.greenapper.models.CampaignManager;
import com.greenapper.models.CampaignManagerProfile;
import com.greenapper.services.CampaignManagerService;
import com.greenapper.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Main.class
)
public class CampaignManagerProfileIntegrationTest {

	@Autowired
	private CampaignManagerService campaignManagerService;

	@Autowired
	private CampaignManagerProfileController campaignManagerProfileController;

	@Autowired
	private SessionService sessionService;

	@Before
	public void setup() {
		final CampaignManager campaignManager = campaignManagerService.getByUsername("admin").orElse(null);
		assertNotNull(campaignManager);

		final CampaignManagerProfile campaignManagerProfile = new CampaignManagerProfile();
		campaignManagerProfile.setCampaignManager(campaignManager);
		campaignManagerProfile.setName("Name");
		campaignManagerProfile.setEmail("test@email.com");
		campaignManager.setCampaignManagerProfile(campaignManagerProfile);

		sessionService.setSessionUser(campaignManager);
	}

	@Test
	public void updateProfileEmptyName() {
		final CampaignManagerProfile sessionProfile = ((CampaignManager) sessionService.getSessionUser()).getCampaignManagerProfile();
		final Errors errors = new BeanPropertyBindingResult(sessionProfile, "sessionProfile");
		assertNotNull(sessionProfile);

		sessionProfile.setName(null);
		final String ret = campaignManagerProfileController.updateProfile(sessionProfile, errors);

		assertEquals(CampaignManagerProfileController.PROFILE_UPDATE_FORM, ret);
		assertEquals(1, errors.getErrorCount());
		assertEquals("err.profile.name", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void updateProfileEmptyEmail() {
		final CampaignManagerProfile sessionProfile = ((CampaignManager) sessionService.getSessionUser()).getCampaignManagerProfile();
		final Errors errors = new BeanPropertyBindingResult(sessionProfile, "sessionProfile");
		assertNotNull(sessionProfile);

		sessionProfile.setEmail(null);
		final String ret = campaignManagerProfileController.updateProfile(sessionProfile, errors);

		assertEquals(CampaignManagerProfileController.PROFILE_UPDATE_FORM, ret);
		assertEquals(1, errors.getErrorCount());
		assertEquals("err.profile.email", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void updateProfileInvalidEmail() {
		final CampaignManagerProfile sessionProfile = ((CampaignManager) sessionService.getSessionUser()).getCampaignManagerProfile();
		final Errors errors = new BeanPropertyBindingResult(sessionProfile, "sessionProfile");
		assertNotNull(sessionProfile);

		sessionProfile.setEmail("invalidemail");
		final String ret = campaignManagerProfileController.updateProfile(sessionProfile, errors);

		assertEquals(CampaignManagerProfileController.PROFILE_UPDATE_FORM, ret);
		assertEquals(1, errors.getErrorCount());
		assertEquals("err.profile.email", errors.getAllErrors().get(0).getCode());
	}

	@Test
	public void updateProfileSuccessfully() {
		final CampaignManagerProfile sessionProfile = ((CampaignManager) sessionService.getSessionUser()).getCampaignManagerProfile();
		final Errors errors = new BeanPropertyBindingResult(sessionProfile, "sessionProfile");
		assertNotNull(sessionProfile);

		final String ret = campaignManagerProfileController.updateProfile(sessionProfile, errors);

		assertEquals(CampaignManagerProfileController.PROFILE_UPDATE_SUCCESS, ret);
		assertFalse(errors.hasErrors());
	}
}
