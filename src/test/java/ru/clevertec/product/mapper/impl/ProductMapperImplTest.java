package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductMapperImplTest {

    private ProductMapperImpl productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
    }

    @ParameterizedTest
    @CsvSource({
            "My product, My description, 1",
            "My product 1, My description 1, 2",
            "My product 2, My description 2, 3"
    })
    void toProductShouldReturnProductCheckEqualsFields(String name, String description, int price) {
        //given
        ProductDto productDto = new ProductDto(name,
                description, BigDecimal.valueOf(price));
        Product expected = new Product(null, name,
                description, BigDecimal.valueOf(price), null);
        //when
        Product actual = productMapper.toProduct(productDto);
        //then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice());
    }

    @ParameterizedTest
    @CsvSource({
            "My product, My description, 1",
            "My product 1, My description 1, 2",
            "My product 2, My description 2, 3"
    })
    void toProductShouldReturnProductWhichExist(String name, String description, int price) {
        //given
        ProductDto productDto = new ProductDto(name,
                description, BigDecimal.valueOf(price));
        //when
        Product actual = productMapper.toProduct(productDto);
        //then
        assertNotNull(actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsProduct")
    void toInfoProductDtoShouldReturnObjectInfoProductDto(Product product) {
        //given
        InfoProductDto expected = new InfoProductDto(product.getUuid(), product.getName(), product.getDescription(), product.getPrice());
        //when
        InfoProductDto actual = productMapper.toInfoProductDto(product);
        //then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsProduct")
    void mergeShouldReturnUpdateProductWithNewNameAndDescription(Product product) {
        //given
        ProductDto productDto = new ProductDto("New product name",
                "New description product", product.getPrice());
        Product expected = new Product(product.getUuid(), productDto.name(), productDto.description(),
                productDto.price(), product.getCreated());
        //when
        Product actual = productMapper.merge(product, productDto);
        //then
        assertEquals(expected, actual);
    }

    public static Stream<Arguments> provideArgumentsProduct() {
        return Stream.of(
                Arguments.of(new Product(UUID.fromString("ee892366-b605-4745-9515-ee7cdd0cebeb"),
                                "My product", "My description", BigDecimal.valueOf(1), LocalDateTime.MIN),
                        new Product(UUID.fromString("d303c1e9-f3da-45e0-9d25-0a9d39336fd3"),
                                "My product 1", "My description 1", BigDecimal.valueOf(2), LocalDateTime.MIN),
                        new Product(UUID.fromString("2a5304d2-d4e1-423f-a209-945167d57980"),
                                "My product 2", "My description 2", BigDecimal.valueOf(3), LocalDateTime.MIN))
        );
    }

}