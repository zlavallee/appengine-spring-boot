package zlavallee.appengine.tasks.autoconfigure;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import zlavallee.appengine.tasks.core.CloudTaskQueueConfiguration;

@ConfigurationProperties("appengine.tasks")
@ConstructorBinding
public class CloudTasksConfigurationProperties {

  private final List<String> payloadBasePackages;
  private final String uriBase;
  private final Boolean enableGoogleCloudTasks;
  private final String projectId;
  private final String locationId;
  private final Map<String, CloudTaskQueueConfiguration> queues;

  public CloudTasksConfigurationProperties(
      List<String> payloadBasePackages,
      String uriBase,
      Boolean enableGoogleCloudTasks,
      String projectId,
      String locationId,
      Map<String, CloudTaskQueueConfiguration> queues) {
    this.payloadBasePackages = payloadBasePackages;
    this.uriBase = uriBase;
    this.enableGoogleCloudTasks = enableGoogleCloudTasks;
    this.projectId = projectId;
    this.locationId = locationId;
    this.queues = queues;
  }

  public String getUriBase() {
    return uriBase;
  }

  public Boolean getEnableGoogleCloudTasks() {
    return enableGoogleCloudTasks;
  }

  public String getProjectId() {
    return projectId;
  }

  public String getLocationId() {
    return locationId;
  }

  public List<String> getPayloadBasePackages() {
    return payloadBasePackages;
  }

  public Map<String, CloudTaskQueueConfiguration> getQueues() {
    return queues;
  }
}
