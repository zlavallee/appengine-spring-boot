package zlavallee.appengine.tasks.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.tasks.core.LocalTaskSubmissionService;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.gcp.tasks.enableGoogleCloudTasks")
@Import(CloudTasksCoreConfiguration.class)
public class LocalTasksConfiguration {

  private final CloudTasksConfigurationProperties cloudTasksConfigurationProperties;

  public LocalTasksConfiguration(
      CloudTasksConfigurationProperties cloudTasksConfigurationProperties) {
    this.cloudTasksConfigurationProperties = cloudTasksConfigurationProperties;
  }

  @Bean
  public LocalTaskSubmissionService localTaskSubmissionService() {
    return new LocalTaskSubmissionService(cloudTasksConfigurationProperties.getUriBase());
  }
}
