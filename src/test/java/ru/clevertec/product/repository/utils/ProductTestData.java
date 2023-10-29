package ru.clevertec.product.repository.utils;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with ")
public class ProductTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("338903f8-ff25-4df4-8348-f8cefc066296");

    @Builder.Default
    private String name = "My product";

    @Builder.Default
    private String description = "My description";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(1);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.of(2023, 7, 3, 13, 0, 0);

}
