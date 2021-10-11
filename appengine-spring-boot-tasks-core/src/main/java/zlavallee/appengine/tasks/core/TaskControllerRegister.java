package zlavallee.appengine.tasks.core;

import static zlavallee.appengine.tasks.core.TasksSettings.CONTROLLER_METHOD_NAME;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class TaskControllerRegister implements TaskPayloadRegister {

  private static final Logger logger = LoggerFactory.getLogger(TaskControllerRegister.class);

  private final RequestMappingHandlerMapping requestMappingHandlerMapping;
  private final TaskDelegator taskDelegator;
  private final ClasspathScanningTaskPayloadProcessor processor;

  public TaskControllerRegister(
      RequestMappingHandlerMapping requestMappingHandlerMapping,
      TaskDelegator taskDelegator,
      ClasspathScanningTaskPayloadProcessor processor) {
    this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    this.taskDelegator = taskDelegator;
    this.processor = processor;
  }


  @Override
  @PostConstruct
  public void registerAnnotatedClasses() {
    processor.findPayloadClasses().forEach(this::register);
  }

  @Override
  public <T> void register(Class<T> tClass) {
    logger.debug("Registering task controller for payload class: {}", tClass);

    PayloadMetadata payloadMetadata = new PayloadMetadata(tClass);

    payloadMetadata.validate();

    RequestMappingInfo requestMappingInfo =
        RequestMappingInfo.paths(payloadMetadata.getRelativeTaskUrl())
            .methods(RequestMethod.POST)
            .consumes(MediaType.APPLICATION_JSON.toString())
            .build();

    logger.trace("Registering mapping: {}", requestMappingInfo);

    try {
      requestMappingHandlerMapping.registerMapping(
          requestMappingInfo,
          new TaskController<>(tClass, taskDelegator),
          TaskController.class.getDeclaredMethod(CONTROLLER_METHOD_NAME, Map.class, String.class));
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(
          "Unable to find task controller method. This should never happen.", e);
    }
  }
}
