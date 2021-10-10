package zlavallee.appengine.tasks.core;

import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.Map;

public class CloudTasksUtils {

  private static final Map<String, String> location =
      Map.of("europe-west", "europe-west1", "us-central", "us-central1");

  public static String getCloudTasksLocation(String appEngineLocationId) {
    return location.getOrDefault(appEngineLocationId, appEngineLocationId);
  }

  public static Duration toDuration(java.time.Duration duration) {
    return Duration.newBuilder()
        .setSeconds(duration.getSeconds())
        .setNanos(duration.getNano())
        .build();
  }

  public static Timestamp toTimestamp(Instant instant) {
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }
}
