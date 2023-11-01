package ru.clevertec.product.service.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with", toBuilder = true)

public class ProductTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("b8003c54-c22b-450a-a0d3-94b646150584");

    @Builder.Default
    private String name = "Продукт";

    @Builder.Default
    private String description = "Описание продукта";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(1);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.MIN;

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuid, name, description, price);
    }
}
