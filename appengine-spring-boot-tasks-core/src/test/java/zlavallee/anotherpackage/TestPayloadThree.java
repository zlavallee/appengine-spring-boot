package zlavallee.anotherpackage;

import zlavallee.appengine.tasks.core.Payload;

@Payload(taskName = "payloadThree")
public class TestPayloadThree {

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
