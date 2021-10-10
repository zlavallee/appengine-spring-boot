package zlavallee.appengine.core;

import com.google.api.services.appengine.v1.model.Application;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zlavallee.appengine.core.admin.AppEngineAdmin;

public class AppEngineApplicationServiceImpl implements AppEngineApplicationService {

  private static final Logger logger = LoggerFactory.getLogger(
      AppEngineApplicationServiceImpl.class);

  private final AppEngineAdmin appEngineAdmin;
  private final AppEngineEnvironment appEngineEnvironment;
  private boolean cacheResponse = true;
  private volatile Application application;

  public AppEngineApplicationServiceImpl(
      AppEngineEnvironment appEngineEnvironment,
      AppEngineAdmin appEngineAdmin) {
    this.appEngineAdmin = appEngineAdmin;
    this.appEngineEnvironment = appEngineEnvironment;
  }

  @Override
  public Optional<Application> getCurrentApplication() {
    if (cacheResponse) {
      return Optional.of(getAndCache());
    }

    return Optional.of(getApplicationFromApi());
  }

  private Application getAndCache() {
    if (application == null) {
      synchronized (this) {
        if (application == null) {
          logger.trace("No Application object cached. Getting Application from api.");
          application = getApplicationFromApi();
        }
      }
    }

    return application;
  }

  private Application getApplicationFromApi() {
    return appEngineAdmin.getApplication(appEngineEnvironment.applicationId());
  }

  public void setCacheResponse(boolean cacheResponse) {
    this.cacheResponse = cacheResponse;
  }
}
