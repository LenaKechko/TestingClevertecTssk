package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.entity.ProductValidator;
import ru.clevertec.product.exception.NotValidException;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Override
    public InfoProductDto get(UUID uuid) {
        return productRepository.findById(uuid)
                .map(mapper::toInfoProductDto)
                .orElseThrow(() -> new ProductNotFoundException(uuid));
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(mapper::toInfoProductDto)
                .toList();
    }

    @Override
    public UUID create(ProductDto productDto) {
        if (productValidator.checkValidation(productDto)) {
            Product productWithUUID = productRepository.save(mapper.toProduct(productDto));
            return productWithUUID.getUuid();
        } else {
            throw new NotValidException();
        }
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        if (productValidator.checkValidation(productDto)) {
            Product product = productRepository.findById(uuid)
                    .orElseThrow(() -> new ProductNotFoundException(uuid));
            Product productMerge = mapper.merge(product, productDto);
            productRepository.save(productMerge);
        }
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
