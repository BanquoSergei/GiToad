package org.example.controllers.github;

import org.example.controllers.responses.LogicalStateResponse;
import org.example.controllers.responses.RepositoriesResponse;
import org.example.controllers.responses.RepositoryResponse;
import org.example.github.utils.GithubUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("repositories")
public class RepositoriesController {

    @Autowired
    private GithubUtils githubUtils;

    @GetMapping("/all")
    public ResponseEntity<RepositoriesResponse> getRepositories() throws IOException {

        return githubUtils.getRepositoriesUtils().getAllRepositories();
    }

    @GetMapping("/get")
    public ResponseEntity<RepositoryResponse> getRepository(@RequestParam("name") String repositoryName) throws IOException {

        return githubUtils.getRepositoriesUtils().getRepository(repositoryName);
    }

    @PutMapping("/create")
    public ResponseEntity<LogicalStateResponse>  createRepository(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String homepage,
            @RequestParam(required = false) String defaultBranch,
            @RequestParam(required = false) boolean autoInit,
            @RequestParam(required = false) boolean downloadsEnable,
            @RequestParam(required = false) boolean issuesEnable,
            @RequestParam(required = false) boolean isPrivate,
            @RequestParam(required = false) boolean isTemplate
    ) throws IOException {

        return githubUtils.getRepositoriesUtils().createRepository(
                name, description, homepage, defaultBranch,
                autoInit, downloadsEnable, issuesEnable, isPrivate, isTemplate
        );
    }

    @GetMapping("/collaborators/add")
    public ResponseEntity<LogicalStateResponse> addCollaborators(@PathVariable String repositoryName,
                                            @ModelAttribute UserPermissionMapWrapper collaborators) throws IOException {

        return githubUtils.getRepositoriesUtils().addCollaboratorsToRepository(repositoryName, collaborators.getTags());
    }

    @PostMapping("/setup")
    public ResponseEntity<LogicalStateResponse> setup(@RequestParam("repositoryName") String repositoryName,
                                    @RequestParam(required = false) String renameTo,
                                    @RequestParam(required = false) String homeUrl,
                                    @RequestParam(required = false) String emailHook,
                                    @RequestParam(required = false) String defaultBranch,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) Boolean isPrivate) throws IOException {

        return githubUtils.getRepositoriesUtils().setupRepository(repositoryName, renameTo, homeUrl, emailHook, defaultBranch, description, isPrivate);
    }
}
