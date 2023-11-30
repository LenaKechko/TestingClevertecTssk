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
import ru.clevertec.product.entity.ProductValidator;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.utils.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void getShouldReturnInfoProductDtoWithExistUUID() {
        // given
        UUID uuid = ProductTestData.builder().build().getUuid();
        Product productSaving = ProductTestData.builder()
                .build().buildProduct();
        InfoProductDto expected = ProductTestData.builder()
                .build().buildInfoProductDto();

        when(productRepository.findById(uuid))
                .thenReturn(Optional.of(productSaving));

        when(productMapper.toInfoProductDto(productSaving))
                .thenReturn(expected);

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

        // when
        when(productRepository.findById(uuid))
                .thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.get(uuid));
        verify(productRepository).findById(uuid);
    }

    @Test
    void getAllShouldReturnInfoProductDtoList() {
        // given
        List<Product> productList = List.of(
                ProductTestData.builder().build().buildProduct(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("ad6aa3ac-8531-4db0-a797-d8c5b6f15d82"))
                        .withName("Продукт 1")
                        .withDescription("Описание 1")
                        .withPrice(BigDecimal.valueOf(2))
                        .withCreated(LocalDateTime.MIN)
                        .build().buildProduct(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("aff76a7a-dc99-4209-9663-e70c30501942"))
                        .withName("Продукт 2")
                        .withDescription("Описание 2")
                        .withPrice(BigDecimal.valueOf(3))
                        .withCreated(LocalDateTime.MIN)
                        .build().buildProduct()
        );

        List<InfoProductDto> expectedList = List.of(
                ProductTestData.builder().build().buildInfoProductDto(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("ad6aa3ac-8531-4db0-a797-d8c5b6f15d82"))
                        .withName("Продукт 1")
                        .withDescription("Описание 1")
                        .withPrice(BigDecimal.valueOf(2))
                        .build().buildInfoProductDto(),
                ProductTestData.builder()
                        .withUuid(UUID.fromString("aff76a7a-dc99-4209-9663-e70c30501942"))
                        .withName("Продукт 2")
                        .withDescription("Описание 2")
                        .withPrice(BigDecimal.valueOf(3))
                        .build().buildInfoProductDto()
        );

        when(productRepository.findAll()).thenReturn(productList);

        IntStream.range(0, productList.size())
                .forEach(index ->
                        doReturn(expectedList.get(index))
                                .when(productMapper).toInfoProductDto(productList.get(index)));

        // when
        List<InfoProductDto> actualList = productService.getAll();

        // then
        assertEquals(expectedList, actualList);
    }

    @Test
    void createShouldReturnUuidWhenProductSave() {
        // given
        Product productToSave = ProductTestData.builder()
                .withUuid(null)
                .build().buildProduct();
        Product expected = ProductTestData.builder()
                .build().buildProduct();
        ProductDto productDto = ProductTestData.builder()
                .build().buildProductDto();

        when(productValidator.checkValidation(productDto))
                .thenReturn(true);

        when(productMapper.toProduct(productDto))
                .thenReturn(productToSave);

        when(productRepository.save(productToSave)).thenReturn(expected);

        UUID expectedUuid = ProductTestData.builder().build().getUuid();

        // when
        UUID actualUuid = productService.create(productDto);

        // then
        assertEquals(expectedUuid, actualUuid);
        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue())
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, null);
    }
    
    @Test
    void updateShouldUpdateDescriptionAndPriceInProductWhenDataCorrect() {
        // given
        UUID uuid = ProductTestData.builder().build().getUuid();
        Product productToUpdate = ProductTestData.builder()
                .build().buildProduct();
        ProductDto productDtoToUpdate = ProductTestData.builder()
                .withDescription("Новое описание продукта")
                .withPrice(BigDecimal.valueOf(4))
                .build().buildProductDto();
        Product excepted = ProductTestData.builder()
                .withDescription("Новое описание продукта")
                .withPrice(BigDecimal.valueOf(4))
                .build().buildProduct();

        doReturn(true)
                .when(productValidator).checkValidation(productDtoToUpdate);

        doReturn(Optional.ofNullable(productToUpdate))
                .when(productRepository).findById(uuid);

        doReturn(excepted)
                .when(productMapper).merge(productToUpdate, productDtoToUpdate);

        doReturn(excepted).
                when(productRepository).save(excepted);

        // when
        productService.update(uuid, productDtoToUpdate);

        // then
        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue())
                .hasFieldOrPropertyWithValue(Product.Fields.description, excepted.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, excepted.getPrice());
    }

    @Test
    void updateShouldNotUpdateWhenIncorrectDescriptionInProduct() {
        // given
        UUID uuid = ProductTestData.builder().build().getUuid();
        ProductDto productDtoToUpdate = ProductTestData.builder()
                .withDescription("My new description")
                .build().buildProductDto();

        when(productValidator.checkValidation(productDtoToUpdate))
                .thenReturn(false);

        // when
        productService.update(uuid, productDtoToUpdate);

        // then
        verify(productValidator).checkValidation(productDtoToUpdate);
    }

    @Test
    void deleteShouldDeleteProductUsingUuid() {
        // given
        UUID uuid = ProductTestData.builder().build().getUuid();

        // when
        productService.delete(uuid);

        // then
        verify(productRepository).delete(uuid);
    }
}