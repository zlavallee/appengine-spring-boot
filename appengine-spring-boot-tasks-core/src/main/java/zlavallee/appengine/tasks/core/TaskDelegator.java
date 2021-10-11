package zlavallee.appengine.tasks.core;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import zlavallee.appengine.tasks.core.exception.TaskDelegationException;

public class TaskDelegator implements ApplicationContextAware {

  private ListableBeanFactory beanFactory;
  private final ConcurrentHashMap<Class<?>, TaskExecutor<?>> cachedExecutors = new ConcurrentHashMap<>();

  public TaskDelegator() {
  }

  public <T> void delegate(T payload) {
    TaskExecutor<T> executor = getTaskExecutor(payload);

    executor.execute(payload);
  }

  @SuppressWarnings("unchecked")
  private <T> TaskExecutor<T> getTaskExecutor(T payload) {
    return (TaskExecutor<T>) cachedExecutors.computeIfAbsent(payload.getClass(),
        aClass -> getBeanFromFactory(payload));
  }

  private <T> TaskExecutor<T> getBeanFromFactory(T payload) {
    return getBean(getBeanName(payload));
  }

  private <T> String getBeanName(T payload) {
    String[] beanNames = beanFactory.getBeanNamesForType(
        getTaskExecutorResolvableType(payload)
    );

    if (beanNames.length == 0) {
      throw new TaskDelegationException("No executor found for type: " + payload.getClass());
    }

    if (beanNames.length > 1) {
      throw new TaskDelegationException("Multiple executors found for type: " + payload.getClass());
    }

    return beanNames[0];
  }

  @SuppressWarnings("unchecked")
  private <T> TaskExecutor<T> getBean(String name) {
    return (TaskExecutor<T>) beanFactory.getBean(name);
  }

  private <T> ResolvableType getTaskExecutorResolvableType(T payload) {
    return ResolvableType.forClassWithGenerics(
        TaskExecutor.class,
        ResolvableType.forClass(payload.getClass()));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.beanFactory = applicationContext;
  }
}
