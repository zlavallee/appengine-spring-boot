package zlavallee.appengine.core.admin;

public class GoogleClientRequestExecutors {

  static GoogleClientRequestExecutor defaultExecutor() {
    return new DefaultGoogleClientRequestExecutor();
  }
}
