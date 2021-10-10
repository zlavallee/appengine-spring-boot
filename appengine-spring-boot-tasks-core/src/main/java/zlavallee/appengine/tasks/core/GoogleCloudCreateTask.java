package zlavallee.appengine.tasks.core;

import com.google.cloud.tasks.v2.Task;

public interface GoogleCloudCreateTask {

  Task create(String path, Task task);
}
