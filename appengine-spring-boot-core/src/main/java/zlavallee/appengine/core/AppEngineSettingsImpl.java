package zlavallee.appengine.core;

import com.google.api.services.appengine.v1.model.Application;

public class AppEngineSettingsImpl implements AppEngineSettings {

  private final AppEngineEnvironment appEngineEnvironment;
  private final AppEngineApplicationService appEngineApplicationService;

  public AppEngineSettingsImpl(
      AppEngineEnvironment appEngineEnvironment,
      AppEngineApplicationService appEngineApplicationService) {
    this.appEngineEnvironment = appEngineEnvironment;
    this.appEngineApplicationService = appEngineApplicationService;
  }

  @Override
  public String getProjectId() {
    return appEngineEnvironment.projectId();
  }

  @Override
  public String getApplicationId() {
    return appEngineEnvironment.applicationId();
  }

  @Override
  public String getLocationId() {
    return appEngineApplicationService.getCurrentApplication()
        .map(Application::getLocationId)
        .orElse(null);
  }

  @Override
  public boolean isAppEngine() {
    return getApplicationId() != null;
  }
}
