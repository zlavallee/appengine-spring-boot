package zlavallee.appengine.tasks;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.core.AppEngineSettings;

@SpringBootTest
public class AppEngineSpringBootStarterTest {

  @Autowired
  public AppEngineEnvironment appEngineEnvironment;
  @Autowired
  public AppEngineApplicationService appEngineApplicationService;
  @Autowired
  public AppEngineSettings appEngineSettings;

  @Test
  void contextLoads() {
    assertNotNull(appEngineEnvironment);
    assertNotNull(appEngineApplicationService);
    assertNotNull(appEngineSettings);
  }
}
