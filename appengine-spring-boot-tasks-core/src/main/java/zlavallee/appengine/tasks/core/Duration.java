package zlavallee.appengine.tasks.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
public @interface Duration {

  long value() default -1;

  ChronoUnit unit() default ChronoUnit.MILLIS;
}
