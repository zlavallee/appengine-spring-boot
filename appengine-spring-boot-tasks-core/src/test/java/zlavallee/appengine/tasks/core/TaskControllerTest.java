package zlavallee.appengine.tasks.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;

class TaskControllerTest {

  private TaskController<TestPayloadOne> taskController;
  private TaskDelegator mockTaskDelegator;

  @BeforeEach
  public void setup() {
    mockTaskDelegator = mock(TaskDelegator.class);
    taskController = new TaskController<>(TestPayloadOne.class, mockTaskDelegator);
  }

  @Test
  public void testAcceptValid() {
    ResponseEntity<String> response = taskController.accept(
        new HashMap<>(),
        "{\"message\":\"message content\"}");

    assertEquals(new ResponseEntity<>(HttpStatus.OK), response);

    verify(mockTaskDelegator).delegate(new TestPayloadOne("message content"));
  }

  @Test
  public void testAcceptBadRequest() {
    ResponseEntity<String> response = taskController.accept(new HashMap<>(),
        "{\"nonsense\":\"message content\"}");

    assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), response);

    verify(mockTaskDelegator, never()).delegate(any());
  }
}
