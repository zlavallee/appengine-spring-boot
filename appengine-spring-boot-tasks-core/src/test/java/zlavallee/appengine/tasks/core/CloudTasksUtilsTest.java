package zlavallee.appengine.tasks.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static zlavallee.appengine.tasks.core.CloudTasksUtils.getCloudTasksLocation;

import org.junit.jupiter.api.Test;

class CloudTasksUtilsTest {

  @Test
  public void testGetCloudTasksLocation() {
    assertEquals("europe-west1", getCloudTasksLocation("europe-west"));
    assertEquals("us-central1", getCloudTasksLocation("us-central"));
    assertEquals("normal-location", getCloudTasksLocation("normal-location"));
  }
}