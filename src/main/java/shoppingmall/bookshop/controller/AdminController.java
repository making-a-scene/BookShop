package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.bookshop.dto.AdminRegisterDto;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @RequestMapping(value = "/join/secret")
    public ResponseEntity<User> adminJoin(AdminRegisterDto adminRegisterDto) throws IllegalAccessException {

        final String secretCode = passwordEncoder.encode("hf7yw2cf0uq49gfk1j");

        if(adminRegisterDto.getSecretCode().equals(secretCode)) {
            User admin = adminRegisterDto.toEntity();
            userService.register(admin);
            return new ResponseEntity<User>(admin, HttpStatus.OK);
        } else {
            throw new IllegalAccessException("입력하신 관리자용 코드가 정확하지 않아 가입하실 수 없습니다.");
        }



    }
}
