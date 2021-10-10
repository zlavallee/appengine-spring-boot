package zlavallee.appengine.core;

import com.google.api.services.appengine.v1.model.Application;
import java.util.Optional;

public interface AppEngineApplicationService {

  Optional<Application> getCurrentApplication();
}
