package org.example.github.utils;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.CommitResponse;
import org.example.controllers.responses.FileResponse;
import org.example.github.dto.GHContentDTO;
import org.kohsuke.github.GitHub;

import java.io.IOException;

@RequiredArgsConstructor
public class FilesUtils {

    private final GitHub client;

    public FileResponse getFile(String repositoryName, String path) throws IOException {

        var response = FileResponse.success();
        var repository = client.getRepository(repositoryName);
        response.setFile(new GHContentDTO(repository.getFileContent(path)));

        return response;
    }
    public FileResponse deleteFile(String repositoryName, String path, String message) throws IOException {

        client.getRepository(repositoryName).getFileContent(path).delete(message);

        return FileResponse.success();
    }

    public FileResponse updateFile(String repositoryName, String path, String content, String message) throws IOException {

        client.getRepository(repositoryName).getFileContent(path).update(content, message);

        return FileResponse.success();
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
}
