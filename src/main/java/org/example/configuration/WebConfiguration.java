package org.example.configuration;

import lombok.RequiredArgsConstructor;
import org.example.data.SecurityData;
import org.example.interceptors.AuthInterceptor;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {
    private final SecurityData data;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

        security.csrf().disable().cors().disable();

        return security.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtUtil)).excludePathPatterns()
                .addPathPatterns(
                        "/account/login",
                        "/security/interactionKey"
                );
    }
}
