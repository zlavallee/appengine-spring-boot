package zlavallee.appengine.tasks.core;

import com.google.cloud.tasks.v2.AppEngineHttpRequest;
import com.google.cloud.tasks.v2.HttpMethod;
import com.google.protobuf.ByteString;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CloudTasksSubmissionService extends AbstractBaseTaskSubmissionService {

  private static final Logger logger = LoggerFactory.getLogger(CloudTasksSubmissionService.class);

  private final GoogleCloudCreateTask googleCloudCreateTask;
  private final QueueConfiguration queueConfiguration;

  public CloudTasksSubmissionService(
      String contextPath,
      GoogleCloudCreateTask googleCloudCreateTask,
      QueueConfiguration queueConfiguration) {
    super(contextPath);
    this.googleCloudCreateTask = googleCloudCreateTask;
    this.queueConfiguration = queueConfiguration;
  }

  @Override
  protected void submitTask(Task task) {
    logger.trace("Submitting Cloud Task: {}", task);

    com.google.cloud.tasks.v2.Task.Builder taskBuilder = com.google.cloud.tasks.v2.Task.newBuilder()
        .setAppEngineHttpRequest(
            AppEngineHttpRequest.newBuilder()
                .setBody(ByteString.copyFrom(task.getBody(), Charset.defaultCharset()))
                .setRelativeUri(task.getSubmissionUrl())
                .putHeaders(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .setHttpMethod(HttpMethod.POST)
                .build());

    if (task.hasScheduleTime()) {
      taskBuilder.setScheduleTime(task.getScheduleTime());
    }

    if (task.hasDispatchDeadline()) {
      taskBuilder.setDispatchDeadline(task.getDispatchDeadline());
    }

    com.google.cloud.tasks.v2.Task submitted =
        googleCloudCreateTask.create(getQueueName(task.getTaskName()), taskBuilder.build());

    logger.trace("Cloud Task submitted: {}", submitted);
  }

  private String getQueueName(String taskName) {
    return queueConfiguration.getQueueName(taskName).toString();
  }
}
