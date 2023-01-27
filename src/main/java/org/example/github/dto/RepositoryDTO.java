package org.example.github.dto;

import lombok.Data;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class RepositoryDTO {

    private int countForks;

    private String name;

    private String url;

    private GHContentDTO readme;

    private List<CommitDTO> commits = new ArrayList<>();

    private List<BranchDTO> branches;

    private CloneTrafficDTO traffic;

    private List<GHUserDTO> collaborators;

    private Map<String, Long> languages;

    public RepositoryDTO(GHRepository repository) throws IOException {

        countForks = repository.getForksCount();
        name = repository.getName();
        url = repository.getUrl().toString();
        readme = new GHContentDTO(repository.getReadme());

        for (var commit: repository.listCommits())
            commits.add(new CommitDTO(commit));

        branches = repository.getBranches().values().stream().map(BranchDTO::new).toList();
        traffic = new CloneTrafficDTO(repository.getCloneTraffic());
        for (GHUser ghUser : repository.getCollaborators())
            collaborators.add(new GHUserDTO(ghUser));
        languages = repository.listLanguages();
    }
}
