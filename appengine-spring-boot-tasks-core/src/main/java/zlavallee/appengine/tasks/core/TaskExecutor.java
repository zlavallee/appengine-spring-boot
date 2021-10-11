package zlavallee.appengine.tasks.core;

public interface TaskExecutor<T> {

  Class<T> getPayloadClass();

  void execute(T payload);
}
