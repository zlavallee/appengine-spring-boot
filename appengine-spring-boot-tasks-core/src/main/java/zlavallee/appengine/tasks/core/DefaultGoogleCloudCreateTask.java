package zlavallee.appengine.tasks.core;

import com.google.cloud.tasks.v2.CloudTasksClient;
import com.google.cloud.tasks.v2.Task;

public class DefaultGoogleCloudCreateTask implements GoogleCloudCreateTask {

  private final CloudTasksClient cloudTasksClient;

  public DefaultGoogleCloudCreateTask(CloudTasksClient cloudTasksClient) {
    this.cloudTasksClient = cloudTasksClient;
  }

  @Override
  public Task create(String path, Task task) {
    return cloudTasksClient.createTask(path, task);
  }
}
