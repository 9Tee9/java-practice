package org.polina.practice.exception;

public class ProductNotInStockException extends RuntimeException{
    public ProductNotInStockException(String message) {
        super(message);
    }
}
