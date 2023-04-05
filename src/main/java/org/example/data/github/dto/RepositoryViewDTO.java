package org.example.data.github.dto;

import java.util.Date;

public record RepositoryViewDTO(String name,
                                String url,
                                String description,
                                boolean isPrivate,
                                String language,
                                Date lastUpdate) {

}
