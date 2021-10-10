package zlavallee.appengine.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import zlavallee.appengine.core.admin.NoOpAppEngineApplicationService;

class NoOpAppEngineApplicationServiceTest {

  @Test
  public void testIsEmpty() {
    assertTrue(NoOpAppEngineApplicationService.instanceOf().getCurrentApplication().isEmpty());
  }
}