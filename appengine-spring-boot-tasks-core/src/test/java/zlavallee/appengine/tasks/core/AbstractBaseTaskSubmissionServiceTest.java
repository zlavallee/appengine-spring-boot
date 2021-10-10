package zlavallee.appengine.tasks.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

class AbstractBaseTaskSubmissionServiceTest {

  private TestTaskSubmissionService submissionService;

  @BeforeEach
  public void setup() {
    submissionService = new TestTaskSubmissionService();
  }

  @Test
  public void testSubmitValidTask() {
    TestPayloadTwo payloadTwo = new TestPayloadTwo();
    submissionService.submit(payloadTwo);

    assertEquals(1, submissionService.taskCount());
    assertEquals(new Task("contextPath", payloadTwo), submissionService.getFirstTask());
  }

  @Test
  public void testSubmitInvalidTask() {
    assertThrows(IllegalArgumentException.class, () -> submissionService.submit("bad payload"));
  }

  private static class TestTaskSubmissionService extends AbstractBaseTaskSubmissionService {

    private final List<Task> tasks = new ArrayList<>();

    protected TestTaskSubmissionService() {
      super("contextPath");
    }

    @Override
    protected void submitTask(Task task) {
      tasks.add(task);
    }

    int taskCount() {
      return tasks.size();
    }

    List<Task> getTasks() {
      return tasks;
    }

    Task getTask(int index) {
      return tasks.get(index);
    }

    Task getFirstTask() {
      return getTask(0);
    }
  }

}
