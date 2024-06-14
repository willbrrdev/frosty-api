package io.maju.frosty.checkin;

import io.maju.frosty.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CheckinOrderItemTest extends UnitTest {

    @Test
    void givenAValidParams_whenCallsNewOrderItem_shouldInstantiateAnOrderItem() {
        // given
        final var expectedId = "123";
        final var expectedName = "Item 1";
        final var expectedPrice = new BigDecimal("10.75");
        final var expectedQuantity = 1;
        final var expectedProductId = "123";

        // when
        final var actualOrderItem =
                CheckinOrderItem.with(expectedId, expectedName, expectedPrice, expectedQuantity, expectedProductId);

        // then
        Assertions.assertNotNull(actualOrderItem);
        Assertions.assertEquals(expectedId, actualOrderItem.id());
        Assertions.assertEquals(expectedName, actualOrderItem.name());
        Assertions.assertEquals(expectedPrice, actualOrderItem.price());
        Assertions.assertEquals(expectedQuantity, actualOrderItem.quantity());
        Assertions.assertEquals(expectedProductId, actualOrderItem.productId());
    }

    @Test
    void givenAnInvalidParams_whenCallsWith_shouldReturnError() {

        final var expectedException = NullPointerException.class;
        Assertions.assertThrows(
                expectedException,
                () -> CheckinOrderItem.with(
                        null, "Item 1", new BigDecimal("10.75"), 1, "123")
        );

        Assertions.assertThrows(
                expectedException,
                () -> CheckinOrderItem.with(
                        "123", null, new BigDecimal("10.75"), 1, "123")
        );

        Assertions.assertThrows(
                expectedException,
                () -> CheckinOrderItem.with(
                        "123", "Item 1", null, 1, "123")
        );

        Assertions.assertThrows(
                expectedException,
                () -> CheckinOrderItem.with(
                        "123", "Item 1", new BigDecimal("10.75"), null, "123")
        );

        Assertions.assertThrows(
                expectedException,
                () -> CheckinOrderItem.with(
                        "123", "Item 1", new BigDecimal("10.75"), 1, null)
        );
    }
}
