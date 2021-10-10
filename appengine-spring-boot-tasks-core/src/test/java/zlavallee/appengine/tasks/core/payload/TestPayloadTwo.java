package zlavallee.appengine.tasks.core.payload;

import java.time.temporal.ChronoUnit;
import zlavallee.appengine.tasks.core.Duration;
import zlavallee.appengine.tasks.core.Payload;

@Payload(
    taskName = "payloadTwo",
    dispatchDeadline = @Duration(value = 100, unit = ChronoUnit.SECONDS),
    scheduleTimeDelay = @Duration(value = 100, unit = ChronoUnit.DAYS))
public class TestPayloadTwo {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }

    return getClass() == o.getClass();
  }
}
