package zlavallee.appengine.tasks.core;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zlavallee.appengine.tasks.core.exception.TaskDelegationException;

public class TaskDelegator {

  private static final Logger logger = LoggerFactory.getLogger(TaskDelegator.class);

  private final Map<Class<?>, TaskExecutor<?>> taskExecutorMap;

  public TaskDelegator(Set<TaskExecutor<?>> taskExecutors) {
    logger.debug("Registering task executors: {}", taskExecutors);
    taskExecutorMap = taskExecutors.stream()
        .collect(Collectors.toMap(TaskExecutor::getPayloadClass, Function.identity()));
    taskExecutorMap.forEach(
        (key, value) -> logger.trace("Registered executor {} for payload class {}", value, key));
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
