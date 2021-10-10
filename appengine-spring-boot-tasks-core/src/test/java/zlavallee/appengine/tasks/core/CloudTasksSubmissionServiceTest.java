package zlavallee.appengine.tasks.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static zlavallee.appengine.tasks.core.TasksSettings.HANDLER_URL;

import com.google.cloud.tasks.v2.AppEngineHttpRequest;
import com.google.cloud.tasks.v2.HttpMethod;
import com.google.cloud.tasks.v2.Task;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.tasks.core.exception.TaskConfigurationException;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

class CloudTasksSubmissionServiceTest {

  private CloudTasksSubmissionService service;
  private GoogleCloudCreateTask mockCreateTask;
  private AppEngineApplicationService appEngineApplicationService;
  private Map<String, CloudTaskQueueConfiguration> cloudTaskQueueConfigurationMap;

  @BeforeEach
  public void setup() {
    mockCreateTask = mock(GoogleCloudCreateTask.class);
    cloudTaskQueueConfigurationMap = new HashMap<>();
    appEngineApplicationService = mock(AppEngineApplicationService.class);
    DefaultQueueConfiguration defaultQueueConfiguration = new DefaultQueueConfiguration(
        appEngineApplicationService, new AppEngineEnvironment(
            new MockEnvironment()
                .withProperty(AppEngineEnvironment.GCP_PROJECT_ID_KEY, "projectId")
    ));
    defaultQueueConfiguration.setCloudTaskQueueConfigurationMap(cloudTaskQueueConfigurationMap);
    service = new CloudTasksSubmissionService(
        "contextPath", mockCreateTask, defaultQueueConfiguration);
  }

  @Test
  public void testSubmit() {
    cloudTaskQueueConfigurationMap.put("payloadOne",
        new CloudTaskQueueConfiguration("projectId", "locationId", "queueName"));
    service.submit(new TestPayloadOne("message"));

    Task expected = Task.newBuilder()
        .setAppEngineHttpRequest(
            AppEngineHttpRequest.newBuilder()
                .setBody(
                    ByteString.copyFrom("{\"message\":\"message\"}", Charset.defaultCharset()))
                .setRelativeUri("/contextPath" + HANDLER_URL + "/payloadOne")
                .putHeaders(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .setHttpMethod(HttpMethod.POST)
                .build()
        ).build();

    verify(mockCreateTask)
        .create(eq("projects/projectId/locations/locationId/queues/queueName"),
            eq(expected));
  }

  @Test
  public void testSubmitWithDuration() {
    cloudTaskQueueConfigurationMap.put("payloadTwo",
        new CloudTaskQueueConfiguration("projectId", "locationId", "queueName"));
    service.submit(new TestPayloadTwo());
    Duration duration = Duration.of(100, ChronoUnit.SECONDS);

    Task.Builder expectedBuilder = Task.newBuilder()
        .setAppEngineHttpRequest(
            AppEngineHttpRequest.newBuilder()
                .setBody(
                    ByteString.copyFrom("{}", Charset.defaultCharset()))
                .setRelativeUri("/contextPath" + HANDLER_URL + "/payloadTwo")
                .putHeaders(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .setHttpMethod(HttpMethod.POST)
                .build()
        )
        .setDispatchDeadline(com.google.protobuf.Duration.newBuilder()
            .setSeconds(duration.getSeconds())
            .setNanos(duration.getNano())
            .build());

    verify(mockCreateTask)
        .create(eq("projects/projectId/locations/locationId/queues/queueName"),
            argThat(argument -> {
              if (argument.getScheduleTime().equals(Timestamp.getDefaultInstance())) {
                return false;
              }

              Task expected = expectedBuilder.setScheduleTime(argument.getScheduleTime()).build();

              return expected.equals(argument);
            }));
  }

  @Test
  public void testNoConfigurationFound() {
    TaskConfigurationException exception = assertThrows(TaskConfigurationException.class,
        () -> service.submit(new TestPayloadOne("message")));

    assertEquals("No location has been set.", exception.getMessage());
  }
}
