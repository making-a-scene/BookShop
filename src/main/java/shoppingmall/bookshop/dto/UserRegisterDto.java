package shoppingmall.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    private PasswordEncoder passwordEncoder;
    private String userId;
    private String password;
    private String nickname;
    private String email;

    public void setPassword(String encodedPw) {
        this.password = encodedPw;
    }

    public User toEntity() {

        passwordEncoder = new BCryptPasswordEncoder();

        return  User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .createdAt(LocalDate.now())
                .role(Role.USER)
                .build();
    }
}
