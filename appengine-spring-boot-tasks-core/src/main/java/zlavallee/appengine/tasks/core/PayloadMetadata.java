package zlavallee.appengine.tasks.core;

import java.util.Objects;

public class PayloadMetadata {

  private final Class<?> tClass;

  public PayloadMetadata(Class<?> tClass) {
    this.tClass = tClass;
  }

  static PayloadMetadata valueOf(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("Object must not be null");
    }

    return new PayloadMetadata(o.getClass());
  }

  void validate() {
    if (!isValidPayload()) {
      throw new IllegalArgumentException("Invalid payload configuration: " + this.tClass);
    }
  }

  boolean isValidPayload() {
    return getPayload() != null;
  }

  private Payload getPayload() {
    return tClass.getAnnotation(Payload.class);
  }

  public String getTaskName() {
    return getPayload().taskName();
  }

  public String getRelativeTaskUrl() {
    return TasksSettings.HANDLER_URL + "/" + getTaskName();
  }

  public java.time.Duration getDispatchDeadline() {
    return toDuration(getPayload().dispatchDeadline());
  }

  public java.time.Duration getScheduleTimeDelay() {
    return toDuration(getPayload().scheduleTimeDelay());
  }

  private java.time.Duration toDuration(Duration duration) {
    if (isSet(duration)) {
      return java.time.Duration.of(duration.value(), duration.unit());
    }
    return null;
  }

  private boolean isSet(Duration duration) {
    return duration.value() != -1;
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
    PayloadMetadata payloadMetadata = (PayloadMetadata) o;

    return Objects.equals(tClass, payloadMetadata.tClass);
  }
}
