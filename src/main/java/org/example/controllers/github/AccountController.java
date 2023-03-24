package org.example.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.ExistsResponse;
import org.example.controllers.responses.Response;
import org.example.data.SecurityData;
import org.example.github.utils.GithubUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final GithubUtils githubUtils;


    @PostMapping("/update")
    public Response updateAccountData(@RequestParam String id,
                                      @RequestParam(name = "jwt_token", required = false) String jwtToken,
                                      @RequestParam(name = "installation_token", required = false) String installationToken,
                                      @RequestParam(required = false) String username,
                                      @RequestParam(required = false) String password,
                                      @RequestParam(name = "oauth_token", required = false) String oauthToken) {


        return githubUtils.getAccountUtils().updateData(id, username, password, jwtToken, installationToken, oauthToken);
    }

    @PostMapping("/login")
    public Response login(@RequestParam String id,
                          @RequestParam(value = "by", required = false, defaultValue = "PASSWORD") String by) throws IOException {

        githubUtils.setup(id, by);

        return Response.success();
    }

    @GetMapping("/exists")
    public ExistsResponse exists(@RequestParam String id) {

        return githubUtils.getAccountUtils().exists(id);
    }
}
