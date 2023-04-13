package org.example.request_processing.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.request_processing.requests.FileRequest;
import org.example.request_processing.responses.FileResponse;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.utils.github.GithubUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("files")
public class FileController {

    private final GithubUtils githubUtils;

    @PostMapping
    public FileResponse getFile(@RequestBody FileRequest fileInfo) throws IOException {

        return githubUtils.getFilesUtils().getFile(fileInfo);
    }

    @DeleteMapping
    public ResponseEntity<LogicalStateResponse> deleteFile(@RequestBody FileRequest fileInfo) throws IOException {

        return githubUtils.getFilesUtils().deleteFile(fileInfo);
    }

    @PutMapping
    public ResponseEntity<LogicalStateResponse> updateFile(@RequestBody FileRequest fileInfo) throws IOException {

        return githubUtils.getFilesUtils().updateFile(fileInfo);
    }
}
