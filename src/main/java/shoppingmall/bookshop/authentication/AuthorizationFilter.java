package shoppingmall.bookshop.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
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
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/oauth2/authorization/naver") || request.getServletPath().equals("/oauth2/authorization/kakao")) {
            log.info("토큰 발급 전이므로 AuthorizationFilter를 거치지 않습니다.");

            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String TOKEN_PREFIX = "Bearer ";

            log.info("요청 헤더에서 access token 정보를 받아 옵니다.");
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());

            log.info("토큰의 유효성을 검증합니다.");
            if(jwtTokenProvider.validateToken(token, response)) {
                log.info("토큰이 유효한 경우 Authentication을 SecurityContextHolder에 보관합니다.");
                SecurityContextHolder.getContext().setAuthentication((jwtTokenProvider.getAuthentication(token)));
                new ObjectMapper().registerModule(new JavaTimeModule()).writeValue(response.getOutputStream(), (jwtTokenProvider.getAuthentication(token)));
            } else {
                log.error("인증 실패");

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType((MediaType.APPLICATION_JSON_VALUE));

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("code", HttpStatus.UNAUTHORIZED.value());
                body.put("error", new ExpiredJwtException(jwtTokenProvider.getJwsFromToken(token).getHeader(), jwtTokenProvider.getClaimsFromJwt(token), "refresh token 만료"));
                new ObjectMapper().writeValue(response.getOutputStream(), body);
            }
        }
    }
}
