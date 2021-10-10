package zlavallee.appengine.core.admin;

import static zlavallee.appengine.core.admin.GoogleClientRequestExecutors.defaultExecutor;

import com.google.api.services.appengine.v1.Appengine;
import com.google.api.services.appengine.v1.model.Application;

public class ApplicationAdminImpl implements AppEngineAdmin {

  private final Appengine appengine;

  public ApplicationAdminImpl(Appengine appengine) {
    this.appengine = appengine;
  }

  @Override
  public Application getApplication(String applicationId) {
    return defaultExecutor().execute(
        () -> appengine.apps().get(applicationId));

  }
}
