package zlavallee.appengine.tasks.core;

import static zlavallee.appengine.tasks.core.CloudTasksUtils.toDuration;
import static zlavallee.appengine.tasks.core.CloudTasksUtils.toTimestamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class Task {

  private final String baseUrl;
  private final Object payload;
  private final PayloadMetadata metadata;

  public Task(String baseUrl, Object payload) {
    this.baseUrl = baseUrl;
    this.payload = payload;
    this.metadata = PayloadMetadata.valueOf(payload);
  }

  public Task(Object payload) {
    this(null, payload);
  }

  boolean isValid() {
    return metadata.isValidPayload();
  }

  String getTaskName() {
    return metadata.getTaskName();
  }

  String getSubmissionUrl() {
    String base = "";
    if (baseUrl != null) {
      base = "/" + baseUrl;
    }

    return base + metadata.getRelativeTaskUrl();
  }

  String getBody() {
    ObjectMapper jsonMapper = new ObjectMapper();
    try {
      return jsonMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  boolean hasDispatchDeadline() {
    return metadata.getDispatchDeadline() != null;
  }

  com.google.protobuf.Duration getDispatchDeadline() {
    return toDuration(metadata.getDispatchDeadline());
  }

  boolean hasScheduleTime() {
    return getScheduleTime() != null;
  }

  Timestamp getScheduleTime() {
    if (metadata.getScheduleTimeDelay() == null) {
      return null;
    }

    Instant scheduledTime = Instant.now().plus(metadata.getScheduleTimeDelay());

    return toTimestamp(scheduledTime);
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
    Task task = (Task) o;

    return Objects.equals(baseUrl, task.baseUrl)
        && Objects.equals(payload, task.payload);
  }
}
