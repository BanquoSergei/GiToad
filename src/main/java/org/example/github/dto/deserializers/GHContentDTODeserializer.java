package org.example.github.dto.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.github.dto.GHContentDTO;

import java.io.IOException;

public class GHContentDTODeserializer extends StdDeserializer<GHContentDTO> {

    public GHContentDTODeserializer() {
        this(null);
    }
    protected GHContentDTODeserializer(Class<?> vc) {
        super(vc);
    }
    @Override
    public GHContentDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);
        var dto = new GHContentDTO();
        dto.setUrl(node.get("url").textValue());
        dto.setSize(node.get("size").longValue());
        dto.setName(node.get("name").textValue());
        dto.setContent(node.get("content").binaryValue());
        dto.setEncoding(node.get("encoding").textValue());
        dto.setDownloadUrl(node.get("download_url").textValue());

        return dto;
    }
}
