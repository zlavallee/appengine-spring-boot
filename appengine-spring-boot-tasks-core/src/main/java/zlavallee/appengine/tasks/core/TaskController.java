package zlavallee.appengine.tasks.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class TaskController<T> {

  private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
  private final Class<T> payloadClass;
  private final TaskDelegator taskDelegator;

  TaskController(Class<T> payloadClass, TaskDelegator taskDelegator) {
    this.payloadClass = payloadClass;
    this.taskDelegator = taskDelegator;
  }

  public ResponseEntity<String> accept(
      @RequestHeader Map<String, String> headers,
      @RequestBody String body) {;
    logger.trace("Handling task: {}", body);

    try {
      T task = createTask(body);
      logger.trace("Executing task: {}", task);
      taskDelegator.delegate(task);
    } catch (JsonProcessingException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private T createTask(String body) throws JsonProcessingException {
    ObjectMapper jsonMapper = new ObjectMapper();
    return jsonMapper.readValue(body, payloadClass);
  }
}
