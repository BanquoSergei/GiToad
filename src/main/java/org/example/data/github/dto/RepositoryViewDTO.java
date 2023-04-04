package org.example.data.github.dto;

public record RepositoryViewDTO(String name,
                                String url,
                                String description,
                                boolean isPrivate,
                                String language) {

}
