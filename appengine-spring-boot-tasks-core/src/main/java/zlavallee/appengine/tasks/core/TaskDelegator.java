package zlavallee.appengine.tasks.core;

public interface TaskDelegator {

  <T> void delegate(T payload);
}
