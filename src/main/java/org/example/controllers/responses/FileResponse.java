package org.example.controllers.responses;

import lombok.Getter;
import lombok.Setter;
import org.example.github.dto.GHContentDTO;

@Getter
@Setter
public class FileResponse extends Response {

    private GHContentDTO file;

    public FileResponse(String status, String message) {
        super(status, message);
    }

    public static FileResponse success() {
        return new FileResponse(SUCCESS_STATUS, "Successful file operation");
    }

    public static FileResponse error(Exception exception) {
        return new FileResponse(ERROR_STATUS, String.format(ERROR_MESSAGE, exception));
    }
}
