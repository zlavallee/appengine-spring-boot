package zlavallee.appengine.tasks.core;

public interface TaskPayloadRegister {

  void registerAnnotatedClasses();

  <T> void register(Class<T> tClass);
}
