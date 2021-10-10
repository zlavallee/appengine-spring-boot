package zlavallee.appengine.core.admin;

import java.io.IOException;

public class DefaultGoogleClientRequestExecutor implements GoogleClientRequestExecutor {

  @Override
  public <T> T execute(GoogleClientRequestSupplier<T> supplier) {
    try {
      return supplier.get().execute();
    } catch (IOException e) {
      throw new UnrecoverableGoogleApiException(e);
    }
  }
}
