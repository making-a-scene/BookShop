package shoppingmall.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;

@Getter
@AllArgsConstructor
public class AdminRegisterDto {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String userId;

    private String password;

    private String secretCode;

    private Role role;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .nickname("운영자1")
                .role(Role.ROLE_SUPER)
                .build();
    }

}
