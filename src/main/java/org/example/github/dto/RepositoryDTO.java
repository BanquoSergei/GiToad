package org.example.github.dto;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.github.dto.deserializers.RepositoryDTODeserializer;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonDeserialize(using = RepositoryDTODeserializer.class)
public class RepositoryDTO {

    private int countForks;

    private String name;

    private String url;

    private GHContentDTO readme;

    private List<ViewCommitDTO> commits = new ArrayList<>();

    private List<BranchDTO> branches;

    private Map<String, Long> languages;

    public RepositoryDTO(GHRepository repository) throws IOException {

        countForks = repository.getForksCount();
        name = repository.getName();
        url = repository.getUrl().toString();
        try {
            readme = new GHContentDTO(repository.getReadme());
        } catch (GHFileNotFoundException e) {

        }
        for (var commit: repository.listCommits())
            commits.add(new ViewCommitDTO(commit.getCommitShortInfo()));

        branches = repository.getBranches().values().stream().map(BranchDTO::new).toList();
        languages = repository.listLanguages();
    }
}
