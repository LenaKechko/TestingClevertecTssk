package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryProductRepositoryTest {

    public InMemoryProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
    }

    @Test
    void findByIdShouldReturnOptionalProduct() {
        //given
        UUID uuid = UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066296");
        Product excepted = new Product(uuid, "My product", "My description",
                BigDecimal.valueOf(1), LocalDateTime.MAX);
        productRepository.save(excepted);

        //when
        Product actual = productRepository.findById(uuid).orElseThrow();

        //then
        assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void findByIdShouldReturnProductEqualsWithUUID(Product product) {
        //given
        Product expected = productRepository.save(product);

        //when
        Product actual = productRepository.findById(expected.getUuid()).orElseThrow();

        //then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.getUuid());
    }

    @Test
    void findByIdShouldReturnOptionalEmpty() {
        //given
        UUID uuid = UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066297");
        Optional<Product> excepted = Optional.empty();

        //when
        Optional<Product> actual = productRepository.findById(uuid);

        //then
        assertEquals(excepted, actual);
    }


    @ParameterizedTest
    @MethodSource("provideArgumentsForFindAll")
    void findAllShouldReturnListProducts(List<Product> products) {
        //given
        List<Product> expected =
                products.stream()
                        .map(product -> productRepository.save(product))
                        .toList();

        //when
        List<Product> actual = productRepository.findAll();

        //then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForFindAll")
    void findAllShouldReturnListIsNotEmpty(List<Product> products) {
        //given
        products.forEach(product -> productRepository.save(product));
        //when
        int actual = productRepository.findAll().size();
        //then
        assertThat(actual).isNotZero();
    }

    @Test
    void findAllShouldReturnListIsEmpty() {
        //given

        //when
        List<Product> actual = productRepository.findAll();
        //then
        assertNull(actual);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void saveShouldReturnSavingProduct(Product product) {
        //given
        Product excepted = product;

        //when
        Product actual = productRepository.save(product);

        //then
        assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void saveShouldReturnSavingProductCheckReturningUUID(Product product) {
        //given
        UUID excepted = product.getUuid();

        //when
        Product actual = productRepository.save(product);

        //then
        assertThat(actual).hasFieldOrPropertyWithValue(Product.Fields.uuid, excepted);
    }

    @Test
    void saveShouldReturnException() {
        //given
        Product actual = null;

        //when

        //then
        assertThrows(IllegalArgumentException.class,
                () -> productRepository.save(actual));
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTesting")
    void deleteExistUUIDShouldReturnSuccess(Product product) {
        //given
        Optional<Product> excepted = Optional.empty();
        productRepository.save(product);
        //when
        productRepository.delete(product.getUuid());
        //then
        Optional<Product> actual = productRepository.findById(product.getUuid());
        assertEquals(excepted, actual);
    }

    public static Stream<Arguments> provideArgumentsForFindAll() {
        return Stream.of(
                Arguments.of(Arrays
                        .asList(new Product(UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066296"), "My product", "My description", BigDecimal.valueOf(1), LocalDateTime.MAX),
                                new Product(UUID.fromString("e495840a-b4ab-40c9-8200-8ed6117ad1a5"), "Another product", "It's description", BigDecimal.valueOf(2), LocalDateTime.MAX),
                                new Product(UUID.fromString("93af5278-cd77-4c51-bfd7-6ab28e6f7f5f"), "One more product", "His description", BigDecimal.valueOf(3), LocalDateTime.MAX))),
                Arguments.of(Arrays
                        .asList(new Product(UUID.fromString("f3f685e2-a0dd-47d8-9da7-d9ac3c64ef09"), "My product 1", "My description 1", BigDecimal.valueOf(11), LocalDateTime.MAX),
                                new Product(UUID.fromString("080bf8e1-7d88-44a9-952c-12969bf8f7e2"), "Another product 1", "It's description 1", BigDecimal.valueOf(21), LocalDateTime.MAX),
                                new Product(UUID.fromString("7a041a44-9f86-4d1c-96d0-f390a47054fc"), "One more product 1", "His description 1", BigDecimal.valueOf(31), LocalDateTime.MAX))),
                Arguments.of(Arrays
                        .asList(new Product(UUID.fromString("39ed88c3-58d9-4153-93ce-4d2c951df27c"), "My product 2", "My description 2", BigDecimal.valueOf(12), LocalDateTime.MAX),
                                new Product(UUID.fromString("328f7a38-f484-454d-b1c1-f010adef4ef9"), "Another product 2", "It's description 2", BigDecimal.valueOf(22), LocalDateTime.MAX),
                                new Product(UUID.fromString("ee892366-b605-4745-9515-ee7cdd0cebeb"), "One more product 2", "His description 2", BigDecimal.valueOf(32), LocalDateTime.MAX)))
        );
    }

    public static Stream<Arguments> provideProductsForTesting() {
        return Stream.of(
                Arguments.of(Arrays
                        .asList(new Product(UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066296"), "My product", "My description", BigDecimal.valueOf(1), LocalDateTime.MAX),
                                new Product(UUID.fromString("e495840a-b4ab-40c9-8200-8ed6117ad1a5"), "Another product", "It's description", BigDecimal.valueOf(2), LocalDateTime.MAX),
                                new Product(UUID.fromString("93af5278-cd77-4c51-bfd7-6ab28e6f7f5f"), "One more product", "His description", BigDecimal.valueOf(3), LocalDateTime.MAX),
                                new Product(UUID.fromString("f3f685e2-a0dd-47d8-9da7-d9ac3c64ef09"), "My product 1", "My description 1", BigDecimal.valueOf(11), LocalDateTime.MAX),
                                new Product(UUID.fromString("080bf8e1-7d88-44a9-952c-12969bf8f7e2"), "Another product 1", "It's description 1", BigDecimal.valueOf(21), LocalDateTime.MAX),
                                new Product(UUID.fromString("7a041a44-9f86-4d1c-96d0-f390a47054fc"), "One more product 1", "His description 1", BigDecimal.valueOf(31), LocalDateTime.MAX),
                                new Product(UUID.fromString("39ed88c3-58d9-4153-93ce-4d2c951df27c"), "My product 2", "My description 2", BigDecimal.valueOf(12), LocalDateTime.MAX),
                                new Product(UUID.fromString("328f7a38-f484-454d-b1c1-f010adef4ef9"), "Another product 2", "It's description 2", BigDecimal.valueOf(22), LocalDateTime.MAX),
                                new Product(UUID.fromString("ee892366-b605-4745-9515-ee7cdd0cebeb"), "One more product 2", "His description 2", BigDecimal.valueOf(32), LocalDateTime.MAX)))
        );
    }
}