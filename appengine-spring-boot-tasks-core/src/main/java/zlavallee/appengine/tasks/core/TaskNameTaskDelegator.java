package zlavallee.appengine.tasks.core;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zlavallee.appengine.tasks.core.exception.TaskDelegationException;

public class TaskNameTaskDelegator {

  private static final Logger logger = LoggerFactory.getLogger(TaskDelegator.class);

  private final Map<String, TaskExecutor<?>> taskExecutorMap;

  public TaskNameTaskDelegator(Set<TaskExecutor<?>> taskExecutors) {
    logger.debug("Registering task executors: {}", taskExecutors);
    taskExecutorMap = taskExecutors.stream()
        .collect(Collectors.toMap(TaskExecutor::getTaskName, Function.identity()));
    taskExecutorMap.forEach(
        (key, value) -> logger.trace("Registered executor {} for payload class {}", value, key));
  }

  public <T> void delegate(T payload) {
    Class<?> payloadClass = payload.getClass();
    logger.trace("Finding executor for class: {}", payloadClass);
    logger.trace("Payload class hashcode: {}", payloadClass.hashCode());
    logger.trace("Payload class second hashcode: {}", payloadClass.hashCode());

    taskExecutorMap.forEach((aClass, taskExecutor) -> {
      logger.trace("Registered class: {}", aClass);
      logger.trace("Registered payload class hashcode: {}", aClass.hashCode());
      logger.trace("Registered payload class second hashcode: {}", aClass.hashCode());
      logger.trace("Equal: {}", payload.equals(aClass));
    });

    TaskExecutor<T> executor = getTaskExecutor(payload);

    executor.execute(payload);
  }

  @SuppressWarnings("unchecked")
  private <T> TaskExecutor<T> getTaskExecutor(T payload) {
    String taskName = new PayloadMetadata(payload.getClass()).getTaskName();

    if (!taskExecutorMap.containsKey(taskName)) {
      throw new TaskDelegationException("No executor found for type: " + payload.getClass());
    }

    return (TaskExecutor<T>) taskExecutorMap.get(taskName);
  }
}
