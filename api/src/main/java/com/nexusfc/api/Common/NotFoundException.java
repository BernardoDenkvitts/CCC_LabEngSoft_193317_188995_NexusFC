package com.nexusfc.api.Common;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message + " was not found");
    }
}
