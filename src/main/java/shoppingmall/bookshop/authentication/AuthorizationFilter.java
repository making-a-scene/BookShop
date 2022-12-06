package shoppingmall.bookshop.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("요청 헤더에서 access token 정보를 받아 옵니다.");
        String TOKEN_PREFIX = "Bearer ";
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());

        if(request.getServletPath().equals("/api/login")) {
            log.info("로그인 전이므로 AuthorizationFilter를 거치지 않습니다.");
            filterChain.doFilter(request, response);
        } else {
            log.info("토큰의 유효성을 검증합니다.");
            if(jwtTokenProvider.validateToken(token, response)) {
                log.info("토큰이 유효한 경우 Authentication을 SecurityContextHolder에 보관합니다.");
                SecurityContextHolder.getContext().setAuthentication((jwtTokenProvider.getAuthentication(token)));
                new ObjectMapper().registerModule(new JavaTimeModule()).writeValue(response.getOutputStream(), (jwtTokenProvider.getAuthentication(token)));
                filterChain.doFilter(request, response);
            } else {
                log.error("유효하지 않은 토큰이므로 인증에 실패하였습니다.");

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType((MediaType.APPLICATION_JSON_VALUE));

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("code", HttpStatus.UNAUTHORIZED.value());
                body.put("error", new ExpiredJwtException(jwtTokenProvider.getJwsFromToken(token).getHeader(), jwtTokenProvider.getClaimsFromJwt(token), "refresh token 만료"));
                new ObjectMapper().writeValue(response.getOutputStream(), body);
            }
        }

    }
    // SecurityContextHolder에 이미 인증 정보가 있는 경우 인증을 완료하고 리다이렉트 처리하는 기능.

//    private boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || AnonymousAuthenticationToken.class.
//                isAssignableFrom(authentication.getClass())) {
//            return false;
//        }
//        return authentication.isAuthenticated();
//    }
}
