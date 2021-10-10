package zlavallee.appengine.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.core.admin.NoOpAppEngineApplicationService;

@Configuration
@AutoConfigureAfter(CoreAppEngineConfiguration.class)
@Import(CoreAppEngineConfiguration.class)
@ConditionalOnProperty(name = AppEngineEnvironment.APPLICATION_ID_KEY, matchIfMissing = true, havingValue = "_")
public class LocalAppEngineConfiguration {

  @Bean
  public AppEngineApplicationService appEngineApplicationService() {
    return new NoOpAppEngineApplicationService();
  }
}
