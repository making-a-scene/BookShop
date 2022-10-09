package shoppingmall.bookshop.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("login") || request.getServletPath().equals("oauth2/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String TOKEN_PREFIX = "Bearer ";

            if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                String token = authorizationHeader.substring("Bearer ".length());
                try {
                    jwtTokenProvider.validateToken(token);
                    SecurityContextHolder.getContext().setAuthentication((jwtTokenProvider.getAuthentication(token)));
                    response.sendRedirect("/user");

                } catch(JwtException e) {
                    log.error("Fail Decode Authorization Token");

                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType((MediaType.APPLICATION_JSON_VALUE));

                    Map<String, Object> body = new LinkedHashMap<>();
                    body.put("code", HttpStatus.UNAUTHORIZED.value());
                    body.put("error", e.getMessage());

                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
