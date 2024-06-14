package io.maju.frosty.product;

import io.maju.frosty.UnitTest;
import io.maju.frosty.exceptions.DomainException;
import io.maju.frosty.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class ProductTest extends UnitTest {

    @Test
    void givenAValidParams_whenCallNewProduct_thenInstantiateAProduct() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedExpirationDate = "2024-12-31";
        final var expectedIsActive = true;

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                LocalDate.parse(expectedExpirationDate),
                expectedStock
        );

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate().toString());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidParams_whenCallNewProduct_thenReturnAProductCopy() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedExpirationDate = "2024-12-31";
        final var expectedIsActive = true;

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                LocalDate.parse(expectedExpirationDate),
                expectedStock
        );

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate().toString());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNull(actualProduct.deletedAt());

        final var copyProduct = Product.with(actualProduct).deactivate();

        Assertions.assertEquals(actualProduct, copyProduct);
        Assertions.assertEquals(actualProduct.getId(), copyProduct.getId());
        Assertions.assertEquals(actualProduct.name(), copyProduct.name());
        Assertions.assertEquals(actualProduct.description(), copyProduct.description());
        Assertions.assertEquals(actualProduct.price(), copyProduct.price());
        Assertions.assertEquals(actualProduct.stock(), copyProduct.stock());
        Assertions.assertEquals(actualProduct.expirationDate(), copyProduct.expirationDate());
        Assertions.assertNotEquals(actualProduct.isActive(), copyProduct.isActive());
        Assertions.assertEquals(actualProduct.createdAt(), copyProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isBefore(copyProduct.updatedAt()));
        Assertions.assertNotEquals(actualProduct.deletedAt(), copyProduct.deletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualProduct = Product.newProduct(
                null,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                LocalDate.now(),
                expectedStock
        );
        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualProduct.validate(validationHandler));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualProduct = Product.newProduct(
                "",
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                LocalDate.now(),
                expectedStock
        );
        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualProduct.validate(validationHandler));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final var expectedName = "So";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                LocalDate.now(),
                expectedStock
        );
        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualProduct.validate(validationHandler));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAnInvalidNameLengthGreaterThan255_whenCallNewProductAndValidate_thenShouldReceiveError() {
        final var expectedName = """
                 Uma das piores coisas que podem acontecer é o sorvete estar duro feito pedra quando você tenta pegar um\s
                 pouco para se refrescar. Mas calma que tem algumas coisas que você pode fazer pra evitar isso. Guardar o
                 pote de sorvete dentro de um saco plástico vedado, manter o pote no fundo do congelador onde a variação
                 de temperatura é menor, e manter os potes bem fechados vai ajudar a manter o sorvete macio e pronto\s
                 para consumo. Se ainda assim o sorvete endurecer, retire ele do congelador quinze minutos antes de\s
                 servir para ele amolecer.
                \s""";

        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                LocalDate.now(),
                expectedStock
        );

        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualProduct.validate(validationHandler));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    void givenAValidEmptyDescription_whenCallNewProductAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Sorvete";
        final var expectedDescription = " ";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;
        final var expectedExpirationDate = LocalDate.now();

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );
        final var validationHandler = new ThrowsValidationHandler();

        Assertions.assertDoesNotThrow(() -> actualProduct.validate(validationHandler));

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidNullDescription_whenCallNewProductAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Sorvete";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;
        final var expectedExpirationDate = LocalDate.now();

        final var actualProduct = Product.newProduct(
                expectedName,
                null,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );
        final var validationHandler = new ThrowsValidationHandler();

        Assertions.assertDoesNotThrow(() -> actualProduct.validate(validationHandler));

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertNull(actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidNullStock_whenCallNewProductAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedExpirationDate = LocalDate.now();

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                null
        );
        final var validationHandler = new ThrowsValidationHandler();

        Assertions.assertDoesNotThrow(() -> actualProduct.validate(validationHandler));

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertNull(actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidNullExpirationDate_whenCallNewProductAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedIsActive = true;
        final var expectedStock = 10;

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                null,
                expectedStock
        );
        final var validationHandler = new ThrowsValidationHandler();

        Assertions.assertDoesNotThrow(() -> actualProduct.validate(validationHandler));

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertNull(actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidFalseIsActive_whenCallNewProductAndValidate_thenShouldNotReceiveError() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = false;
        final var expectedExpirationDate = LocalDate.now();

        final var actualProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> actualProduct.validate(validationHandler));

        Assertions.assertNotNull(actualProduct);
        Assertions.assertNotNull(actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertNotNull(actualProduct.createdAt());
        Assertions.assertNotNull(actualProduct.updatedAt());
        Assertions.assertNotNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidActiveProduct_whenCallDeactivate_thenReturnAnProductInactivated() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = false;
        final var expectedExpirationDate = LocalDate.now();

        final var aProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                true,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        final var createdAt = aProduct.createdAt();
        final var updatedAt = aProduct.updatedAt();

        Assertions.assertTrue(aProduct.isActive());
        Assertions.assertNull(aProduct.deletedAt());

        final var actualProduct = aProduct.deactivate();

        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(createdAt, actualProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidInactiveProduct_whenCallActivate_thenReturnAnProductActivated() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = true;
        final var expectedExpirationDate = LocalDate.now();

        final var aProduct = Product.newProduct(
                expectedName,
                expectedDescription,
                false,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        final var createdAt = aProduct.createdAt();
        final var updatedAt = aProduct.updatedAt();

        Assertions.assertFalse(aProduct.isActive());
        Assertions.assertNotNull(aProduct.deletedAt());

        final var actualProduct = aProduct.activate();

        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(createdAt, actualProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidProduct_whenCallUpdate_thenReturnProductUpdated() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = true;
        final var expectedExpirationDate = LocalDate.now();

        final var aProduct = Product.newProduct(
                "Sorvete sem sabor",
                "Sorvete de sem sabor e sem gosto",
                expectedIsActive,
                new BigDecimal("5.0"),
                expectedExpirationDate.minusMonths(2),
                9
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        final var createdAt = aProduct.createdAt();
        final var updatedAt = aProduct.updatedAt();

        final var actualProduct = aProduct.update(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );

        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(createdAt, actualProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidProduct_whenCallUpdateWithNullStock_thenReturnProductUpdated() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = true;
        final var expectedExpirationDate = LocalDate.now();

        final var aProduct = Product.newProduct(
                "Sorvete sem sabor",
                "Sorvete de sem sabor e sem gosto",
                expectedIsActive,
                new BigDecimal("5.0"),
                expectedExpirationDate.minusMonths(2),
                expectedStock
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        final var createdAt = aProduct.createdAt();
        final var updatedAt = aProduct.updatedAt();

        final var actualProduct = aProduct.update(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                null
        );

        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(createdAt, actualProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidProduct_whenCallUpdateWithNullExpirationDate_thenReturnProductUpdated() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = true;
        final var expectedExpirationDate = LocalDate.now();

        final var aProduct = Product.newProduct(
                "Sorvete sem sabor",
                "Sorvete de sem sabor e sem gosto",
                expectedIsActive,
                new BigDecimal("5.0"),
                expectedExpirationDate,
                expectedStock
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        final var createdAt = aProduct.createdAt();
        final var updatedAt = aProduct.updatedAt();

        final var actualProduct = aProduct.update(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                null,
                expectedStock
        );

        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertNull(actualProduct.expirationDate());
        Assertions.assertEquals(expectedIsActive, actualProduct.isActive());
        Assertions.assertEquals(createdAt, actualProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualProduct.deletedAt());
    }

    @Test
    void givenAValidProduct_whenCallUpdateToInactive_thenReturnProductUpdated() {
        final var expectedName = "Sorvete";
        final var expectedDescription = "Sorvete de chocolate";
        final var expectedPrice = new BigDecimal("10.0");
        final var expectedStock = 10;
        final var expectedIsActive = false;
        final var expectedExpirationDate = LocalDate.now();

        final var aProduct = Product.newProduct(
                "Sorvete sem sabor",
                "Sorvete de sem sabor e sem gosto",
                true,
                new BigDecimal("5.0"),
                expectedExpirationDate.minusMonths(2),
                9
        );

        final var validationHandler = new ThrowsValidationHandler();
        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        final var createdAt = aProduct.createdAt();
        final var updatedAt = aProduct.updatedAt();

        final var actualProduct = aProduct.update(
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedPrice,
                expectedExpirationDate,
                expectedStock
        );

        Assertions.assertDoesNotThrow(() -> aProduct.validate(validationHandler));

        Assertions.assertEquals(aProduct.getId(), actualProduct.getId());
        Assertions.assertEquals(expectedName, actualProduct.name());
        Assertions.assertEquals(expectedDescription, actualProduct.description());
        Assertions.assertEquals(expectedPrice, actualProduct.price());
        Assertions.assertEquals(expectedStock, actualProduct.stock());
        Assertions.assertEquals(expectedExpirationDate, actualProduct.expirationDate());
        Assertions.assertFalse(actualProduct.isActive());
        Assertions.assertEquals(createdAt, actualProduct.createdAt());
        Assertions.assertTrue(actualProduct.updatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualProduct.deletedAt());
    }
}
