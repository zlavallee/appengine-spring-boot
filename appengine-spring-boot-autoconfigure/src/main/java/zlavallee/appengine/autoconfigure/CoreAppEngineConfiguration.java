package zlavallee.appengine.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zlavallee.appengine.core.AppEngineEnvironment;

@Configuration
public class CoreAppEngineConfiguration {

  @Bean
  public AppEngineEnvironment appEngineEnvironment() {
    return new AppEngineEnvironment();
  }
}
