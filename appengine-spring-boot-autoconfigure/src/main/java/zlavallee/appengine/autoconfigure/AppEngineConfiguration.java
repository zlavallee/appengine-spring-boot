package zlavallee.appengine.autoconfigure;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.appengine.v1.Appengine;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zlavallee.appengine.core.AppEngineEnvironment;

@Configuration
@ConditionalOnProperty(name= AppEngineEnvironment.APPLICATION_ID_KEY)
@ConditionalOnMissingBean(Appengine.class)
public class AppEngineConfiguration {

  @Bean
  public Appengine appengine() throws GeneralSecurityException, IOException {
    return new Appengine.Builder(
        httpTransport(),
        GsonFactory.getDefaultInstance(),
        httpRequestInitializer())
        .build();
  }

  @Bean
  public HttpTransport httpTransport() throws GeneralSecurityException, IOException {
    return GoogleNetHttpTransport.newTrustedTransport();
  }

  private HttpRequestInitializer httpRequestInitializer() throws IOException {
    return new HttpCredentialsAdapter(ServiceAccountCredentials.getApplicationDefault());
  }
}