package com.safiap.techchallengeoficinamecanica.modules.shared.exceptions;

public class DomainException extends RuntimeException{

    public DomainException(String message) {
        super(message);
    }

    public static void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new DomainException(message);
        }
    }

    public static void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
    }

}
