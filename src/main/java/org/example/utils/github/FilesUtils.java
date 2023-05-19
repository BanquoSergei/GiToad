package org.example.utils.github;

import lombok.RequiredArgsConstructor;
import org.example.data.dto.UploadFileDTO;
import org.example.data.dto.views.FileViewDTO;
import org.example.data.dto.views.ViewCommitDTO;
import org.example.request_processing.requests.FileRequest;
import org.example.request_processing.responses.CommitResponse;
import org.example.request_processing.responses.FileResponse;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.data.dto.FileDTO;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RequiredArgsConstructor
public class FilesUtils {

    private final GitHub client;

    public FileResponse getFile(FileRequest fileInfo) throws IOException {

        var repository = client.getRepository(fileInfo.repository());
        var file = repository.getFileContent(fileInfo.path(), fileInfo.branch());

        return new FileResponse(
                new FileDTO(
                        file.getName(),
                        file.getContent(),
                        file.getDownloadUrl(),
                        file.getGitUrl(),
                        file.getSha()
                )
        );
    }
    public ResponseEntity<LogicalStateResponse> deleteFile(FileRequest fileInfo) throws IOException {

        var repository = client.getRepository(fileInfo.repository());
        repository.getFileContent(fileInfo.path(), fileInfo.branch()).delete(fileInfo.message());

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public ResponseEntity<LogicalStateResponse> updateFile(FileRequest fileInfo) throws IOException {

        var repository = client.getRepository(fileInfo.repository());
           repository.getFileContent(fileInfo.path()).update(fileInfo.content(), fileInfo.message());

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public LogicalStateResponse createCommit(UploadFileDTO file, FileRequest fileInfo) {

        try {
            var repoName = client.getMyself().getLogin() + "/" + fileInfo.repository();
            var repository = client.getRepository(repoName);
            var baseTree = repository.getTree(getLastCommit(fileInfo.branch(), repository).sha());
            var tree = repository.createTree().baseTree(baseTree.getSha());
            tree.add(file.path(), file.content(), true);
            repository.createCommit().tree(tree.create().getSha()).message(fileInfo.message()).create();
            return new LogicalStateResponse(true);
        } catch (IOException e) {
            return new LogicalStateResponse(false);
        }
    }

    private static ViewCommitDTO getLastCommit(String branch, GHRepository repo) throws IOException {
        try {
            var commit = repo.queryCommits().from(branch).list()
                    .toList().stream()
                    .max((c1, c2) -> {
                        try {
                            return c1.getCommitDate().compareTo(c2.getCommitDate());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).map(c -> {
                        try {
                            return new ViewCommitDTO(
                                    c.getCommitDate(),
                                    c.getCommitShortInfo().getMessage(),
                                    c.getSHA1(),
                                    c.getUrl().toString(),
                                    c.getCommitShortInfo().getCommitter() == null ? "Unknown user" : c.getCommitShortInfo().getCommitter().getUsername()
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).get();

            return commit;
        } catch (HttpException e) {
            return null;
        }
    }
    public CommitResponse getCommitFiles(String repositoryName, String sha) throws IOException {

        var repository = client.getRepository(repositoryName);
        return new CommitResponse(
                repository.getCommit(sha).getFiles().stream()
                        .map(file -> new FileViewDTO(file.getFileName(), file.getSha(), true))
                        .toList()
        );
    }
}
