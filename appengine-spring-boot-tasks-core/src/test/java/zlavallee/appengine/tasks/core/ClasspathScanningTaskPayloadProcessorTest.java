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
import zlavallee.anotherpackage.TestPayloadThree;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

@SpringBootTest
@EnableAutoConfiguration
public class ClasspathScanningTaskPayloadProcessorTest {

  @Autowired
  private ClasspathScanningTaskPayloadProcessor processor;

  @Test
  public void testFindPayloadClasses() {
    Collection<Class<?>> classes = processor.findPayloadClasses();

    assertEquals(3, classes.size());
    assertThat(classes, containsInAnyOrder(TestPayloadOne.class, TestPayloadTwo.class,
        TestPayloadThree.class));
  }

  @Configuration
  static class ClasspathScanningTaskPayloadProcessorConfiguration {

    @Bean
    public ClasspathScanningTaskPayloadProcessor processor() {
      ClasspathScanningTaskPayloadProcessor processor = new ClasspathScanningTaskPayloadProcessor();
      processor.addBasePackage("zlavallee.anotherpackage");
      return processor;
    }
  }
}
