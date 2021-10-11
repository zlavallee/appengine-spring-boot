package zlavallee.appengine.tasks.core;

public abstract class TaskExecutorSupport<T> implements TaskExecutor<T> {

  private final Class<T> payloadClass;

  public TaskExecutorSupport(Class<T> payloadClass) {
    this.payloadClass = payloadClass;
  }

  @Override
  public Class<T> getPayloadClass() {
    return this.payloadClass;
  }
}
