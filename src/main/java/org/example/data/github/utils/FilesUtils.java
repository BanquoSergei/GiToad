package org.example.data.github.utils;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.CommitResponse;
import org.example.controllers.responses.FileResponse;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.data.github.dto.GHContentDTO;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RequiredArgsConstructor
public class FilesUtils {

    private final GitHub client;

    public FileResponse getFile(String repositoryName, String path) throws IOException {

        var repository = client.getRepository(repositoryName);

        return new FileResponse(new GHContentDTO(repository.getFileContent(path)));
    }
    public ResponseEntity<LogicalStateResponse> deleteFile(String repositoryName, String path, String message) throws IOException {

        client.getRepository(repositoryName).getFileContent(path).delete(message);

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public ResponseEntity<LogicalStateResponse> updateFile(String repositoryName, String path, String content, String message) throws IOException {

        client.getRepository(repositoryName).getFileContent(path).update(content, message);

        return ResponseEntity.ok(new LogicalStateResponse(true));
    }

    public CommitResponse getCommitFiles(String repositoryName, String sha) throws IOException {

        var repository = client.getRepository(repositoryName);
        return new CommitResponse(
                repository.getCommit(sha).getFiles().stream()
                        .map(file -> {
                            try {
                                return new GHContentDTO(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).toList()
        );
    }
}
