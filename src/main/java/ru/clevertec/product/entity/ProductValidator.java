package ru.clevertec.product.entity;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ProductValidator {

    public boolean checkName(String name) {
        return true;
    }

    public boolean checkDescription(String description) {
        return true;
    }

    public boolean checkPrice(BigDecimal price) {
        return true;
    }

    public boolean checkCreated(LocalDateTime created) {
        return true;
    }

    public Product checkValidation(Product product) {
        return null;
    }
}
