package org.example.github.dto.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.controllers.responses.HttpClient;
import org.example.github.dto.BranchDTO;
import org.example.github.dto.GHContentDTO;
import org.example.github.dto.RepositoryDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RepositoryDTODeserializer extends StdDeserializer<RepositoryDTO> {

    private final ObjectMapper mapper;

    public RepositoryDTODeserializer() {
        this(RepositoryDTO.class);
    }

    protected RepositoryDTODeserializer(Class<?> vc) {
        super(vc);
        mapper = new ObjectMapper();
    }

    @Override
    public RepositoryDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        var dto = new RepositoryDTO();
        dto.setName(node.get("name").textValue());
        dto.setUrl(node.get("url").textValue());
        var rawLanguages = HttpClient.getRawResponse(node.get("languages_url").textValue());
        TypeReference<HashMap<String, Long>> languagesTypeReference = new TypeReference<>(){};
        dto.setLanguages(mapper.readValue(rawLanguages, languagesTypeReference));
        dto.setCountForks(node.get("forks").intValue());

        try {
            var rawReadmeFile = HttpClient.getRawResponse(dto.getUrl() + "/contents/README.md");
            dto.setReadme(mapper.readValue(rawReadmeFile, GHContentDTO.class));
        }
        catch (IOException ignored) {}


        var rawBranches = HttpClient.getRawResponse(node.get("branches_url").textValue().replace("{/branch}", ""));
        TypeReference<List<BranchDTO>> branchesTypeReference = new TypeReference<>() {};
        dto.setBranches(mapper.readValue(rawBranches, branchesTypeReference));

        return dto;
    }
}
