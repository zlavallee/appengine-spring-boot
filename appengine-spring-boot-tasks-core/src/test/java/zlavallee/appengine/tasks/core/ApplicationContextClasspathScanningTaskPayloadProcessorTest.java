package zlavallee.appengine.tasks.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import zlavallee.appengine.tasks.core.ApplicationContextClasspathScanningTaskPayloadProcessorTest.ClasspathScanningTaskPayloadProcessorConfiguration;
import zlavallee.appengine.tasks.core.TaskDelegatorTest.UnRegisteredExecutor;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

@SpringBootTest
@EnableAutoConfiguration
@Import(ClasspathScanningTaskPayloadProcessorConfiguration.class)
public class ApplicationContextClasspathScanningTaskPayloadProcessorTest {

  @Autowired
  private ClasspathScanningTaskPayloadProcessor processor;

  @Test
  public void testFindPayloadClasses() {
    Collection<Class<?>> classes = processor.findPayloadClasses();

    assertEquals(3, classes.size());
    assertThat(classes, containsInAnyOrder(TestPayloadOne.class, TestPayloadTwo.class, UnRegisteredExecutor.class));
  }

  @Configuration
  static class ClasspathScanningTaskPayloadProcessorConfiguration {

    @Bean
    public ClasspathScanningTaskPayloadProcessor processor() {
      return new ClasspathScanningTaskPayloadProcessor();
    }
  }
}
