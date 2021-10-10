package zlavallee.appengine.core.admin;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import java.io.IOException;

public interface GoogleClientRequestSupplier<T> {

  AbstractGoogleClientRequest<T> get() throws IOException;
}
