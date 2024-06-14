package io.maju.frosty.checkin;

import io.maju.frosty.UnitTest;
import io.maju.frosty.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class ChekinOrderTest extends UnitTest {

    @Test
    void givenAValidParams_whenCallNewCheckinOrder_shouldInstantiateAnOrder() {
        final var expectedTotal = new BigDecimal("21.50");
        final var expectedIsCanceled = false;
        final var expectedItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.75"), 1, "123"),
                CheckinOrderItem.with("Item 2", new BigDecimal("10.75"), 1, "456")
        );

        final var actualOrder = CheckinOrder.newOrder(expectedItems, expectedIsCanceled);

        Assertions.assertNotNull(actualOrder);
        Assertions.assertEquals(expectedTotal, actualOrder.total());
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertEquals(expectedIsCanceled, actualOrder.isCanceled());
        Assertions.assertEquals(expectedItems, actualOrder.items());
        Assertions.assertEquals(2, actualOrder.items().size());
        Assertions.assertEquals(expectedItems.getFirst(), actualOrder.items().getFirst());
        Assertions.assertEquals(expectedItems.getLast(), actualOrder.items().getLast());
        Assertions.assertNotNull(actualOrder.createdAt());
        Assertions.assertNotNull(actualOrder.updatedAt());
        Assertions.assertNull(actualOrder.deletedAt());
    }

    @Test
    void givenAnInvalidNullItems_whenCallNewCheckinOrder_shouldReceiveAnError() {
        final var expectedErrorMessage = "'items' should not be null";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(null, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidEmptyItems_whenCallNewCheckinOrder_shouldReceiveAnError() {
        final var expectedOrderItems = List.<CheckinOrderItem>of();

        final var expectedErrorMessage = "'items' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidNullOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = new ArrayList<CheckinOrderItem>();
        expectedOrderItems.add(null);

        final var expectedErrorMessage = "'item[0]' should not be null";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidTwoNullOrderItems_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = new ArrayList<CheckinOrderItem>();
        expectedOrderItems.add(null);
        expectedOrderItems.add(null);

        final var expectedErrorMessageOne = "'item[0]' should not be null";
        final var expectedErrorMessageTwo = "'item[1]' should not be null";
        final var expectedErrorCount = 2;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidEmptyNameOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("", new BigDecimal("10.75"), 1, "123")
        );

        final var expectedErrorMessage = "'item[0].name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidShortNameOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("A", new BigDecimal("10.75"), 1, "123")
        );

        final var expectedErrorMessage = "'item[0].name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidLongNameOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("A".repeat(256), new BigDecimal("10.75"), 1, "123")
        );

        final var expectedErrorMessage = "'item[0].name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidZeroPriceOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", BigDecimal.ZERO, 1, "123")
        );

        final var expectedErrorMessage = "'item[0].price' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidNegativePriceOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("-10.75"), 1, "123")
        );

        final var expectedErrorMessage = "'item[0].price' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidNegativeQuantityOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.75"), -1, "123")
        );

        final var expectedErrorMessage = "'item[0].quantity' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidEmptyProductIdOrderItem_whenCallNewCheckinOrder_shouldReceiveAnError() {

        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.75"), 1, "")
        );

        final var expectedErrorMessage = "'item[0].productId' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CheckinOrder.newOrder(expectedOrderItems, false);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnUncanceledOrder_whenCallCancel_shouldReceiveOK() {
        final var expectedIsCanceled = true;
        final var anOrder = CheckinOrder.newOrder(
                List.of(
                        CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                        CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
                ),
                false
        );

        Assertions.assertFalse(anOrder.isCanceled());
        Assertions.assertNull(anOrder.deletedAt());

        final var actualOrder = anOrder.cancel();

        Assertions.assertNotNull(actualOrder);
        Assertions.assertEquals(expectedIsCanceled, actualOrder.isCanceled());
        Assertions.assertEquals(anOrder.items(), actualOrder.items());
        Assertions.assertEquals(anOrder.total(), actualOrder.total());
        Assertions.assertEquals(anOrder.createdAt(), actualOrder.createdAt());
        Assertions.assertEquals(anOrder.updatedAt(), actualOrder.updatedAt());
        Assertions.assertNotNull(actualOrder.deletedAt());
    }

    @Test
    void givenACanceledOrder_whenCallUncancel_shouldReceiveOK() {
        final var expectedIsCanceled = false;
        final var anOrder = CheckinOrder.newOrder(
                List.of(
                        CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                        CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
                ),
                true
        );

        Assertions.assertTrue(anOrder.isCanceled());
        Assertions.assertNotNull(anOrder.deletedAt());

        final var actualOrder = anOrder.uncancel();

        Assertions.assertNotNull(actualOrder);
        Assertions.assertEquals(expectedIsCanceled, actualOrder.isCanceled());
        Assertions.assertEquals(anOrder.items(), actualOrder.items());
        Assertions.assertEquals(anOrder.total(), actualOrder.total());
        Assertions.assertEquals(anOrder.createdAt(), actualOrder.createdAt());
        Assertions.assertEquals(anOrder.updatedAt(), actualOrder.updatedAt());
        Assertions.assertNull(actualOrder.deletedAt());
    }

    @Test
    void givenAValidCanceledOrder_whenCallUpdateWithUncanceled_shouldReceiveOrderUpdated() {
        final var expectedIsCanceled = false;
        final var expectedTotal = BigDecimal.valueOf(21.4);
        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
        );

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                true
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertTrue(actualOrder.isCanceled());
        Assertions.assertNotNull(actualOrder.deletedAt());

        final var actualCreatedAt = actualOrder.createdAt();
        final var actualUpdatedAt = actualOrder.updatedAt();

        actualOrder.update(expectedOrderItems, expectedIsCanceled);

        Assertions.assertNotNull(actualOrder);
        Assertions.assertEquals(expectedIsCanceled, actualOrder.isCanceled());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());
        Assertions.assertEquals(expectedTotal, actualOrder.total());
        Assertions.assertEquals(actualCreatedAt, actualOrder.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualOrder.updatedAt()));
        Assertions.assertNull(actualOrder.deletedAt());
    }

    @Test
    void givenAValidUncanceledOrder_whenCallUpdateWithCanceled_shouldReceiveOrderUpdated() {
        final var expectedIsCanceled = true;
        final var expectedTotal = BigDecimal.valueOf(21.4);
        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
        );

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                false
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());

        final var actualCreatedAt = actualOrder.createdAt();
        final var actualUpdatedAt = actualOrder.updatedAt();

        actualOrder.update(expectedOrderItems, expectedIsCanceled);

        Assertions.assertNotNull(actualOrder);
        Assertions.assertEquals(expectedIsCanceled, actualOrder.isCanceled());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());
        Assertions.assertEquals(expectedTotal, actualOrder.total());
        Assertions.assertEquals(actualCreatedAt, actualOrder.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualOrder.updatedAt()));
        Assertions.assertNotNull(actualOrder.deletedAt());
    }

    @Test
    void givenAnInvalidNullOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
        );

        final var expectedErrorMessage = "'items' should not be empty";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> actualOrder.update(null, expectedIsCanceled)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidEmptyOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
        );

        final var expectedErrorMessage = "'items' should not be empty";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> actualOrder.update(List.of(), expectedIsCanceled)
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidEmptyNameOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123"),
                CheckinOrderItem.with("Item 2", new BigDecimal("10.7"), 1, "456")
        ));

        final var expectedErrorMessage = "'item[2].name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems
                .add(CheckinOrderItem.with(" ", new BigDecimal("10.7"), 1, "789"));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidShortNameOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems
                .add(CheckinOrderItem.with("A", new BigDecimal("10.7"), 1, "789"));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidLongNameOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems.add(
                CheckinOrderItem.with("A".repeat(256), new BigDecimal("10.7"), 1, "789")
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidLassThanZeroPriceOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].price' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems.add(
                CheckinOrderItem.with("M".repeat(3), new BigDecimal("-10.7"), 1, "789")
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidEqualZeroPriceOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].price' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems.add(
                CheckinOrderItem.with("M".repeat(3),BigDecimal.ZERO, 1, "789")
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidZeroQuantityOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].quantity' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems.add(
                CheckinOrderItem.with("M".repeat(3), new BigDecimal("10"), 0, "789")
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidLassThanZeroQuantityOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].quantity' should be greater than or equal to zero";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems.add(
                CheckinOrderItem.with("M".repeat(3), new BigDecimal("10.7"), -1, "789")
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidEmptyProductIdOrderItem_whenCallUpdateOrder_shouldReceiveAnError() {
        final var expectedIsCanceled = false;
        final var expectedOrderItems = new ArrayList<>(List.of(
                CheckinOrderItem.with("Item 1", new BigDecimal("10.7"), 1, "123")
        ));

        final var expectedErrorMessage = "'item[1].productId' should not be empty";
        final var expectedErrorCount = 1;

        final var actualOrder = CheckinOrder.newOrder(
                expectedOrderItems,
                expectedIsCanceled
        );

        Assertions.assertNotNull(actualOrder);
        Assertions.assertFalse(actualOrder.isCanceled());
        Assertions.assertNull(actualOrder.deletedAt());
        Assertions.assertEquals(expectedOrderItems, actualOrder.items());

        expectedOrderItems.add(
                CheckinOrderItem.with("M".repeat(3), new BigDecimal("10.7"), 1, " ")
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualOrder.update(expectedOrderItems, expectedIsCanceled);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }
}
