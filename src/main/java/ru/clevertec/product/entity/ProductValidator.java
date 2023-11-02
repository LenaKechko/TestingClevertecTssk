package ru.clevertec.product.entity;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
public class ProductValidator {

    public boolean checkName(String name) {
        return name != null && !name.isEmpty()
                && name.matches("[а-яА-ЯёЁ ]{5,10}");
    }

    public boolean checkDescription(String description) {
        return (description.isEmpty()
                || description.matches("[а-яА-ЯёЁ ]{10,30}"));
    }

    public boolean checkPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean checkCreated(LocalDateTime created) {
        return created != null;
    }

    public Product checkValidation(Product product) {
        return checkName(product.getName()) && checkDescription(product.getDescription())
                && checkPrice(product.getPrice()) && checkCreated(product.getCreated()) ? product : null;
    }
}
