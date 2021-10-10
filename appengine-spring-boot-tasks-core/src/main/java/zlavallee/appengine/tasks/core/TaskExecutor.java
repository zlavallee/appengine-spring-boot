package zlavallee.appengine.tasks.core;

public interface TaskExecutor<T> {

  void execute(T payload);
}
