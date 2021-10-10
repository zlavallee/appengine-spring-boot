package zlavallee.appengine.core;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class AppEngineEnvironment implements EnvironmentAware {

  public static final String APPLICATION_ID_KEY = "GAE_APPLICATION";
  public static final String GAE_DEPLOYMENT_ID_KEY = "GAE_DEPLOYMENT_ID";
  public static final String GAE_ENV_KEY = "GAE_ENV";
  public static final String GAE_INSTANCE_KEY = "GAE_INSTANCE";
  public static final String GAE_MEMORY_MB_KEY = "GAE_MEMORY_MB";
  public static final String GAE_RUNTIME_KEY = "GAE_RUNTIME";
  public static final String GAE_SERVICE_KEY = "GAE_SERVICE";
  public static final String GAE_VERSION_KEY = "GAE_VERSION";
  public static final String GCP_PROJECT_ID_KEY = "GOOGLE_CLOUD_PROJECT";
  public static final String PORT_KEY = "PORT";

  private Environment environment;

  public AppEngineEnvironment() {
  }

  public AppEngineEnvironment(Environment environment) {
    this.environment = environment;
  }

  public String applicationId() {
    return environment.getProperty(APPLICATION_ID_KEY);
  }

  public String deploymentId() {
    return environment.getProperty(GAE_DEPLOYMENT_ID_KEY);
  }

  public String env() {
    return environment.getProperty(GAE_ENV_KEY);
  }

  public String instance() {
    return environment.getProperty(GAE_INSTANCE_KEY);
  }

  public String memoryMb() {
    return environment.getProperty(GAE_MEMORY_MB_KEY);
  }

  public String runtime() {
    return environment.getProperty(GAE_RUNTIME_KEY);
  }

  public String service() {
    return environment.getProperty(GAE_SERVICE_KEY);
  }

  public String version() {
    return environment.getProperty(GAE_VERSION_KEY);
  }

  public String projectId() {
    return environment.getProperty(GCP_PROJECT_ID_KEY);
  }

  public String port() {
    return environment.getProperty(PORT_KEY);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}
