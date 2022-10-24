package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import shoppingmall.bookshop.authentication.JwtTokenProvider;
import shoppingmall.bookshop.authentication.CustomAuthenticationFailureHandler;
import shoppingmall.bookshop.authentication.CustomAuthenticationSuccessHandler;
import shoppingmall.bookshop.dto.LoginRequestDto;
import shoppingmall.bookshop.dto.UserRegisterDto;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserRestController {
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    @RequestMapping (value = "/api/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@RequestBody UserRegisterDto userRegisterDto){

        log.info("일반 회원가입을 합니다.");
        log.info("사용자가 입력한 비밀번호를 인코딩합니다.");
        String rawPassword = userRegisterDto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        userRegisterDto.setPassword(encPassword);

        log.info("새로운 User 엔티티를 생성합니다.");
        User user = userRegisterDto.toEntity();
        log.info("생성한 User 엔티티를 db에 저장합니다.");
        userService.register(user);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(user, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            log.info("인증을 수행합니다.");
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(), loginRequestDto.getPassword()));
            new CustomAuthenticationSuccessHandler(jwtTokenProvider, userService).onAuthenticationSuccess(request, response, authentication);
        } catch(AuthenticationException e) {
            new CustomAuthenticationFailureHandler().onAuthenticationFailure(request, response, e);
        }
    }

    @RequestMapping(value = "/oauth2/authorization/naver", method = RequestMethod.POST)
    public void naverLogin() {

    }

    @RequestMapping(value = "/oauth2/authorization/kakao", method = RequestMethod.POST)
    public void kakaoLogin() {

    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        return "logout completed";

    }


}
