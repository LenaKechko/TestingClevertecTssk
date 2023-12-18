package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.utils.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryProductRepositoryTest {

    public InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Test
    void findByIdShouldReturnOptionalProduct() {
        // given
        UUID uuid = ProductTestData.builder().build().getUuid();
        Product excepted = new Product(uuid, "Продукт", "Описание продукта",
                BigDecimal.valueOf(1), LocalDateTime.MAX);
        productRepository.save(excepted);

        // when
        Product actual = productRepository.findById(uuid).orElseThrow();

        // then
        assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void findByIdShouldReturnProductEqualsWithUUID(Product product) {
        // given
        Product expected = productRepository.save(product);

        // when
        Product actual = productRepository.findById(expected.getUuid()).orElseThrow();

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid());
    }

    @Test
    void findByIdShouldReturnOptionalEmpty() {
        // given
        UUID uuid = UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066297");
        Optional<Product> excepted = Optional.empty();

        // when
        Optional<Product> actual = productRepository.findById(uuid);

        // then
        assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForFindAll")
    void findAllShouldReturnListProducts(List<Product> products) {
        // given
        List<Product> expected =
                products.stream()
                        .map(product -> productRepository.save(product))
                        .toList();

        // when
        List<Product> actual = productRepository.findAll();

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForFindAll")
    void findAllShouldReturnListIsNotEmpty(List<Product> products) {
        // given
        products.forEach(product -> productRepository.save(product));

        // when
        int actual = productRepository.findAll().size();

        // then
        assertThat(actual).isNotZero();
    }

    @Test
    void findAllShouldReturnListIsEmpty() {
        // given

        // when
        List<Product> actual = productRepository.findAll();

        // then
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void saveShouldReturnSavingProduct(Product expected) {
        // given

        // when
        Product actual = productRepository.save(expected);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void saveShouldReturnSavingProductCheckReturningUUID(Product product) {
        // given
        UUID excepted = product.getUuid();

        // when
        Product actual = productRepository.save(product);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, excepted);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void deleteShouldReturnSuccessByExistUUID(Product product) {
        // given
        Optional<Product> excepted = Optional.empty();
        productRepository.save(product);

        // when
        productRepository.delete(product.getUuid());

        // then
        Optional<Product> actual = productRepository.findById(product.getUuid());
        assertEquals(excepted, actual);
    }

    public static Stream<Arguments> provideArgumentsForFindAll() {
        return Stream.of(
                Arguments.of(Arrays
                        .asList(new Product(UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066296"), "Продукт", "Описание", BigDecimal.valueOf(1), LocalDateTime.MAX),
                                new Product(UUID.fromString("e495840a-b4ab-40c9-8200-8ed6117ad1a5"), "Мой прод", "Это описание", BigDecimal.valueOf(2), LocalDateTime.MAX),
                                new Product(UUID.fromString("93af5278-cd77-4c51-bfd7-6ab28e6f7f5f"), "Еще прод", "Описание продукта", BigDecimal.valueOf(3), LocalDateTime.MAX)))
        );
    }

    public static Stream<Arguments> provideProductsForTesting() {
        return Stream.of(
                Arguments.of(new Product(UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066296"), "Продукт", "Описание", BigDecimal.valueOf(1), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("e495840a-b4ab-40c9-8200-8ed6117ad1a5"), "Мой прод", "Это описание", BigDecimal.valueOf(2), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("93af5278-cd77-4c51-bfd7-6ab28e6f7f5f"), "Еще прод", "Описание продукта", BigDecimal.valueOf(3), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("f3f685e2-a0dd-47d8-9da7-d9ac3c64ef09"), "Продукт 1", "Описание 1", BigDecimal.valueOf(11), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("080bf8e1-7d88-44a9-952c-12969bf8f7e2"), "Мой прод 1", "Это описание 1", BigDecimal.valueOf(21), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("7a041a44-9f86-4d1c-96d0-f390a47054fc"), "Еще прод 1", "Описание продукта 1", BigDecimal.valueOf(31), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("39ed88c3-58d9-4153-93ce-4d2c951df27c"), "Продукт 2", "Описание 2", BigDecimal.valueOf(12), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("328f7a38-f484-454d-b1c1-f010adef4ef9"), "Мой прод 2", "Это описание 2", BigDecimal.valueOf(22), LocalDateTime.MAX)),
                Arguments.of(new Product(UUID.fromString("ee892366-b605-4745-9515-ee7cdd0cebeb"), "ЕЯще прод 2", "Описание продукта 2", BigDecimal.valueOf(32), LocalDateTime.MAX))
        );
    }
}