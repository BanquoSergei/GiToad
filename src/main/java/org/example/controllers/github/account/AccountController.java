package org.example.controllers.github.account;

import org.example.controllers.responses.LogicalStateResponse;
import org.example.controllers.responses.RegistrationResponse;
import org.example.github.utils.GithubUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequestMapping("account")
@RestController
public class AccountController {

    @Autowired
    private GithubUtils githubUtils;


    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> updateAccountData(@RequestBody(required = false) UserDataDTO data) {


        return githubUtils.getAccountUtils().updateData(data);
    }

    @PostMapping("/login")
    public ResponseEntity<LogicalStateResponse> login(@RequestParam String id,
                                                      @RequestParam(required = false, defaultValue = "JWT") String by)  {

        return githubUtils.setup(id, by);
    }

    @GetMapping("/exists")
    public ResponseEntity<LogicalStateResponse> exists(@RequestParam String id) {

        return githubUtils.getAccountUtils().exists(id);
    }
}
