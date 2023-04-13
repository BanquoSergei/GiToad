package org.example.request_processing.controllers.security;

import org.example.data.SecurityData;
import org.example.request_processing.exceptions.InvalidInteractionKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("security")
public class KeyManagementController {

    private final SecurityData data;

    @Autowired
    public KeyManagementController(SecurityData data) {
        this.data = data;
    }

    @PostMapping("/interactionKey")
    @ResponseStatus(HttpStatus.OK)
    public void setKey(@RequestParam("secretKey") String secret, @RequestParam("key") String key) throws InvalidInteractionKeyException {

        data.setInteractionKey(secret, key);
    }
}
