package org.example.request_processing.requests;

public record CreateRepositoryRequest(
        String name,
        String newName,
        String description,
        String homepage,
        String defaultBranch,
        boolean autoInit,
        boolean downloadsEnable,
        boolean issuesEnable,
        boolean isPrivate,
        boolean isTemplate
) {
}
