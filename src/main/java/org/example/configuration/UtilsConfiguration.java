package org.example.configuration;

import org.example.data.SecurityData;
import org.example.data.accounts.AccountService;
import org.example.utils.crypt.Cryptographer;
import org.example.utils.github.GithubUtils;
import org.example.utils.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.SessionScope;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class UtilsConfiguration {

    @Bean
    @Scope(scopeName = "singleton")
    public SecurityData data(@Autowired Cryptographer cryptographer, @Value("${secret_key}") String secret, @Autowired JwtUtil jwtUtil) {
        return new SecurityData(cryptographer, secret, jwtUtil);
    }

    @Bean
    @Scope(scopeName = "singleton")
    public Cryptographer cryptographer(@Value("${encryption.algorithm.key}") String algorithmKey,
                                       @Value("${encryption.algorithm.cipher}") String algorithmCipher,
                                       @Value("${encryption.key}") String key) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return new Cryptographer(algorithmKey, algorithmCipher, key);
    }

    @Bean
    @Scope("singleton")
    public JwtUtil jwtUtil(@Value("${jwt.secret}") String secret) {

        return new JwtUtil(secret);
    }

    @Bean
    @SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public GithubUtils githubUtils(AccountService accountService, Cryptographer cryptographer, JwtUtil jwtUtil) {

        return new GithubUtils(accountService, cryptographer, jwtUtil);
    }
}
