package org.example.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@AllArgsConstructor
@Data
public class Response {
    static final String SUCCESS_STATUS = "Success";
    static final String ERROR_STATUS = "Error";

    static final String ERROR_MESSAGE = "Operation was finished with error:\n%s";
    protected String status;
    protected String message;

    public static Response success() {
        return new Response(SUCCESS_STATUS, "Successful operation");
    }

    public static Response error(Exception exception) {
        return new Response(ERROR_STATUS, String.format(ERROR_MESSAGE, exception));
    }

    public void concatToMessage(String added) {
        message = added + "\n" + message;
    }

    public boolean isError() { return status.equals(ERROR_STATUS); }
}
