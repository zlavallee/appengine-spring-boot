package zlavallee.appengine.core;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.api.services.appengine.v1.model.Application;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import zlavallee.appengine.core.admin.AppEngineAdmin;

class AppEngineApplicationServiceImplTest {

  private AppEngineApplicationServiceImpl appEngineApplicationService;
  private AppEngineAdmin appEngineAdmin;

  @BeforeEach
  public void setup() {
    this.appEngineAdmin = mock(AppEngineAdmin.class);
    AppEngineEnvironment appEngineEnvironment = new AppEngineEnvironment(
        new MockEnvironment().withProperty(
            AppEngineEnvironment.APPLICATION_ID_KEY, "applicationId"
        )
    );
    this.appEngineApplicationService = new AppEngineApplicationServiceImpl(
        appEngineEnvironment, appEngineAdmin);
  }

  @Test
  public void testWithCache() {
    doReturn(new Application()).when(appEngineAdmin).getApplication("applicationId");

    Optional<Application> application = appEngineApplicationService.getCurrentApplication();
    assertTrue(application.isPresent());
    application = appEngineApplicationService.getCurrentApplication();
    assertTrue(application.isPresent());

    verify(appEngineAdmin, times(1)).getApplication("applicationId");
  }

  @Test
  public void testNoCache() {
    appEngineApplicationService.setCacheResponse(false);

    doReturn(new Application()).when(appEngineAdmin).getApplication("applicationId");

    Optional<Application> application = appEngineApplicationService.getCurrentApplication();
    assertTrue(application.isPresent());
    application = appEngineApplicationService.getCurrentApplication();
    assertTrue(application.isPresent());

    verify(appEngineAdmin, times(2)).getApplication("applicationId");
  }
}