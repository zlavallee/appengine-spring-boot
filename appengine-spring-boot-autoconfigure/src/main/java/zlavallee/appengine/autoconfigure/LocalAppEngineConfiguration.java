package zlavallee.appengine.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.autoconfigure.condition.ConditionalOnMissingAppEngine;
import zlavallee.appengine.autoconfigure.condition.NoAppEngineCondition;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.admin.NoOpAppEngineApplicationService;

@Configuration
@AutoConfigureAfter(CoreAppEngineConfiguration.class)
@Import(CoreAppEngineConfiguration.class)
@ConditionalOnMissingAppEngine
public class LocalAppEngineConfiguration {

  @Bean
  public AppEngineApplicationService appEngineApplicationService() {
    return new NoOpAppEngineApplicationService();
  }
}
