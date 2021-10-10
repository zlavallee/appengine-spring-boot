package zlavallee.appengine.tasks.core;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class AppEngineTaskRequestHeaders {

  private static final String QUEUE_NAME_HEADER = "X-AppEngine-QueueName";
  private static final String TASK_NAME_HEADER = "X-AppEngine-TaskName";
  private static final String TASK_RETRY_COUNT_HEADER = "X-AppEngine-TaskRetryCount";
  private static final String TASK_EXECUTION_COUNT_HEADER = "X-AppEngine-TaskExecutionCount";
  private static final String TASK_ETA_HEADER = "X-AppEngine-TaskETA";

  private static final String TASK_PREVIOUS_RESPONSE_HEADER = "X-AppEngine-TaskPreviousResponse";
  private static final String TASK_RETRY_REASON_HEADER = "X-AppEngine-TaskRetryReason";
  private static final String FAIL_FAST_HEADER = "X-AppEngine-FailFast";

  private static final String[] REQUIRED_HEADERS = new String[]{
      QUEUE_NAME_HEADER,
      TASK_NAME_HEADER,
      TASK_ETA_HEADER,
      TASK_RETRY_COUNT_HEADER,
      TASK_EXECUTION_COUNT_HEADER,
      TASK_ETA_HEADER
  };

  private static final String[] OPTIONAL_HEADERS = new String[]{
      TASK_PREVIOUS_RESPONSE_HEADER,
      TASK_RETRY_REASON_HEADER,
      FAIL_FAST_HEADER
  };

  private final Map<String, String> headers;

  public AppEngineTaskRequestHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public boolean isValid() {
    for (String requiredHeader : REQUIRED_HEADERS) {
      if (!headers.containsKey(requiredHeader)) {
        return false;
      }
    }
    return true;
  }

  public String getQueueName() {
    return get(QUEUE_NAME_HEADER);
  }

  public String getTaskName() {
    return get(TASK_NAME_HEADER);
  }

  public Integer getTaskRetryCount() {
    return getInteger(TASK_RETRY_COUNT_HEADER);
  }

  public Integer getTaskExecutionCount() {
    return getInteger(TASK_EXECUTION_COUNT_HEADER);
  }

  public Instant getTaskEta() {
    return Instant.ofEpochSecond(Long.parseLong(get(TASK_ETA_HEADER)));
  }

  public Optional<Integer> getTaskPreviousResponse() {
    return getOptional(TASK_RETRY_REASON_HEADER).map(Integer::valueOf);
  }

  public Optional<String> getTaskRetryReason() {
    return getOptional(TASK_RETRY_REASON_HEADER);
  }

  public Optional<String> getFailFast() {
    return getOptional(FAIL_FAST_HEADER);
  }

  private Optional<String> getOptional(String key) {
    return Optional.ofNullable(get(key));
  }

  private String get(String key) {
    return headers.get(key);
  }

  private Integer getInteger(String key) {
    return Integer.valueOf(get(key));
  }
}
