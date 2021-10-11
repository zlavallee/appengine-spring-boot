package zlavallee.appengine.tasks.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.tasks.core.TaskDelegatorTest.TaskDelegatorTestConfiguration;
import zlavallee.appengine.tasks.core.exception.TaskDelegationException;

@SpringBootTest
@Import(TaskDelegatorTestConfiguration.class)
public class TaskDelegatorTest {

  @Autowired
  private TestTaskExecutor<String> stringTaskExecutor;
  @Autowired
  private TestTaskExecutor<Date> dateTaskExecutor;
  @Autowired
  private TaskDelegator taskDelegator;

  @AfterEach
  public void tearDown() {
    this.stringTaskExecutor.clear();
    this.dateTaskExecutor.clear();
  }

  @Test
  public void testDelegate() {
    taskDelegator.delegate("test payload");

    assertEquals(1, stringTaskExecutor.payloads.size());
    assertEquals("test payload", stringTaskExecutor.payloads.get(0));

    Date date = new Date();

    taskDelegator.delegate(date);

    assertEquals(1, stringTaskExecutor.payloads.size());
    assertEquals(1, dateTaskExecutor.payloads.size());
    assertEquals(date, dateTaskExecutor.payloads.get(0));
  }

  @Test
  public void testDelegateNoExecutor() {
    TaskDelegationException exception = assertThrows(TaskDelegationException.class, () -> {
      taskDelegator.delegate(1.0f);
    });

    assertThat(exception.getMessage(), containsString("No executor found"));
  }

  @Test
  public void testDelegateMultipleExecutors() {
    TaskDelegationException exception = assertThrows(TaskDelegationException.class, () -> {
      taskDelegator.delegate(1);
    });

    assertThat(exception.getMessage(), containsString("Multiple executors found"));
  }

  @Configuration
  static class TaskDelegatorTestConfiguration {

    @Bean
    public TaskDelegator taskDelegator() {
      return new TaskDelegator();
    }

    @Bean
    public TestTaskExecutor<String> stringTaskExecutor() {
      return new TestTaskExecutor<>();
    }

    @Bean
    public TestTaskExecutor<Date> dateTaskExecutor() {
      return new TestTaskExecutor<>();
    }

    @Bean
    public TestTaskExecutor<Integer> firstIntegerTaskExecutor() {
      return new TestTaskExecutor<>();
    }

    @Bean
    public TestTaskExecutor<Integer> secondIntegerTaskExecutor() {
      return new TestTaskExecutor<>();
    }
  }

  static class TestTaskExecutor<T> implements TaskExecutor<T> {

    private final List<T> payloads = new ArrayList<>();

    @Override
    public void execute(T payload) {
      payloads.add(payload);
    }

    public List<T> getPayloads() {
      return payloads;
    }

    public void clear() {
      this.payloads.clear();
    }
  }
}
