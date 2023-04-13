package org.example.request_processing.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.request_processing.responses.CommitResponse;
import org.example.utils.github.GithubUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("commits")
public class CommitController {
    private final GithubUtils githubUtils;

    @GetMapping(value = "/files")
    public CommitResponse getFiles(@RequestParam String repositoryName, @RequestParam String sha) throws IOException {

        return githubUtils.getFilesUtils().getCommitFiles(repositoryName, sha);
    }
}
