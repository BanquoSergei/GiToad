package org.example.request_processing.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.data.dto.UploadFileDTO;
import org.example.request_processing.requests.CreateCommitRequest;
import org.example.request_processing.requests.FileRequest;
import org.example.request_processing.responses.CommitResponse;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.utils.github.GithubUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("commits")
@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
public class CommitController {
    private final GithubUtils githubUtils;

    @GetMapping(value = "/files")
    public CommitResponse getFiles(@RequestParam String repositoryName, @RequestParam String sha) throws IOException {

        return githubUtils.getFilesUtils().getCommitFiles(repositoryName, sha);
    }
    @PostMapping(value = "/upload")
    public LogicalStateResponse uploadFile(@RequestBody CreateCommitRequest data) throws IOException {

        return githubUtils.getFilesUtils().createCommit(data.file(), data.fileInfo());
    }
}
