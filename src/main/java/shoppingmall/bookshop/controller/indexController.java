package shoppingmall.bookshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String authorizedUserIndex() {
        return "userIndex";
    }

    @GetMapping("/super")
    public String superUser() {
        return "super";
    }
}
