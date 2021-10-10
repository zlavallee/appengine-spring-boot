package zlavallee.appengine.tasks.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseTaskSubmissionService implements TaskSubmissionService {

  private static final Logger logger =
      LoggerFactory.getLogger(AbstractBaseTaskSubmissionService.class);

  private final String contextPath;

  protected AbstractBaseTaskSubmissionService(String contextPath) {
    this.contextPath = contextPath;
  }

  @Override
  public <T> void submit(T payload) {
    logger.trace("Submitting task with payload: {}", payload);

    Task task = new Task(contextPath, payload);

    if (!task.isValid()) {
      throw new IllegalArgumentException("Task submission payload must be annotated with @Payload");
    }

    submitTask(task);

    logger.trace("Task submitted successfully. {}", payload);
  }

  protected abstract void submitTask(Task task);

}
