package org.example.controllers.responses;

import org.example.github.dto.RepositoryDTO;

import java.util.List;

public class RepositoryResponse extends Response {

    private RepositoryDTO repository;

    private List<RepositoryDTO> repositories;


    public RepositoryResponse(String status, String message) {
        super(status, message);
    }

    public static RepositoryResponse success() {
        return new RepositoryResponse(SUCCESS_STATUS, "Successful repository operation");
    }

    public static RepositoryResponse error(Exception exception) {
        return new RepositoryResponse(ERROR_STATUS, String.format(ERROR_MESSAGE, exception));
    }

    public void setRepositories(List<RepositoryDTO> repositories) {

        this.repositories = repositories;
    }

    public void setRepository(RepositoryDTO repository) {
        this.repository = repository;
    }
}
