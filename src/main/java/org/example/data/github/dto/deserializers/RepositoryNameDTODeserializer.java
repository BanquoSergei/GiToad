package org.example.data.github.dto.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.data.github.dto.RepositoryNameDTO;

import java.io.IOException;

public class RepositoryNameDTODeserializer extends StdDeserializer<RepositoryNameDTO> {

    public RepositoryNameDTODeserializer() {
        this(RepositoryNameDTO.class);
    }
    protected RepositoryNameDTODeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RepositoryNameDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        var dto = new RepositoryNameDTO();
        dto.setName(node.findValue("name").textValue());

        return dto;
    }
}
