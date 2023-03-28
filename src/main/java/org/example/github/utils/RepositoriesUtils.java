package org.example.github.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.controllers.clients.HttpClient;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.controllers.responses.RepositoriesResponse;
import org.example.controllers.responses.RepositoryResponse;
import org.example.crypt.Cryptographer;
import org.example.github.dto.RepositoryDTO;
import org.example.github.dto.RepositoryNameDTO;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RepositoriesUtils {

    private final byte[] jwt;
    private final GitHub client;

    private final Cryptographer cryptographer;
    private final static String REPOS_URL = "https://api.github.com/repos/%s/%s";

    private ObjectMapper mapper = new ObjectMapper();

    public ResponseEntity<RepositoriesResponse> getAllRepositories() throws IOException {

        var rawResponse = HttpClient.getRawResponseWithAuthentication("https://api.github.com/user/repos", new String(cryptographer.decrypt(jwt)));
        var listRepositoriesTypeReference = new TypeReference<List<RepositoryNameDTO>>() {};
        var repositories = mapper.readValue(rawResponse, listRepositoriesTypeReference)
                .stream()
                .map(RepositoryNameDTO::getName).toList();
        return ResponseEntity.ok(new RepositoriesResponse(repositories));
    }

    public ResponseEntity<RepositoryResponse> getRepository(String repositoryName) throws IOException {

        var rawResponse = HttpClient.getRawResponseWithAuthentication(
                String.format(
                        REPOS_URL,
                        client.getMyself().getLogin(),
                        repositoryName
                ),
                new String(cryptographer.decrypt(jwt)
                )
        );

        return ResponseEntity.ok(new RepositoryResponse(mapper.readValue(rawResponse, RepositoryDTO.class)));
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
