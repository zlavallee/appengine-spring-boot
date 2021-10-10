package zlavallee.appengine.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.core.AppEngineSettings;
import zlavallee.appengine.core.AppEngineSettingsImpl;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter({
    LocalAppEngineConfiguration.class,
    CloudAppEngineConfiguration.class,
})
public class AppEngineAutoConfigure {

  @Autowired
  private AppEngineEnvironment appEngineEnvironment;
  @Autowired
  private AppEngineApplicationService appEngineApplicationService;

  @Bean
  public AppEngineSettings appEngineSettings() {
    return new AppEngineSettingsImpl(appEngineEnvironment, appEngineApplicationService);
  }
}
