package org.example.controllers.github;

import lombok.RequiredArgsConstructor;
import org.example.controllers.responses.Response;
import org.example.data.SecurityData;
import org.example.exceptions.InvalidInteractionKeyException;
import org.example.github.GithubUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final SecurityData data;
    private final GithubUtils githubUtils;


    @PostMapping("/update")
    public Response updateAccountData(@RequestParam String id,
                                      @RequestParam("interaction_key") String interactionKey,
                                      @RequestParam(name = "jwt_token", required = false) String jwtToken,
                                      @RequestParam(name = "installation_token", required = false) String installationToken,
                                      @RequestParam(required = false) String username,
                                      @RequestParam(required = false) String password,
                                      @RequestParam(name = "oauth_token", required = false) String oauthToken) throws InvalidInteractionKeyException {

//        if(!data.checkInteractionKey(interactionKey))
//            throw new InvalidInteractionKeyException();

        return githubUtils.updateData(id, username, password, jwtToken, installationToken, oauthToken);
    }

    @PostMapping("/login")
    public Response login(@RequestParam String id,
                          @RequestParam("interaction_key") String interactionKey,
                          @RequestParam(value = "by", required = false) String by) throws InvalidInteractionKeyException, IOException {

//        if(!data.checkInteractionKey(interactionKey))
//            throw new InvalidInteractionKeyException();

        githubUtils.setup(id, by);

        return Response.success();
    }
}
