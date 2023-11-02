package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.entity.ProductValidator;
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
        InfoProductDto infoProductDto = null;
        try {
            Product product = productRepository.findById(uuid).orElseThrow();
            infoProductDto = mapper.toInfoProductDto(product);
        } catch (Exception e) {
            throw new ProductNotFoundException(uuid);
        }
        return infoProductDto;
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> productList = productRepository.findAll();
        List<InfoProductDto> infoProductDtoList = productList.stream()
                .map(mapper::toInfoProductDto)
                .toList();
        return infoProductDtoList;
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product productWithoutUUID = mapper.toProduct(productDto);
        Product productToSave = productValidator.checkValidation(productWithoutUUID);
        try {
            Product productWithUUID = productRepository.save(productToSave);
            return productWithUUID.getUuid();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Product product = productRepository.findById(uuid).orElseThrow();
        product = productValidator.checkValidation(product);
        if (product != null) {
            Product productMerge = mapper.merge(product, productDto);
            productRepository.save(productMerge);
        }
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
