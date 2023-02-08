package org.example.controllers.responses;

import lombok.Getter;
import org.example.github.dto.RepositoryDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class RepositoryResponse extends Response {

    private RepositoryDTO repository;

    private List<String> repositories;


    public RepositoryResponse(String status, String message) {
        super(status, message);
    }

    public static RepositoryResponse success() {
        return new RepositoryResponse(SUCCESS_STATUS, "Successful repository operation");
    }

    public static RepositoryResponse error(Exception exception) {
        return new RepositoryResponse(ERROR_STATUS, String.format(ERROR_MESSAGE, exception));
    }

    public void setRepositories(Collection<String> repositories) {

        this.repositories = new ArrayList<>(repositories);
    }

    public void setRepository(RepositoryDTO repository) {
        this.repository = repository;
    }
}
