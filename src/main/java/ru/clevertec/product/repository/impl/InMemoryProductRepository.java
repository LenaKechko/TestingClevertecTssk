package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> productMap = new LinkedHashMap<>();

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(productMap.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return productMap.values().stream()
                .toList();
    }

    @Override
    public Product save(Product product) {
        UUID uuid;
        uuid = (product.getUuid() == null) ? UUID.randomUUID() : product.getUuid();
        product.setUuid(uuid);
        productMap.put(uuid, product);
        return product;
    }

    @Override
    public void delete(UUID uuid) {
        productMap.remove(uuid);
    }
}