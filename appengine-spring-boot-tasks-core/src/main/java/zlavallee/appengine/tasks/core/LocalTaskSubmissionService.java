package zlavallee.appengine.tasks.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LocalTaskSubmissionService extends AbstractBaseTaskSubmissionService {

  private static final Logger logger = LoggerFactory.getLogger(LocalTaskSubmissionService.class);

  private final TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

  public LocalTaskSubmissionService(String contextPath) {
    super(contextPath);
  }

  @Override
  protected void submitTask(Task task) {
    taskExecutor.execute(
        () -> {
          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          RestTemplate restTemplate = new RestTemplate();

          HttpEntity<String> entity = new HttpEntity<>(task.getBody(), headers);
          ResponseEntity<String> responses =
              restTemplate.postForEntity("http://localhost:8080" + task.getSubmissionUrl(), entity,
                  String.class);

          logger.debug("Response: {}", responses);
        });
  }
}
