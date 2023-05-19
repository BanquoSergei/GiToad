package org.example.request_processing.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.utils.github.GithubUtils;
import org.example.utils.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("OPTIONS") || request.getRequestURI().equals("/error"))
            return true;

        var token = request.getHeader("Authorization");


	GithubUtils githubUtils = (GithubUtils) request.getSession().getAttribute("scopedTarget.githubUtils");

        boolean valid = jwtUtil.validateToken(token);
        if (!valid || !request.getRequestURI().equals("/account/login") && githubUtils == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            if(!valid)
                response.setHeader("Expired", "true");
	
            return false;
        }

        if(!githubUtils.initialized())
            githubUtils.setup(token);

        return valid;
    }
}
