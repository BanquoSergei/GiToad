package org.example.request_processing.exceptions;

import java.security.InvalidKeyException;

public class InvalidInteractionKeyException extends InvalidKeyException {

    public InvalidInteractionKeyException() {

        super("Interaction key is not valid");
    }
}

