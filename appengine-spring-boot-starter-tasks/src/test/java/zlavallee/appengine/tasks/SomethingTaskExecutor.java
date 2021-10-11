package zlavallee.appengine.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import zlavallee.appengine.tasks.core.TaskExecutor;

@Component
public class SomethingTaskExecutor implements TaskExecutor<Something> {

  private static final Logger logger = LoggerFactory.getLogger(SomethingTaskExecutor.class);

  private long executionCount;

  @Override
  public void execute(Something payload) {
    executionCount++;

    logger.info("Something execution count: {}", executionCount);
  }
}
