package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;

class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}