package zlavallee.appengine.core.admin;

public interface GoogleClientRequestExecutor {

  <T> T execute(GoogleClientRequestSupplier<T> supplier);
}
