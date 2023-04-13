package org.example.request_processing.responses;

import org.example.data.dto.views.RepositoryViewDTO;

import java.util.List;

public record RepositoriesResponse(List<RepositoryViewDTO> repositories) {

}
