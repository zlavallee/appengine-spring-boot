package zlavallee.appengine.tasks.core;

import com.google.cloud.tasks.v2.QueueName;

public interface QueueConfiguration {

  QueueName getQueueName(String taskName);
}
