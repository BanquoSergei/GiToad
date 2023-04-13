package org.example.data.dto;

import org.example.data.dto.views.FileViewDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record RepositoryDTO(
        String name,
        String url,
        FileDTO readme,
        String description,
        Set<String> branches,
        Map<String, Long> languages,
        List<FileViewDTO> files) {
}
