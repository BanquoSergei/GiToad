package org.example.controllers.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.github.dto.GHContentDTO;

import java.util.List;

@Getter
@Setter
public class CommitResponse extends Response {

    private List<GHContentDTO> files;

    public CommitResponse(String status, String message) {
        super(status, message);
    }

    public static CommitResponse success() {
        return new CommitResponse(SUCCESS_STATUS, "Successful commit operation");
    }

    public static CommitResponse error(Exception exception) {
        return new CommitResponse(ERROR_STATUS, String.format(ERROR_MESSAGE, exception));
    }
}
