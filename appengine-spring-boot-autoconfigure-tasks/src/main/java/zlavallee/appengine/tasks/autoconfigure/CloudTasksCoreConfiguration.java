package zlavallee.appengine.tasks.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.tasks.core.DefaultQueueConfiguration;
import zlavallee.appengine.tasks.core.QueueConfiguration;

@Configuration
@EnableConfigurationProperties(CloudTasksConfigurationProperties.class)
public class CloudTasksCoreConfiguration {

  @Autowired
  private AppEngineApplicationService appEngineApplicationService;
  @Autowired
  private CloudTasksConfigurationProperties cloudTasksConfigurationProperties;
  @Autowired
  private AppEngineEnvironment appEngineEnvironment;

  @Bean
  public QueueConfiguration queueConfiguration() {
    DefaultQueueConfiguration defaultQueueConfiguration = new DefaultQueueConfiguration(
        appEngineApplicationService, appEngineEnvironment);

    defaultQueueConfiguration.setDefaultProjectId(
        cloudTasksConfigurationProperties.getProjectId());
    defaultQueueConfiguration.setDefaultLocationId(
        cloudTasksConfigurationProperties.getLocationId());
    defaultQueueConfiguration.setCloudTaskQueueConfigurationMap(
        cloudTasksConfigurationProperties.getQueues());

    return defaultQueueConfiguration;
  }

}
