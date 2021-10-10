package zlavallee.appengine.core.admin;

public class UnrecoverableGoogleApiException extends RuntimeException {

  UnrecoverableGoogleApiException(String message, Throwable cause) {
    super(message, cause);
  }

  UnrecoverableGoogleApiException(Throwable cause) {
    super(cause);
  }
}
