package zlavallee.appengine.autoconfigure;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static zlavallee.appengine.core.AppEngineEnvironment.APPLICATION_ID_KEY;

import com.google.api.services.appengine.v1.Appengine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zlavallee.appengine.core.AppEngineApplicationService;
import zlavallee.appengine.core.AppEngineApplicationServiceImpl;
import zlavallee.appengine.core.AppEngineEnvironment;
import zlavallee.appengine.core.AppEngineSettings;
import zlavallee.appengine.core.admin.AppEngineAdmin;
import zlavallee.appengine.core.admin.NoOpAppEngineApplicationService;

class AppEngineAutoConfigureTest {

  private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(
          LocalAppEngineConfiguration.class,
          CloudAppEngineConfiguration.class,
          AppEngineConfiguration.class,
          AppEngineAutoConfigure.class
      ));

  @Test
  public void testCoreConfiguration() {
    contextRunner.run(context -> {
      assertThat(context).hasSingleBean(AppEngineEnvironment.class);
      assertThat(context.getBean(AppEngineApplicationService.class))
          .isInstanceOf(NoOpAppEngineApplicationService.class);
      assertThat(context).doesNotHaveBean(Appengine.class);
      assertThat(context).doesNotHaveBean(AppEngineAdmin.class);
      assertThat(context).hasSingleBean(AppEngineSettings.class);
    });
  }

  @Test
//  @Disabled
  public void testCloudCoreConfiguration() {
    contextRunner.withPropertyValues(property(APPLICATION_ID_KEY, "applicationId"))
        .run(context -> {
          assertThat(context).hasSingleBean(AppEngineEnvironment.class);
          assertThat(context.getBean(AppEngineApplicationService.class))
              .isInstanceOf(AppEngineApplicationServiceImpl.class);
          assertThat(context).hasSingleBean(Appengine.class);
          assertThat(context).hasSingleBean(AppEngineAdmin.class);
          assertThat(context).hasSingleBean(AppEngineSettings.class);
        });
  }

  @Test
  public void testCloudCoreWithExistingAppEngineConfiguration() {
    contextRunner
        .withConfiguration(AutoConfigurations.of(AppEngine.class))
        .withPropertyValues(property(APPLICATION_ID_KEY, "applicationId"))
        .run(context -> {
          assertThat(context).hasSingleBean(AppEngineEnvironment.class);
          assertThat(context.getBean(AppEngineApplicationService.class))
              .isInstanceOf(AppEngineApplicationServiceImpl.class);
          assertThat(context).hasSingleBean(Appengine.class);
          assertThat(context).hasSingleBean(AppEngineAdmin.class);
          assertThat(context).hasSingleBean(AppEngineSettings.class);
        });
  }

  private String property(String key, String value) {
    return key + "=" + value;
  }

  @Configuration
  static class AppEngine {
    @Bean
    public Appengine appengine() {
      return mock(Appengine.class);
    }
  }
}