package org.example.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.github.GithubUtils;
import org.example.github.dto.GHContentDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("commits")
public class CommitController {
    private final GithubUtils githubUtils;

    @GetMapping(value = "/files", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public List<GHContentDTO> getFiles(@RequestParam String repositoryName, @RequestParam String sha) throws IOException {

        return githubUtils.getCommitFiles(repositoryName, sha);
    }
}
