package zlavallee.appengine.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zlavallee.appengine.core.AppEngineEnvironment;

@Configuration
public class CoreAppEngineConfiguration {
  private static final Logger logger = LoggerFactory.getLogger(CoreAppEngineConfiguration.class);

  @Bean
  public AppEngineEnvironment appEngineEnvironment() {
    logger.debug("Creating app engine environment bean.");
    return new AppEngineEnvironment();
  }
}
