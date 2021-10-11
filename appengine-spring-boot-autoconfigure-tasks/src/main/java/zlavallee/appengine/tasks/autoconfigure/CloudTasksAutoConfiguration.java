package zlavallee.appengine.tasks.autoconfigure;

import java.util.Set;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import zlavallee.appengine.tasks.core.ClasspathScanningTaskPayloadProcessor;
import zlavallee.appengine.tasks.core.TaskControllerRegister;
import zlavallee.appengine.tasks.core.TaskDelegator;
import zlavallee.appengine.tasks.core.TaskExecutor;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(CloudTasksConfigurationProperties.class)
@AutoConfigureAfter({
    zlavallee.appengine.tasks.autoconfigure.LocalTasksConfiguration.class,
    zlavallee.appengine.tasks.autoconfigure.CloudTasksConfiguration.class})
public class CloudTasksAutoConfiguration {

  private final CloudTasksConfigurationProperties cloudTasksConfigurationProperties;
  private final RequestMappingHandlerMapping requestMappingHandlerMapping;

  public CloudTasksAutoConfiguration(
      CloudTasksConfigurationProperties cloudTasksConfigurationProperties,
      RequestMappingHandlerMapping requestMappingHandlerMapping) {
    this.cloudTasksConfigurationProperties = cloudTasksConfigurationProperties;
    this.requestMappingHandlerMapping = requestMappingHandlerMapping;
  }

  @Bean
  public ClasspathScanningTaskPayloadProcessor classpathScanningTaskPayloadProcessor() {
    ClasspathScanningTaskPayloadProcessor processor = new ClasspathScanningTaskPayloadProcessor();
    processor.addBasePackages(cloudTasksConfigurationProperties.getPayloadBasePackages());
    return processor;
  }

  @Bean
  public TaskDelegator taskDelegator(Set<TaskExecutor<?>> taskExecutors) {
    return new TaskDelegator(taskExecutors);
  }

  @Bean
  public TaskControllerRegister taskControllerRegister(Set<TaskExecutor<?>> taskExecutors) {
    return new TaskControllerRegister(
        requestMappingHandlerMapping,
        taskDelegator(taskExecutors),
        classpathScanningTaskPayloadProcessor());
  }
}
