package zlavallee.appengine.core.admin;

import com.google.api.services.appengine.v1.model.Application;
import java.util.Optional;
import zlavallee.appengine.core.AppEngineApplicationService;

public class NoOpAppEngineApplicationService implements AppEngineApplicationService {

  public static AppEngineApplicationService instanceOf() {
    return new NoOpAppEngineApplicationService();
  }

  public NoOpAppEngineApplicationService() {
  }

  @Override
  public Optional<Application> getCurrentApplication() {
    return Optional.empty();
  }
}
