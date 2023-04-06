package org.example.data.github.dto;

import java.util.Date;

public record RepositoryViewDTO(String name,
                                String description,
                                boolean isPrivate,
                                boolean isFork,
                                boolean isArchived,
                                boolean isTemplate,
                                String language,
                                Date lastUpdate) {

}
