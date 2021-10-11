package zlavallee.appengine.tasks.core;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import zlavallee.appengine.tasks.core.exception.TaskDelegationException;

public class TaskDelegator {

  private final Map<Class<?>, TaskExecutor<?>> taskExecutorMap;

  public TaskDelegator(Set<TaskExecutor<?>> taskExecutors) {
    taskExecutorMap = taskExecutors.stream()
        .collect(Collectors.toMap(TaskExecutor::getPayloadClass, Function.identity()));
  }

  public <T> void delegate(T payload) {
    TaskExecutor<T> executor = getTaskExecutor(payload);

    executor.execute(payload);
  }

  @SuppressWarnings("unchecked")
  private <T> TaskExecutor<T> getTaskExecutor(T payload) {
    if (!taskExecutorMap.containsKey(payload.getClass())) {
      throw new TaskDelegationException("No executor found for type: " + payload.getClass());
    }

    return (TaskExecutor<T>) taskExecutorMap.get(payload.getClass());
  }
}
