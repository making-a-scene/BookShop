package shoppingmall.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shoppingmall.bookshop.dto.UserRegisterDto;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping ("/new")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/register")
    public String register(UserRegisterDto userRegisterDto) {

        String rawPassword = userRegisterDto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        userRegisterDto.setPassword(encPassword);

        User user = userRegisterDto.toEntity();
        userService.register(user);
        return "redirect:/loginForm";

    }
}
