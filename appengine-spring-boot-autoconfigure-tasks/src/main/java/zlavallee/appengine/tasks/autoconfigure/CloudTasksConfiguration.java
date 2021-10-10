package zlavallee.appengine.tasks.autoconfigure;

import com.google.cloud.tasks.v2.CloudTasksClient;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.tasks.core.CloudTasksSubmissionService;
import zlavallee.appengine.tasks.core.DefaultGoogleCloudCreateTask;
import zlavallee.appengine.tasks.core.GoogleCloudCreateTask;
import zlavallee.appengine.tasks.core.QueueConfiguration;
import zlavallee.appengine.tasks.core.TaskSubmissionService;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.gcp.tasks.enableGoogleCloudTasks", matchIfMissing = true)
@ConditionalOnExpression("#{@appEngineEnvironment.applicationId() != null}")
@Import(CloudTasksCoreConfiguration.class)
public class CloudTasksConfiguration {

  private final CloudTasksConfigurationProperties cloudTasksConfigurationProperties;
  private final CloudTasksClient cloudTasksClient;
  private final QueueConfiguration queueConfiguration;

  public CloudTasksConfiguration(
      CloudTasksConfigurationProperties cloudTasksConfigurationProperties,
      CloudTasksClient cloudTasksClient,
      QueueConfiguration queueConfiguration) {
    this.cloudTasksConfigurationProperties = cloudTasksConfigurationProperties;
    this.cloudTasksClient = cloudTasksClient;
    this.queueConfiguration = queueConfiguration;
  }

  @Bean
  public TaskSubmissionService cloudTasksSubmissionService() {
    return new CloudTasksSubmissionService(
        cloudTasksConfigurationProperties.getUriBase(),
        googleCloudCreateTask(),
        queueConfiguration);
  }

  @Bean
  public GoogleCloudCreateTask googleCloudCreateTask() {
    return new DefaultGoogleCloudCreateTask(cloudTasksClient);
  }

  @Bean
  @ConditionalOnMissingBean(CloudTasksClient.class)
  public CloudTasksClient cloudTasksClient() throws IOException {
    return CloudTasksClient.create();
  }
}
