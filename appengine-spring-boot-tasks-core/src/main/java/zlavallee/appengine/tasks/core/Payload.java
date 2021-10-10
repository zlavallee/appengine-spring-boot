package zlavallee.appengine.tasks.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Payload {

  String taskName();

  Duration dispatchDeadline() default @Duration();

  Duration scheduleTimeDelay() default @Duration();

}


