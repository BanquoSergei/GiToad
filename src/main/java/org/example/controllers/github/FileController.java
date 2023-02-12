package org.example.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.FileResponse;
import org.example.github.GithubUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("files")
public class FileController {

    private final GithubUtils githubUtils;

    @GetMapping
    public FileResponse getFile(@RequestParam String repositoryName,
                                @RequestParam String path) throws IOException {

        return githubUtils.getFile(repositoryName, path);
    }
}
