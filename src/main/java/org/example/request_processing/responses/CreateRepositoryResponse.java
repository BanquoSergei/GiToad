package org.example.request_processing.responses;

import java.util.List;

public record CreateRepositoryResponse(boolean successOperation, List<String> violations) {
}
