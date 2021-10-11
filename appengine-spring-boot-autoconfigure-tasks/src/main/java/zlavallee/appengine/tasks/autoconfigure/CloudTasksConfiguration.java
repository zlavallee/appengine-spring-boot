package zlavallee.appengine.tasks.autoconfigure;

import com.google.cloud.tasks.v2.CloudTasksClient;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.autoconfigure.condition.ConditionalOnAppEngine;
import zlavallee.appengine.tasks.core.CloudTasksSubmissionService;
import zlavallee.appengine.tasks.core.DefaultGoogleCloudCreateTask;
import zlavallee.appengine.tasks.core.QueueConfiguration;
import zlavallee.appengine.tasks.core.TaskSubmissionService;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.gcp.tasks.enableGoogleCloudTasks", matchIfMissing = true)
@ConditionalOnAppEngine
@Import(CloudTasksCoreConfiguration.class)
public class CloudTasksConfiguration {

  private final CloudTasksConfigurationProperties cloudTasksConfigurationProperties;
  private final QueueConfiguration queueConfiguration;

  public CloudTasksConfiguration(
      CloudTasksConfigurationProperties cloudTasksConfigurationProperties,
      QueueConfiguration queueConfiguration) {
    this.cloudTasksConfigurationProperties = cloudTasksConfigurationProperties;
    this.queueConfiguration = queueConfiguration;
  }

  @Bean
  public TaskSubmissionService cloudTasksSubmissionService(
      CloudTasksClient cloudTasksClient) {
    return new CloudTasksSubmissionService(
        cloudTasksConfigurationProperties.getUriBase(),
        new DefaultGoogleCloudCreateTask(cloudTasksClient),
        queueConfiguration);
  }

  @Bean
  @ConditionalOnMissingBean(CloudTasksClient.class)
  public CloudTasksClient cloudTasksClient() throws IOException {
    return CloudTasksClient.create();
  }
}
