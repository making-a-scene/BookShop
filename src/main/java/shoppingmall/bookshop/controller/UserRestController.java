package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import shoppingmall.bookshop.authentication.JwtTokenProvider;
import shoppingmall.bookshop.authentication.formLogin.FormAuthenticationFilter;
import shoppingmall.bookshop.dto.LoginRequestDto;
import shoppingmall.bookshop.dto.UserRegisterDto;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    @RequestMapping (value = "/register", method = RequestMethod.POST)
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


}
