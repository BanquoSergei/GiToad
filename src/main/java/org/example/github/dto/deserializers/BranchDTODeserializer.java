package org.example.github.dto.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.NoArgsConstructor;
import org.example.github.dto.BranchDTO;
import org.example.github.dto.ViewCommitDTO;

import java.io.IOException;

public class BranchDTODeserializer extends StdDeserializer<BranchDTO> {

    private ObjectMapper mapper;


    public BranchDTODeserializer() {

        this(BranchDTO.class);
    }
    protected BranchDTODeserializer(Class<?> vc) {
        super(vc);
        mapper = new ObjectMapper();
    }

    @Override
    public BranchDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        var dto = new BranchDTO();
        dto.setName(node.get("name").textValue());
        dto.setProtected(node.get("protected").booleanValue());
        dto.setCurrentCommit(mapper.readValue(node.get("commit").toPrettyString(), ViewCommitDTO.class));

        return dto;
    }
}
