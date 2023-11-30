package ru.clevertec.product.data;

import lombok.Getter;

import java.math.BigDecimal;

public record ProductDto(

        /**
         * {@link ru.clevertec.product.entity.Product}
         */
        @Getter
        String name,


        /**
         * {@link ru.clevertec.product.entity.Product}
         */
        @Getter
        String description,


        /**
         * {@link ru.clevertec.product.entity.Product}
         */
        @Getter
        BigDecimal price) {
}
