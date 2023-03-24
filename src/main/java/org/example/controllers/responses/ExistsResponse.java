package org.example.controllers.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExistsResponse extends Response {

    private boolean exists;

    public ExistsResponse(String status, String message) {
        super(status, message);
    }

    public static ExistsResponse success() {
        return new ExistsResponse(SUCCESS_STATUS, "Successful exists request");
    }

    public static ExistsResponse error(Exception exception) {
        return new ExistsResponse(ERROR_STATUS, String.format(ERROR_MESSAGE, exception));
    }
}
