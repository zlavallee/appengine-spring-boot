package zlavallee.appengine.tasks.core;

import static zlavallee.appengine.tasks.core.CloudTasksUtils.getCloudTasksLocation;

import com.google.api.services.appengine.v1.model.Application;
import com.google.cloud.tasks.v2.QueueName;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.tasks.core.exception.TaskConfigurationException;

public class DefaultQueueConfiguration implements QueueConfiguration {

  private static final String DEFAULT_QUEUE_NAME = "default";

  private String defaultProjectId;
  private String defaultLocationId;
  private Map<String, CloudTaskQueueConfiguration> cloudTaskQueueConfigurationMap = new HashMap<>();
  private final AppEngineApplicationService appEngineApplicationService;
  private final AppEngineEnvironment appEngineEnvironment;

  public DefaultQueueConfiguration(
      AppEngineApplicationService appEngineApplicationService,
      AppEngineEnvironment appEngineEnvironment) {
    this.appEngineApplicationService = appEngineApplicationService;
    this.appEngineEnvironment = appEngineEnvironment;
  }

  @Override
  public QueueName getQueueName(String taskName) {
    QueueName.Builder builder = QueueName.newBuilder()
        .setProject(appEngineEnvironment.projectId())
        .setLocation(defaultLocationId)
        .setQueue(DEFAULT_QUEUE_NAME);

    ifNotNull(defaultProjectId, builder::setProject);

    getConfiguration(taskName).ifPresent(cloudTaskQueueConfiguration -> {
      ifNotNull(cloudTaskQueueConfiguration.getProjectId(), builder::setProject);
      ifNotNull(cloudTaskQueueConfiguration.getLocationId(), builder::setLocation);
      ifNotNull(cloudTaskQueueConfiguration.getQueueName(), builder::setQueue);
    });

    if (builder.getLocation() == null) {
      builder.setLocation(getDetectedLocationId());
    }

    return builder.build();
  }

  private String getDetectedLocationId() {
    Application application = appEngineApplicationService.getCurrentApplication()
        .orElseThrow(() -> new TaskConfigurationException("No location has been set."));

    return getCloudTasksLocation(application.getLocationId());
  }

  private Optional<CloudTaskQueueConfiguration> getConfiguration(String taskName) {
    return Optional.ofNullable(cloudTaskQueueConfigurationMap.get(taskName));
  }

  public void setDefaultProjectId(String defaultProjectId) {
    this.defaultProjectId = defaultProjectId;
  }

  public void setDefaultLocationId(String defaultLocationId) {
    this.defaultLocationId = defaultLocationId;
  }

  public void setCloudTaskQueueConfigurationMap(
      Map<String, CloudTaskQueueConfiguration> cloudTaskQueueConfigurationMap) {
    if (cloudTaskQueueConfigurationMap == null) {
      return;
    }

    this.cloudTaskQueueConfigurationMap = cloudTaskQueueConfigurationMap;
  }

  private void ifNotNull(String property, Consumer<String> consumer) {
    if (property != null) {
      consumer.accept(property);
    }
  }
}
