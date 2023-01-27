package org.example.controllers.github;

import org.example.controllers.responses.RepositoryResponse;
import org.example.github.GithubUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("repositories")
public class RepositoriesController {

    @Autowired
    private GithubUtils githubUtils;

    @GetMapping("/all")
    public RepositoryResponse getRepositories() throws IOException {

        return githubUtils.getAllRepositories();
    }

    @GetMapping("/{name}")
    public RepositoryResponse getRepository(@PathVariable String repositoryName) throws IOException {

        return githubUtils.getRepository(repositoryName);
    }

    @GetMapping("/collaborators/add")
    public RepositoryResponse getRepository(@PathVariable String repositoryName,
                                            @ModelAttribute UserPermissionMapWrapper collaborators) throws IOException {

        return githubUtils.addCollaboratorsToRepository(repositoryName, collaborators.getTags());
    }
}
