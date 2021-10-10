package zlavallee.appengine.tasks.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import zlavallee.appengine.tasks.core.payload.TestPayloadOne;
import zlavallee.appengine.tasks.core.payload.TestPayloadTwo;

class PayloadMetadataTest {

  @Test
  public void testIsNotValid() {
    PayloadMetadata payloadMetadata = PayloadMetadata.valueOf("Payload object");
    assertFalse(payloadMetadata.isValidPayload());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        payloadMetadata::validate);
    assertThat(exception.getMessage(), containsString("Invalid payload configuration"));
  }

  @Test
  public void isValid() {
    PayloadMetadata payloadMetadata = new PayloadMetadata(TestPayloadOne.class);

    payloadMetadata.validate();
    assertTrue(payloadMetadata.isValidPayload());
    assertEquals("payloadOne", payloadMetadata.getTaskName());
    assertEquals(TasksSettings.HANDLER_URL + "/payloadOne", payloadMetadata.getRelativeTaskUrl());
  }

  @Test
  public void testNullObject() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> PayloadMetadata.valueOf(null));

    assertEquals("Object must not be null", exception.getMessage());
  }

  @Test
  public void testEquality() {
    assertEquals(new PayloadMetadata(TestPayloadOne.class),
        new PayloadMetadata(TestPayloadOne.class));
    assertNotEquals(new PayloadMetadata(TestPayloadOne.class),
        new PayloadMetadata(TestPayloadTwo.class));
  }
}
