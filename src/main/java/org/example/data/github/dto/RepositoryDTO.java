package org.example.data.github.dto;

import java.util.Map;
import java.util.Set;

public record RepositoryDTO(
        String name,
        String url,
        GHContentDTO readme,
        String description,
        Set<String> branches,
        Map<String, Long> languages) {
}
