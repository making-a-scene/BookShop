package shoppingmall.bookshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import shoppingmall.bookshop.dto.LoginRequestDto;

@Controller
public class UserController {

    // 회원 가입
    @GetMapping("/new")
    public String join() {
        return "joinForm";
    }

    // 로그인
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/oauth2/login")
    public String socialLogin() {
        return "socialLogin";
    }

}
