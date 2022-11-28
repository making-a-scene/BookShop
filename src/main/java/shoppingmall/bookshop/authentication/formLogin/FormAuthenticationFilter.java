package shoppingmall.bookshop.authentication.formLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.authentication.JwtTokenProvider;
import shoppingmall.bookshop.authentication.TokenDto;
import shoppingmall.bookshop.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class FormAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("사용자가 입력한 아이디와 비밀번호를 http request에서 받아 옵니다.");
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        log.info("UsernamePasswordAuthenticationToken을 authenticationManager에 전달해 인증을 수행토록 합니다.");

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
    }

    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        log.info("로그인 인증이 성공한 회원에 대해 jwt 토큰을 발급합니다.");
        final UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String userId = userDetails.getUsername();
        final TokenDto tokenDto = jwtTokenProvider.getToken(userId);

        log.info("refresh token은 해당 유저 엔티티에 저장해 둡니다.");
        userService.setRefreshToken(userId, tokenDto.getRefreshToken());

        log.info("access token은 response header의 Authorization에 담아 클라이언트에 보냅니다.");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), response.getHeaders(HttpHeaders.AUTHORIZATION));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("unsuccessfulAuthentication exception.getLocalizedMessage(): {}", failed.getLocalizedMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", HttpStatus.UNAUTHORIZED.value());
        body.put("error", failed.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
