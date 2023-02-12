package org.example.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.*;
import org.example.crypt.Cryptographer;
import org.example.github.auth.AuthBy;
import org.example.github.dto.GHContentDTO;
import org.example.github.dto.RepositoryDTO;
import org.example.github.dto.RepositoryNameDTO;
import org.example.users.User;
import org.example.users.UserService;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHOrganization.RepositoryRole;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GithubUtils {

    private final UserService userService;

    private final Cryptographer cryptographer;
    private final String REPOS_URL = "https://api.github.com/repos/";

    private GitHub client;

    private User user;

    private final ObjectMapper mapper = new ObjectMapper();

    public void setup(String id, String by) throws IOException {

        if(id == null)
            throw new InvalidParameterException("Parameter 'id' has been required");

        user = userService.find(id);

        if(by == null) {
            loginByJwt();
            return;
        }

        switch (AuthBy.valueOf(by)) {
            case OAUTH -> loginByOauthToken();
            case INSTALLATION -> loginByInstallationToken();
            case PASSWORD -> loginByPassword();
            default -> loginByJwt();
        }
    }

    private void loginByPassword() throws IOException {
        client = new GitHubBuilder()
                .withPassword(
                        decrypt(user.getUsername()),
                        decrypt(user.getPassword())
                )
                .build();
    }

    private void loginByOauthToken() throws IOException {
        client = new GitHubBuilder()
                .withOAuthToken(decrypt(user.getOauthToken()))
                .build();
    }

    private void loginByInstallationToken() throws IOException {
        client = new GitHubBuilder()
                .withAppInstallationToken(decrypt(user.getInstallationToken()))
                .build();
    }

    private void loginByJwt() throws IOException {
        client = new GitHubBuilder()
                .withJwtToken(decrypt(user.getJwtToken()))
                .build();
    }

    public RepositoryResponse getAllRepositories() throws IOException {

        var response = RepositoryResponse.success();
        var rawResponse = HttpClient.getRawResponseWithAuthentication("https://api.github.com/user/repos", decrypt(user.getJwtToken()));
        var listRepositoriesTypeReference = new TypeReference<List<RepositoryNameDTO>>() {};
        var repositories = mapper.readValue(rawResponse, listRepositoriesTypeReference)
                        .stream()
                        .map(RepositoryNameDTO::getName).toList();
        response.setRepositories(repositories);

        return response;
    }

    public RepositoryResponse getRepository(String repositoryName) throws IOException {

        var response = RepositoryResponse.success();
        var rawResponse = HttpClient.getRawResponseWithAuthentication(REPOS_URL + repositoryName, decrypt(user.getJwtToken()));
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

    public FileResponse getFile(String repositoryName, String path) throws IOException {

        var response = FileResponse.success();
        var repository = client.getRepository(repositoryName);
        response.setFile(new GHContentDTO(repository.getFileContent(path)));

        return response;
    }

    public CommitResponse getCommitFiles(String repositoryName, String sha) throws IOException {

        var repository = client.getRepository(repositoryName);
        var response = CommitResponse.success();
        response.setFiles(
                repository.getCommit(sha).getFiles().stream()
                .map(file -> {
                    try {
                        return new GHContentDTO(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList()
        );
        return response;
    }

    private byte[] encrypt(String value) {
        return (value == null) ? null : cryptographer.encrypt(value.getBytes());
    }
    private String decrypt(byte[] value) {
        return (value == null) ? null :  new String(cryptographer.decrypt(value));
    }

    public Response updateData(String id, String username, String password, String jwtToken, String installationToken, String oauthToken) {
        return userService.updateData(
                id,
                encrypt(username),
                encrypt(password),
                encrypt(jwtToken),
                encrypt(installationToken),
                encrypt(oauthToken)
        );
    }
}