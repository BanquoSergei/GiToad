package org.example.request_processing.responses;

import org.example.data.dto.RepositoryDTO;

public record RepositoryResponse(RepositoryDTO repository, String branch) {

}