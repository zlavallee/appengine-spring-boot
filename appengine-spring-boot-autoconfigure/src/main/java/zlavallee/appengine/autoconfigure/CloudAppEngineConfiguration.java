package zlavallee.appengine.autoconfigure;

import com.google.api.services.appengine.v1.Appengine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineApplicationServiceImpl;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.core.admin.AppEngineAdmin;
import zlavallee.appengine.core.admin.ApplicationAdminImpl;

@Configuration
@Import(CoreAppEngineConfiguration.class)
@ConditionalOnProperty(name = AppEngineEnvironment.APPLICATION_ID_KEY)
public class CloudAppEngineConfiguration {

  @Autowired
  private AppEngineEnvironment appEngineEnvironment;
  @Autowired
  private Appengine appengine;

  @Bean
  public AppEngineApplicationService appEngineApplicationService() {
    return new AppEngineApplicationServiceImpl(appEngineEnvironment, appEngineAdmin());
  }

  @Bean
  public AppEngineAdmin appEngineAdmin() {
    return new ApplicationAdminImpl(appengine);
  }
}
