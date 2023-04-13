package org.example.utils.github;

import lombok.RequiredArgsConstructor;
import org.example.data.dto.views.FileViewDTO;
import org.example.request_processing.requests.FileRequest;
import org.example.request_processing.responses.CommitResponse;
import org.example.request_processing.responses.FileResponse;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.data.dto.FileDTO;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RequiredArgsConstructor
public class FilesUtils {

    private final GitHub client;

    public FileResponse getFile(FileRequest fileInfo) throws IOException {

        var repository = client.getRepository(fileInfo.repository());
        var defaultBranch = repository.getDefaultBranch();
        var swapBranch = fileInfo.branch() != null && !fileInfo.branch().equals(defaultBranch);
        if(swapBranch)
            repository.setDefaultBranch(fileInfo.branch());
        var file = repository.getFileContent(fileInfo.path());
        if(swapBranch)
            repository.setDefaultBranch(defaultBranch);

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
        var defaultBranch = repository.getDefaultBranch();
        var swapBranch = fileInfo.branch() != null && !fileInfo.branch().equals(defaultBranch);
        if(swapBranch)
            repository.setDefaultBranch(fileInfo.branch());
        repository.getFileContent(fileInfo.path()).delete(fileInfo.message());
        if(swapBranch)
            repository.setDefaultBranch(defaultBranch);

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public ResponseEntity<LogicalStateResponse> updateFile(FileRequest fileInfo) throws IOException {

        var repository = client.getRepository(fileInfo.repository());
        var defaultBranch = repository.getDefaultBranch();
        var swapBranch = fileInfo.branch() != null && !fileInfo.branch().equals(defaultBranch);
        if(swapBranch)
            repository.setDefaultBranch(fileInfo.branch());
        repository.getFileContent(fileInfo.path()).update(fileInfo.content(), fileInfo.message());
        if(swapBranch)
            repository.setDefaultBranch(defaultBranch);


        return ResponseEntity.ok(new LogicalStateResponse(true));
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
