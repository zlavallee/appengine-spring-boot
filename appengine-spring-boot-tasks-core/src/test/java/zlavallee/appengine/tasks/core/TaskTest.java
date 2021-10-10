package zlavallee.appengine.tasks.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static zlavallee.appengine.tasks.core.CloudTasksUtils.toDuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

class TaskTest {

  @Test
  public void testTaskWithOutContextPath() {
    Task task = new Task(new TestPayloadTwo());

    assertTrue(task.isValid());
    assertEquals(TasksSettings.HANDLER_URL + "/payloadTwo", task.getSubmissionUrl());
    assertEquals("{}", task.getBody());
    assertEquals("payloadTwo", task.getTaskName());
    assertTrue(task.hasDispatchDeadline());
    assertEquals(toDuration(Duration.of(100, ChronoUnit.SECONDS)), task.getDispatchDeadline());
    assertTrue(task.hasScheduleTime());
  }

  @Test
  public void testTaskWithContextPath() {
    Task task = new Task("contextPath", new TestPayloadOne("message content"));

    assertTrue(task.isValid());
    assertEquals("/contextPath" + TasksSettings.HANDLER_URL + "/payloadOne",
        task.getSubmissionUrl());
    assertEquals("{\"message\":\"message content\"}", task.getBody());
    assertFalse(task.hasDispatchDeadline());
    assertFalse(task.hasScheduleTime());
  }

  @Test
  public void testTaskBadObject() {
    Task task = new Task("contextPath", new ClassThatJacksonCannotSerialize());

    assertThrows(IllegalArgumentException.class, task::getBody);
  }

  @Test
  public void testEquality() {
    assertEquals(
        new Task("contextPath", new TestPayloadOne("message")),
        new Task("contextPath", new TestPayloadOne("message")));

    assertNotEquals(
        new Task("contextPath", new TestPayloadOne("message")),
        new Task("contextPath", new TestPayloadTwo())
    );
  }

  private static class ClassThatJacksonCannotSerialize {

    private final ClassThatJacksonCannotSerialize self = this;

    @Override
    public String toString() {
      return self.getClass().getName();
    }
  }
}
