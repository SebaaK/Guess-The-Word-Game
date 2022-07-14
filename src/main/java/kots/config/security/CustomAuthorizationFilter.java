package kots.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kots.service.JWTManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Log4j2
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final JWTManager jwtManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.startsWith(TOKEN_PREFIX)) {
            try {
                String token = authorization.replace(TOKEN_PREFIX, "");
                SecurityContextHolder.getContext().setAuthentication(jwtManager.validateToken(token));
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
                log.warn("Error while authorization filter. Message: {}", e.getMessage());
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
