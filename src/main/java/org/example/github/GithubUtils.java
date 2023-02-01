package org.example.github;

import org.example.controllers.github.User;
import org.example.controllers.responses.RepositoryResponse;
import org.example.github.auth.AuthBy;
import org.example.github.dto.RepositoryDTO;
import org.example.users.UserService;
import org.kohsuke.github.*;
import org.kohsuke.github.GHOrganization.RepositoryRole;

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
        GHRepository repository = client.getRepository(repositoryName);
        response.setRepository(new RepositoryDTO(repository));

        return response;

    }

    public RepositoryResponse addCollaboratorsToRepository(String repositoryName, Map<RepositoryRole, List<GHUser>> collaborators) throws IOException {

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

