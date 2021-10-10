package zlavallee.appengine.tasks.core.payload;

import java.util.Objects;
import zlavallee.appengine.tasks.core.Payload;

@Payload(taskName = "payloadOne")
public class TestPayloadOne {

  private String message;

  public TestPayloadOne(String message) {
    this.message = message;
  }

  public TestPayloadOne() {
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (getClass() != o.getClass()) {
      return false;
    }
    TestPayloadOne testPayloadOne = (TestPayloadOne) o;

    return Objects.equals(message, testPayloadOne.message);
  }
}
