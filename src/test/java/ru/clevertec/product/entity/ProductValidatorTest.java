package ru.clevertec.product.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductValidatorTest {

    private ProductValidator productValidator;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator();
    }

    @Test
    void checkNameShouldReturnFalseWhenNameIsNull() {
        // given
        String name = (String) null;

        // when
        boolean expected = productValidator.checkName(name);

        // then
        assertFalse(expected);
    }

    @Test
    void checkNameShouldReturnFalseWhenNameIsEmpty() {
        // given
        String name = "";

        // when
        boolean expected = productValidator.checkName(name);

        // then
        assertFalse(expected);
    }

    @Test
    void checkNameShouldReturnFalseWhenNameHasLengthMoreThanTenSymbols() {
        // given
        String name = "Мой продукт";

        // when
        boolean expected = productValidator.checkName(name);

        // then
        assertFalse(expected);
    }

    @Test
    void checkNameShouldReturnFalseWhenNameHasLengthLessThanFiveSymbols() {
        // given
        String name = "Мой";

        // when
        boolean expected = productValidator.checkName(name);

        // then
        assertFalse(expected);
    }

    @Test
    void checkNameShouldReturnFalseWhenNameHasUnacceptableSymbols() {
        // given
        String name = "Product";

        // when
        boolean expected = productValidator.checkName(name);

        // then
        assertFalse(expected);
    }

    @Test
    void checkNameShouldReturnTrueWhenNameIsCorrected() {
        // given
        String name = "продукт";

        // when
        boolean expected = productValidator.checkName(name);

        // then
        assertTrue(expected);
    }

    @Test
    void checkDescriptionShouldReturnTrueWhenDescriptionIsNull() {
        // given
        String description = null;

        // when
        boolean expected = productValidator.checkDescription(description);

        // then
        assertTrue(expected);
    }

    @Test
    void checkDescriptionShouldReturnFalseWhenDescriptionHasLengthMoreThanThirtySymbols() {
        // given
        String description = "Мое очень длинное описание товара для проверки валидации";

        // when
        boolean expected = productValidator.checkDescription(description);

        // then
        assertFalse(expected);
    }

    @Test
    void checkDescriptionShouldReturnFalseWhenDescriptionHasLengthLessThanTenSymbols() {
        // given
        String description = "Короткое";

        // when
        boolean expected = productValidator.checkDescription(description);

        // then
        assertFalse(expected);
    }

    @Test
    void checkDescriptionShouldReturnFalseWhenDescriptionHasUnacceptableSymbols() {
        // given
        String description = "My product!!";

        // when
        boolean expected = productValidator.checkDescription(description);

        // then
        assertFalse(expected);
    }

    @Test
    void checkDescriptionShouldReturnTrueWhenDescriptionIsNotNullAndCorrected() {
        // given
        String description = "Мой продукт";

        // when
        boolean expected = productValidator.checkDescription(description);

        // then
        assertTrue(expected);
    }


    @Test
    void checkPriceShouldReturnFalseWhenValueIsNegative() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1);

        // when
        boolean actual = productValidator.checkPrice(price);

        // then
        assertFalse(actual);
    }

    @Test
    void checkPriceShouldReturnTrueWhenValueIsPositiveAndNotNull() {
        // given
        BigDecimal price = BigDecimal.valueOf(1);

        // when
        boolean actual = productValidator.checkPrice(price);

        // then
        assertTrue(actual);
    }

    @Test
    void checkPriceShouldReturnFalseWhenValueIsNull() {
        // given
        BigDecimal price = null;

        // when
        boolean actual = productValidator.checkPrice(price);

        // then
        assertFalse(actual);
    }

    @Test
    void checkCreatedShouldReturnFalseWhenTimeIsNull() {
        // given
        LocalDateTime created = null;

        // when
        boolean actual = productValidator.checkCreated(created);

        // then
        assertFalse(actual);
    }

    @Test
    void checkCreatedShouldReturnTrueWhenTimeIsNotNull() {
        // given
        LocalDateTime created = LocalDateTime.MIN;

        // when
        boolean actual = productValidator.checkCreated(created);

        // then
        assertTrue(actual);
    }

    @Test
    void checkValidationShouldReturnProductWhenAllFieldsCorrected() {
        // given
        Product expected = new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                "Продукт", "Описание продукта", BigDecimal.valueOf(1), LocalDateTime.MIN);

        // when
        Product actual = productValidator.checkValidation(expected);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideProductArguments")
    void checkValidationShouldReturnNullWhenOneOfFieldsUncorrected(Product product) {
        // given

        // when
        Product actual = productValidator.checkValidation(product);

        // then
        assertNull(actual);
    }

    public static Stream<Arguments> provideProductArguments() {
        return Stream.of(
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        null, "Описание продукта", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "", "Описание продукта", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Мой продукт", "Описание продукта", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Мой", "Описание продукта", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Product", "Описание продукта", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", null, BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "Мое очень длинное описание товара для проверки валидации",
                        BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "Короткое", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "My product!!", BigDecimal.valueOf(1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "Описание продукта", BigDecimal.valueOf(-1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "Описание продукта", null, LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "Описание продукта", BigDecimal.valueOf(1), null)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Мой продукт", "Коротко", BigDecimal.valueOf(-1), null)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Продукт", "Коротко", BigDecimal.valueOf(-1), LocalDateTime.MIN)),
                Arguments.of(new Product(UUID.fromString("b5028e80-bd7f-463d-9c1f-5e831da5ebef"),
                        "Product", "My description", BigDecimal.valueOf(1), LocalDateTime.MIN))
        );
    }
}