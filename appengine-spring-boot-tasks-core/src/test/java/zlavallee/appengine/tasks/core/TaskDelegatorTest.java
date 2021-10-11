package zlavallee.appengine.tasks.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.anotherpackage.TestPayloadThree;
import zlavallee.appengine.tasks.core.TaskDelegatorTest.TaskDelegatorTestConfiguration;
import zlavallee.appengine.tasks.core.exception.TaskDelegationException;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

@SpringBootTest
@EnableAutoConfiguration
@Import(TaskDelegatorTestConfiguration.class)
public class TaskDelegatorTest {

  @Autowired
  private TestTaskExecutor<TestPayloadTwo> testPayloadTwoTestTaskExecutor;
  @Autowired
  private TestTaskExecutor<TestPayloadThree> testPayloadThreeTestTaskExecutor;
  @Autowired
  private TaskDelegator taskDelegator;
  @Autowired
  private ConcreteExecutor concreteExecutor;

  @AfterEach
  public void tearDown() {
    this.testPayloadTwoTestTaskExecutor.clear();
    this.testPayloadThreeTestTaskExecutor.clear();
  }

  @Test
  public void testDelegate() {
    taskDelegator.delegate(new TestPayloadOne("message"));

    assertEquals(1, concreteExecutor.payloads.size());
    assertEquals(new TestPayloadOne("message"), concreteExecutor.payloads.get(0));

    TestPayloadTwo testPayloadTwo = new TestPayloadTwo();

    taskDelegator.delegate(testPayloadTwo);

    assertEquals(1, concreteExecutor.payloads.size());
    assertEquals(1, testPayloadTwoTestTaskExecutor.payloads.size());
    assertEquals(testPayloadTwo, testPayloadTwoTestTaskExecutor.payloads.get(0));
  }

  @Test
  public void testDelegateNoExecutor() {
    TaskDelegationException exception = assertThrows(TaskDelegationException.class, () -> {
      taskDelegator.delegate(new UnRegisteredExecutor());
    });

    assertThat(exception.getMessage(), containsString("No executor found"));
  }

  @Configuration
  static class TaskDelegatorTestConfiguration {

    @Bean
    public TaskDelegator taskDelegator() {
      return new ApplicationContextTaskDelegator();
    }

    @Bean
    public TestTaskExecutor<TestPayloadTwo> payloadTwoTestTaskExecutor() {
      return new TestTaskExecutor<>(TestPayloadTwo.class);
    }

    @Bean
    public TestTaskExecutor<TestPayloadThree> payloadThreeTestTaskExecutor() {
      return new TestTaskExecutor<>(TestPayloadThree.class);
    }

    @Bean
    public ConcreteExecutor concreteExecutor() {
      return new ConcreteExecutor();
    }
  }

  static class ConcreteExecutor implements TaskExecutor<TestPayloadOne> {

    private final List<TestPayloadOne> payloads = new ArrayList<>();

    @Override
    public Class<TestPayloadOne> getPayloadClass() {
      return TestPayloadOne.class;
    }

    @Override
    public void execute(TestPayloadOne payload) {
      payloads.add(payload);
    }
  }

  static class TestTaskExecutor<T> extends TaskExecutorSupport<T> {

    private final List<T> payloads = new ArrayList<>();

    public TestTaskExecutor(Class<T> payloadClass) {
      super(payloadClass);
    }

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

  @Payload(taskName = "unRegisteredExecutor")
  static class UnRegisteredExecutor {

  }
}
