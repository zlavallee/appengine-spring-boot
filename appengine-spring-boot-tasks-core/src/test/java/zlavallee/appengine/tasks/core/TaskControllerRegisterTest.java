package zlavallee.appengine.tasks.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import zlavallee.anotherpackage.TestPayloadThree;
import zlavallee.appengine.tasks.core.TaskControllerRegisterTest.TaskControllerRegisterTestConfiguration;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Import(TaskControllerRegisterTestConfiguration.class)
class TaskControllerRegisterTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TaskDelegator mockTaskDelegator;
  @Autowired
  private TaskControllerRegister taskControllerRegister;

  @Test
  public void testRegister() throws Exception {
    taskControllerRegister.register(TestPayloadThree.class);

    mockMvc.perform(
            post(TasksSettings.HANDLER_URL + "/payloadThree")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(mockTaskDelegator).delegate(new TestPayloadThree());
  }

  @Test
  public void testRegisterAnnotatedClasses() throws Exception {
    mockMvc.perform(
            post(TasksSettings.HANDLER_URL + "/payloadOne")
                .content("{\"message\":\"message content\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(mockTaskDelegator).delegate(new TestPayloadOne("message content"));

    mockMvc.perform(
            post(TasksSettings.HANDLER_URL + "/payloadTwo")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(mockTaskDelegator).delegate(new TestPayloadTwo());

  }

  @Test
  public void testRegisterInvalidClass() {
    assertThrows(IllegalArgumentException.class, () ->
        taskControllerRegister.register(String.class));
  }

  @SpringBootApplication
  @Configuration
  static class TaskControllerRegisterTestConfiguration {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public TaskDelegator taskDelegator() {
      return mock(TaskDelegator.class);
    }

    @Bean
    public TaskControllerRegister taskControllerRegister() {
      return new TaskControllerRegister(
          requestMappingHandlerMapping, taskDelegator(), processor());
    }

    @Bean
    public ClasspathScanningTaskPayloadProcessor processor() {
      ClasspathScanningTaskPayloadProcessor processor = new ClasspathScanningTaskPayloadProcessor();
      processor.addBasePackage("zlavallee.appengine.tasks.core.payload");
      return processor;
    }
  }
}
