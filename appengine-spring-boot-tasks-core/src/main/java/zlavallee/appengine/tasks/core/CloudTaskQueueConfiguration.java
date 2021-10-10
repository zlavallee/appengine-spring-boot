package zlavallee.appengine.tasks.core;

public class CloudTaskQueueConfiguration {

  private String projectId;
  private String locationId;
  private String queueName;

  public CloudTaskQueueConfiguration(String projectId, String locationId, String queueName) {
    this.projectId = projectId;
    this.locationId = locationId;
    this.queueName = queueName;
  }

  public CloudTaskQueueConfiguration() {
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }
}
