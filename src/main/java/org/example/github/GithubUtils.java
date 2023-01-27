package org.example.github;

import org.example.controllers.responses.RepositoryResponse;
import org.example.github.auth.AuthBy;
import org.example.github.dto.RepositoryDTO;
import org.example.users.UserService;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GithubUtils {

    private final UserService userService;

    private GitHub client;

    public GithubUtils(UserService userService) {

        this.userService = userService;
    }

    public void setup(String id, String by) throws IOException {

        if(id == null)
            throw new InvalidParameterException("Parameter 'id' has been required");

        if(by == null) {
            loginByPassword(id);
            return;
        }

        switch (AuthBy.valueOf(by)) {
            case JWT -> loginByJwt(id);
            case OAUTH -> loginByOauthToken(id);
            case INSTALLATION -> loginByInstallationToken(id);
            case PASSWORD -> loginByPassword(id);
        }
    }
    private void loginByPassword(String id) throws IOException {

        var credentials = userService.findUsernameAndPassword(id);
        client = new GitHubBuilder()
                .withPassword(
                        credentials.getUsername(),
                        credentials.getPassword()
                ).build();
    }

    private void loginByJwt(String id) throws IOException {

        client = new GitHubBuilder()
                .withJwtToken(
                        userService.findJwtToken(id)
                ).build();
    }

    private void loginByInstallationToken(String id) throws IOException {

        client = new GitHubBuilder()
                .withJwtToken(
                        userService.findInstallationToken(id)
                ).build();
    }

    private void loginByOauthToken(String id) throws IOException {

        client = new GitHubBuilder()
                .withOAuthToken(
                        userService.findOauthToken(id)
                ).build();
    }

    public RepositoryResponse getAllRepositories() throws IOException {

        var response = RepositoryResponse.success();
        List<RepositoryDTO> repositories = new ArrayList<>();
        for(var repository: client.listAllPublicRepositories())
            repositories.add(new RepositoryDTO((repository)));
        response.setRepositories(repositories);

        return response;
    }

    public RepositoryResponse getRepository(String repositoryName) throws IOException {

        var response = RepositoryResponse.success();
        response.setRepository(new RepositoryDTO(client.getRepository(repositoryName)));

        return response;

    }

    public RepositoryResponse addCollaboratorsToRepository(String repositoryName, Map<String, GHOrganization.Permission> collaborators) throws IOException {

        var repository = client.getRepository(repositoryName);

        for(var login: collaborators.keySet());

        return RepositoryResponse.success();
    }
}
