package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shoppingmall.bookshop.dto.UserRegisterDto;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

@Controller
@RequiredArgsConstructor
public class SigninController {

    private final UserService userService;

    @GetMapping ("/user/new")
    public String signinPage() {
        return "Signin";
    }

    @PostMapping("/user/register")
    public String register(@RequestBody UserRegisterDto userRegisterDto) {
        User user = userRegisterDto.toEntity();
        userService.register(user);
        return "redirect:/";

    }
}
