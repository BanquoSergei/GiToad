package org.example.data.github.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.data.github.dto.deserializers.RepositoryNameDTODeserializer;

@NoArgsConstructor
@Data
@JsonDeserialize(using = RepositoryNameDTODeserializer.class)
public class RepositoryNameDTO {

    private String name;

}
