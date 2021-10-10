package zlavallee.appengine.tasks.core.exception;

public class TaskExecutionException extends RuntimeException {

  public TaskExecutionException(String message) {
    super(message);
  }
}
