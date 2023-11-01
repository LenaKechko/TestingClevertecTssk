package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.util.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<UUID> productUuidCaptor;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void getShouldReturnInfoProductDtoWithExistUUID() {
        // given
        UUID uuid = ProductTestData.builder().build().getUuid();
        Product productToSave = ProductTestData.builder()
                .withUuid(null)
                .build().buildProduct();
        Product productSaving = ProductTestData.builder()
                .build().buildProduct();
        InfoProductDto expected = ProductTestData.builder()
                .build().buildInfoProductDto();

        when(productRepository.save(productToSave))
                .thenReturn(productSaving);

        when(productRepository.findById(uuid).orElseThrow())
                .thenReturn(productSaving);

        doReturn(expected)
                .when(productMapper)
                .toInfoProductDto(productSaving);
        // when
        InfoProductDto actual = productService.get(uuid);
        // then
        assertEquals(expected, actual);
        verify(productRepository).findById(uuid);
    }

    @Test
    void getShouldReturnProductNotFoundException() {
        // given
        UUID uuid = UUID.fromString("b8003c54-c22b-450a-a0d3-94b646150585");
        Product productToSave = ProductTestData.builder()
                .withUuid(null)
                .build().buildProduct();
        Product productSaving = ProductTestData.builder()
                .build().buildProduct();
        Optional<Product> expectedOptional = Optional.empty();
        Exception expectedException = new ProductNotFoundException(uuid);
        when(productRepository.save(productToSave))
                .thenReturn(productSaving);
        when(productRepository.findById(uuid))
                .thenReturn(expectedOptional);
        doThrow(expectedException).when(productService).get(uuid);

        // when-then
        assertThrows(ProductNotFoundException.class, () -> productService.get(uuid));

        verify(productRepository).findById(productUuidCaptor.capture());
        assertThat(productUuidCaptor.getValue())
                .isNotNull();
    }

    @Test
    void getAllShouldReturnInfoProductDtoList() {
        //given
        List<Product> productList = List.of(
                new Product(UUID.fromString("b8003c54-c22b-450a-a0d3-94b646150584"),
                        "Продукт", "Описание", BigDecimal.valueOf(1), LocalDateTime.MIN),
                new Product(UUID.fromString("ad6aa3ac-8531-4db0-a797-d8c5b6f15d82"),
                        "Продукт 1", "Описание 1", BigDecimal.valueOf(2), LocalDateTime.MIN),
                new Product(UUID.fromString("aff76a7a-dc99-4209-9663-e70c30501942"),
                        "Продукт 2", "Описание 2", BigDecimal.valueOf(3), LocalDateTime.MIN)
        );

        List<InfoProductDto> expectedList = List.of(
                new InfoProductDto(UUID.fromString("b8003c54-c22b-450a-a0d3-94b646150584"),
                        "Продукт", "Описание", BigDecimal.valueOf(1)),
                new InfoProductDto(UUID.fromString("ad6aa3ac-8531-4db0-a797-d8c5b6f15d82"),
                        "Продукт 1", "Описание 1", BigDecimal.valueOf(2)),
                new InfoProductDto(UUID.fromString("aff76a7a-dc99-4209-9663-e70c30501942"),
                        "Продукт 2", "Описание 2", BigDecimal.valueOf(3))
        );

        when(productRepository.findAll()).thenReturn(productList);

        doReturn(expectedList).
                when(productList.stream()
                        .map(product -> productMapper.toInfoProductDto(product))
                        .toList());

        //when
        List<InfoProductDto> actualList = productService.getAll();
        //then
        assertEquals(expectedList, actualList);
        verify(productRepository).findAll();
    }

    @Test
    void createShouldReturnUuidWhenProductSave() {
        //given
        Product productToSave = ProductTestData.builder()
                .withUuid(null)
                .build().buildProduct();
        Product expected = ProductTestData.builder()
                .build().buildProduct();
        ProductDto productDto = ProductTestData.builder()
                .build().buildProductDto();

        doReturn(productToSave)
                .when(productMapper).toProduct(productDto);
        when(productRepository.save(productToSave)).thenReturn(expected);

        UUID expectedUuid = ProductTestData.builder().build().getUuid();

        //when
        UUID actualUuid = productService.create(productDto);
        //then
        assertEquals(expectedUuid, actualUuid);
        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue())
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, null);
    }

    @Test
    void updateShouldUpdateDescriptionAndPriceInProduct() {
        //given
        UUID uuid = ProductTestData.builder().build().getUuid();
        Product productToUpdate = ProductTestData.builder()
                .build().buildProduct();
        ProductDto productDtoToUpdate = ProductTestData.builder()
                .withDescription("My new description")
                .withPrice(BigDecimal.valueOf(4))
                .build().buildProductDto();
        Product excepted = ProductTestData.builder()
                .withDescription("My new description")
                .withPrice(BigDecimal.valueOf(4))
                .build().buildProduct();

        when(productRepository.findById(uuid).orElseThrow())
                .thenReturn(productToUpdate);

        doReturn(excepted)
                .when(productMapper).merge(productToUpdate, productDtoToUpdate);

        //when
        productService.update(uuid, productDtoToUpdate);
        //then
        verify(productMapper).merge(productToUpdate, productDtoToUpdate);
        verify(productRepository).save(productToUpdate);

        verify(productMapper).merge(productCaptor.capture(), productDtoToUpdate);
        assertNotNull(productCaptor.getValue());

        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue())
                .hasFieldOrPropertyWithValue(Product.Fields.description, excepted.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, excepted.getPrice());
        assertEquals(excepted, productCaptor.getValue());
    }

    @Test
    void deleteShouldDeleteProductUsingUuid() {
        //given
        UUID uuid = ProductTestData.builder().build().getUuid();
        //when
        productService.delete(uuid);
        //then
        verify(productRepository).delete(uuid);
    }
}