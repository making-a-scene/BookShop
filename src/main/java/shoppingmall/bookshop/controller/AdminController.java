package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.bookshop.dto.account.AdminRegisterDto;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @RequestMapping(value = "/v1/auth/join", method = RequestMethod.POST)
    public ResponseEntity<User> adminJoin(AdminRegisterDto adminRegisterDto) {

        User admin = adminRegisterDto.toEntity();
        userService.register(admin);
        return new ResponseEntity<User>(admin, HttpStatus.OK);
    }
}
