package org.example.data.dto;

import org.example.data.dto.views.FileViewDTO;
import org.example.data.dto.views.ViewCommitDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record RepositoryDTO(
        String name,
        String url,
        String owner,
        FileDTO readme,
        String description,
        Set<String> branches,
        String currentBranch,
        List<ViewCommitDTO> commits,
        Map<String, Long> languages,
        List<FileViewDTO> files) {
}
