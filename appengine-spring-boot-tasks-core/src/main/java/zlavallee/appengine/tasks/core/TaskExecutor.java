package zlavallee.appengine.tasks.core;

public interface TaskExecutor<T> {

  default String getTaskName() {
    return new PayloadMetadata(getPayloadClass()).getTaskName();
  }

  Class<T> getPayloadClass();

  void execute(T payload);
}
