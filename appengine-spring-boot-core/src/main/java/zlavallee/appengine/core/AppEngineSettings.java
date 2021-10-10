package zlavallee.appengine.core;

public interface AppEngineSettings {

  String getProjectId();

  String getApplicationId();

  String getLocationId();

  boolean isAppEngine();
}
