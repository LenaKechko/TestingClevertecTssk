package ru.clevertec.product.exception;

public class NotValidException extends RuntimeException {

    public NotValidException() {
        super("Entity isn't valid");
    }
}
