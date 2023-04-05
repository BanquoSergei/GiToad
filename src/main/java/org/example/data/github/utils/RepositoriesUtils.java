package org.example.data.github.utils;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.controllers.responses.RepositoriesResponse;
import org.example.controllers.responses.RepositoryResponse;
import org.example.data.github.dto.GHContentDTO;
import org.example.data.github.dto.RepositoryDTO;
import org.example.data.github.dto.RepositoryViewDTO;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RepositoriesUtils {

    private final GitHub client;

    public ResponseEntity<RepositoriesResponse> getAllRepositories() throws IOException {

        var repositories = client.getMyself().getRepositories().values().stream()
                .map(repo -> {
                    try {
                        return new RepositoryViewDTO(
                          repo.getName(),
                          repo.getHtmlUrl().toString(),
                          repo.getDescription(),
                          repo.isPrivate(),
                          repo.getLanguage(),
                          repo.getUpdatedAt()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return ResponseEntity.ok(new RepositoriesResponse(repositories));
    }

    public ResponseEntity<RepositoryResponse> getRepository(String repositoryName) throws IOException {

        var repo = client.getMyself().getRepository(repositoryName);
        var sha = repo.getBranch("master").getSHA1();

        GHContentDTO readme;

        try {
            readme = new GHContentDTO(repo.getReadme());
        } catch (IOException e) {
            readme = GHContentDTO.getInstance();
        }
        var repoDTO = new RepositoryDTO(
                repo.getName(),
                repo.getHtmlUrl().toString(),
                readme,
                repo.getDescription(),
                repo.getBranches().keySet(),
                repo.listLanguages()
        );

        return ResponseEntity.ok(new RepositoryResponse(repoDTO));
    }

    public ResponseEntity<LogicalStateResponse> createRepository(String name,
                                               String description,
                                               String homepage,
                                               String defaultBranch,
                                               boolean autoInit,
                                               boolean downloadsEnable,
                                               boolean issuesEnable,
                                               boolean isPrivate,
                                               boolean isTemplate) throws IOException {

        name = name.substring(name.indexOf('/'));
        client.createRepository(name)
                .defaultBranch(defaultBranch)
                .private_(isPrivate)
                .issues(issuesEnable)
                .autoInit(autoInit)
                .description(description)
                .homepage(homepage)
                .downloads(downloadsEnable)
                .isTemplate(isTemplate)
                .create();

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public ResponseEntity<LogicalStateResponse> addCollaboratorsToRepository(String repositoryName, Map<GHOrganization.RepositoryRole, List<GHUser>> collaborators) throws IOException {

        var repository = client.getRepository(repositoryName);
        collaborators.keySet().forEach(role -> {
            try {
                repository.addCollaborators(collaborators.get(role), role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public ResponseEntity<LogicalStateResponse> setupRepository(String repositoryName,
                                                                String renameTo,
                                                                String homeUrl,
                                                                String emailHook,
                                                                String defaultBranch,
                                                                String description,
                                                                Boolean isPrivate) throws IOException {

        var repository = client.getRepository(repositoryName);

        repository.set().defaultBranch(defaultBranch)
                .set().homepage(homeUrl)
                .set().private_(isPrivate)
                .set().description(description);

        repository.renameTo(renameTo);
        if(emailHook != null)
            repository.setEmailServiceHook(emailHook);

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }
}
