package zlavallee.appengine.tasks.core;

public interface TaskSubmissionService {

  <T> void submit(T payload);
}
