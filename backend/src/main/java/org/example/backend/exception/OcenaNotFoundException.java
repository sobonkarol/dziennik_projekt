package org.example.backend.exception;

public class OcenaNotFoundException extends RuntimeException {
    public OcenaNotFoundException(String message) {
        super(message);
    }
}