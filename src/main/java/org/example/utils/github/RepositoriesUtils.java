package org.example.utils.github;

import org.example.data.dto.views.ViewCommitDTO;
import org.example.request_processing.clients.GitoadHttpClient;
import org.example.request_processing.requests.CreateRepositoryRequest;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.request_processing.responses.RepositoriesResponse;
import org.example.request_processing.responses.RepositoryResponse;
import org.example.data.dto.FileDTO;
import org.example.data.dto.RepositoryDTO;
import org.example.data.dto.views.RepositoryViewDTO;
import org.example.utils.crypt.Cryptographer;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RepositoriesUtils {

    private final GitHub client;

    private final GitoadHttpClient httpClient;

    public RepositoriesUtils(GitHub client, Cryptographer cryptographer, byte[] jwt) {
        this.client = client;
        httpClient = new GitoadHttpClient(cryptographer, jwt);
    }

    public ResponseEntity<RepositoriesResponse> getAllRepositories() throws IOException {

        var repositories = client.getMyself().getRepositories().values().stream()
                .map(repo -> {
                    try {
                        return new RepositoryViewDTO(
                          repo.getName(),
                          repo.getDescription(),
                          repo.isPrivate(),
                          repo.isFork(),
                          repo.isArchived(),
                          repo.isTemplate(),
                          repo.getLanguage(),
                          repo.getUpdatedAt()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        return ResponseEntity.ok(new RepositoriesResponse(repositories));
    }

    public ResponseEntity<RepositoryResponse> getRepository(String repositoryName, String branch) throws IOException {

        var repo = client.getMyself().getRepository(repositoryName);

        var currentBranch = branch == null ? repo.getDefaultBranch() : branch;
        var files = httpClient.getFiles(client.getMyself().getLogin(), repositoryName, currentBranch);

        FileDTO readme;

        try {
            var file = repo.getReadme();
            readme = new FileDTO(file.getName(), file.getContent(), file.getDownloadUrl(), file.getGitUrl(), file.getSha());
        } catch (IOException e) {
            readme = null;
        }
        var commits = repo.queryCommits().from(branch).list()
                .toList().stream()
                .map(commit -> {
                    try {
                        return new ViewCommitDTO(
                                commit.getCommitDate(),
                                commit.getCommitShortInfo().getMessage(),
                                commit.getSHA1(),
                                commit.getHtmlUrl().toString()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();


        var repoDTO = new RepositoryDTO(
                repo.getName(),
                repo.getHtmlUrl().toString(),
                repo.getOwnerName(),
                readme,
                repo.getDescription(),
                repo.getBranches().keySet(),
                currentBranch,
                commits,
                repo.listLanguages(),
                files
        );

        return ResponseEntity.ok(new RepositoryResponse(repoDTO));
    }

    public ResponseEntity<LogicalStateResponse> createRepository(CreateRepositoryRequest repositoryInfo) throws IOException {

        client.createRepository(repositoryInfo.name().substring(repositoryInfo.name().indexOf('/')))
                .defaultBranch(repositoryInfo.defaultBranch())
                .private_(repositoryInfo.isPrivate())
                .issues(repositoryInfo.issuesEnable())
                .autoInit(repositoryInfo.autoInit())
                .description(repositoryInfo.description())
                .homepage(repositoryInfo.homepage())
                .downloads(repositoryInfo.downloadsEnable())
                .isTemplate(repositoryInfo.isTemplate())
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

    public ResponseEntity<LogicalStateResponse> setupRepository(CreateRepositoryRequest repositoryInfo) throws IOException {

        var repository = client.getRepository(repositoryInfo.name());

        repository
                .set().defaultBranch(repositoryInfo.defaultBranch())
                .set().homepage(repositoryInfo.homepage())
                .set().private_(repositoryInfo.isPrivate())
                .set().issues(repositoryInfo.issuesEnable())
                .set().description(repositoryInfo.description())
                .set().downloads(repositoryInfo.downloadsEnable())
                .set().isTemplate(repositoryInfo.isTemplate());

        repository.renameTo(repositoryInfo.newName());

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }
}
