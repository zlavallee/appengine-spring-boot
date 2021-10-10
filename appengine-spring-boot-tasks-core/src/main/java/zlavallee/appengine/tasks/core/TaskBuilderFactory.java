package zlavallee.appengine.tasks.core;

public interface TaskBuilderFactory {

  com.google.cloud.tasks.v2.Task.Builder create(Task task);
}
