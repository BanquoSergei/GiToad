package org.example.github.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.controllers.clients.HttpClient;
import org.example.controllers.responses.RepositoryResponse;
import org.example.crypt.Cryptographer;
import org.example.github.dto.RepositoryDTO;
import org.example.github.dto.RepositoryNameDTO;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

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

    public RepositoryResponse getAllRepositories() throws IOException {

        var response = RepositoryResponse.success();
        var rawResponse = HttpClient.getRawResponseWithAuthentication("https://api.github.com/user/repos", new String(cryptographer.decrypt(jwt)));
        var listRepositoriesTypeReference = new TypeReference<List<RepositoryNameDTO>>() {};
        var repositories = mapper.readValue(rawResponse, listRepositoriesTypeReference)
                .stream()
                .map(RepositoryNameDTO::getName).toList();
        response.setRepositories(repositories);

        return response;
    }

    public RepositoryResponse getRepository(String repositoryName) throws IOException {

        var response = RepositoryResponse.success();
        var rawResponse = HttpClient.getRawResponseWithAuthentication(String.format(REPOS_URL, client.getMyself().getLogin(), repositoryName), new String(cryptographer.decrypt(jwt)));
        response.setRepository(mapper.readValue(rawResponse, RepositoryDTO.class));

        return response;

    }

    public RepositoryResponse createRepository(String name,
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

        return RepositoryResponse.success();
    }

    public RepositoryResponse addCollaboratorsToRepository(String repositoryName, Map<GHOrganization.RepositoryRole, List<GHUser>> collaborators) throws IOException {

        var repository = client.getRepository(repositoryName);
        collaborators.keySet().forEach(role -> {
            try {
                repository.addCollaborators(collaborators.get(role), role);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return RepositoryResponse.success();
    }

    public RepositoryResponse setupRepository(String repositoryName,
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

        return RepositoryResponse.success();
    }
}
