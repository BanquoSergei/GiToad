package org.example.controllers.responses;

import org.example.data.github.dto.RepositoryViewDTO;

import java.util.List;

public record RepositoriesResponse(List<RepositoryViewDTO> repositories) {

}
