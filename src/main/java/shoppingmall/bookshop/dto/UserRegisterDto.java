package shoppingmall.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shoppingmall.bookshop.entity.Role;
import shoppingmall.bookshop.entity.User;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    private String email;

    private String nickname;

    public User toEntity(String email, String nickname) {
        return  User.builder()
                .email(email)
                .nickname(nickname)
                .createdAt(LocalDate.now())
                .role(Role.USER)
                .build();
    }
}
