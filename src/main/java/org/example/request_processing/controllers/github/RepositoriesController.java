package org.example.request_processing.controllers.github;

import org.example.request_processing.requests.CreateRepositoryRequest;
import org.example.request_processing.responses.LogicalStateResponse;
import org.example.request_processing.responses.RepositoriesResponse;
import org.example.request_processing.responses.RepositoryResponse;
import org.example.request_processing.wrappers.UserPermissionMapWrapper;
import org.example.utils.github.GithubUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
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
    public ResponseEntity<RepositoryResponse> getRepository(@RequestParam("name") String repositoryName, @RequestParam(required = false) String branch) throws IOException {

        return githubUtils.getRepositoriesUtils().getRepository(repositoryName, branch);
    }

    @PutMapping("/create")
    public ResponseEntity<LogicalStateResponse>  createRepository(
            @RequestBody CreateRepositoryRequest repositoryInfo
            ) throws IOException {

        return githubUtils.getRepositoriesUtils().createRepository(repositoryInfo);
    }

    @GetMapping("/collaborators/add")
    public ResponseEntity<LogicalStateResponse> addCollaborators(@PathVariable String repositoryName,
                                            @ModelAttribute UserPermissionMapWrapper collaborators) throws IOException {

        return githubUtils.getRepositoriesUtils().addCollaboratorsToRepository(repositoryName, collaborators.getTags());
    }

    @PostMapping("/setup")
    public ResponseEntity<LogicalStateResponse> setup(@RequestBody CreateRepositoryRequest repositoryInfo)throws IOException {

        return githubUtils.getRepositoriesUtils().setupRepository(repositoryInfo);
    }
}
