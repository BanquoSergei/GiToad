package org.example.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.Context;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.example.request_processing.interceptors.AuthInterceptor;
import org.example.utils.jwt.JwtUtil;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebConfiguration implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

        security.csrf().disable().cors().disable();

        return security.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthInterceptor(jwtUtil)).addPathPatterns("/**")
                .excludePathPatterns(
                        "/error",
                        "/account/login",
                        "/account/exists",
                        "/account/registration",
                        "/test/test",
                        "/security/interactionKey"
                ).pathMatcher(new AntPathMatcher()).order(1);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                Rfc6265CookieProcessor rfc6265Processor = new Rfc6265CookieProcessor();
                rfc6265Processor.setSameSiteCookies("None");
                context.setCookieProcessor(rfc6265Processor);
            }
        };
    }
}
