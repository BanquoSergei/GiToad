package org.example.controllers;

import org.example.controllers.github.requests.OnlyTokenRequest;
import org.example.controllers.responses.LogicalStateResponse;
import org.example.controllers.responses.MySelfResponse;
import org.example.controllers.responses.RegistrationResponse;
import org.example.data.github.utils.GithubUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@CrossOrigin(allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST}, allowCredentials = "true", originPatterns = "*")
@RequestMapping("account")
@RestController
public class AccountController {

    @Autowired
    private GithubUtils githubUtils;


    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> updateAccountData(@RequestBody OnlyTokenRequest request, @RequestHeader("Auth-token") String jwt) {


        return githubUtils.getAccountUtils().updateData(jwt, request.token());
    }

    @GetMapping("/myself")
    public ResponseEntity<MySelfResponse> getMyself() throws IOException {

        return githubUtils.getAccountUtils().getMyself();
    }

    @PostMapping("/login")
    public ResponseEntity<LogicalStateResponse> login(@RequestHeader("Auth-token") String jwt)  {

        return githubUtils.setup(jwt);
    }

    @GetMapping("/exists")
    public ResponseEntity<LogicalStateResponse> exists(@RequestParam String token) {

        return githubUtils.getAccountUtils().exists(token);
    }
}
