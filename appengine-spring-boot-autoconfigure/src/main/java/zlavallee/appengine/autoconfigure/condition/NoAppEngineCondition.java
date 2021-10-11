package zlavallee.appengine.autoconfigure.condition;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import zlavallee.appengine.core.AppEngineEnvironment;

public class NoAppEngineCondition extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext context,
      AnnotatedTypeMetadata metadata) {

    boolean isAppEngine =
        context.getEnvironment()
            .getProperty(AppEngineEnvironment.APPLICATION_ID_KEY) != null;

    if (isAppEngine) {
      return ConditionOutcome.noMatch("App engine environment detected");
    }

    return ConditionOutcome.match();
  }
}