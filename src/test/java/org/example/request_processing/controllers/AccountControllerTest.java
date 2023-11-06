package org.example.request_processing.controllers;

import org.example.request_processing.responses.LogicalStateResponse;
import org.example.utils.github.GithubUtils;
import org.example.utils.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@SpringBootTest
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private GithubUtils githubUtils;

    @Autowired
    private JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        String jwt = "jwt-token";

        // Mock the behavior of githubUtils and test the controller method
        when(githubUtils.setup(jwt)).thenReturn(ResponseEntity.ok(new LogicalStateResponse(true)));

        ResponseEntity<LogicalStateResponse> response = accountController.login(jwt);

        verify(githubUtils, times(1)).setup(jwt);
        // Add assertions for the response
    }




}
