package org.example.github.dto.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.github.dto.ViewCommitDTO;

import java.io.IOException;

public class ViewCommitDTODeserializer extends StdDeserializer<ViewCommitDTO> {

    private ObjectMapper mapper;

    public ViewCommitDTODeserializer() {

        this(ViewCommitDTO.class);
    }
    protected ViewCommitDTODeserializer(Class<?> vc) {

        super(vc);
        mapper = new ObjectMapper();
    }

    @Override
    public ViewCommitDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        var dto = new ViewCommitDTO();

        dto.setUrl(node.get("url").textValue());
        dto.setSha(node.get("sha").textValue());
//        dto.setFiles(mapper.readValue(node.get("files")));

        return dto;
    }
}
