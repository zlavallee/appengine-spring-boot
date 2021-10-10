package zlavallee.appengine.tasks.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

class ClasspathScanningTaskPayloadProcessorTest {

  private ClasspathScanningTaskPayloadProcessor processor;

  @BeforeEach
  public void setup() {
    processor = new ClasspathScanningTaskPayloadProcessor();
    processor.addBasePackage("zlavallee.appengine.tasks.core.payload");
  }

  @Test
  public void testFindPayloadClasses() {
    Collection<Class<?>> classes = processor.findPayloadClasses();

    assertEquals(2, classes.size());
    assertThat(classes, containsInAnyOrder(TestPayloadOne.class, TestPayloadTwo.class));
  }
}
