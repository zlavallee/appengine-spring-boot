package zlavallee.appengine.core.admin;

import com.google.api.services.appengine.v1.model.Application;

public interface AppEngineAdmin {

  Application getApplication(String applicationId);
}
