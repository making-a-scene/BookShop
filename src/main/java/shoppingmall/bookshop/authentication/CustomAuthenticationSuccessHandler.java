package shoppingmall.bookshop.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import shoppingmall.bookshop.authentication.JwtTokenProvider;
import shoppingmall.bookshop.authentication.TokenDto;
import shoppingmall.bookshop.service.UserService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("로그인 인증이 성공한 회원에 대해 jwt 토큰을 발급합니다.");
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
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

}
