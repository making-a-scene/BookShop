package shoppingmall.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    private String userId;
    private String oAuth2Id;
    private String password;
    private String nickname;
    private String email;

    public void setPassword(String encodedPw) {
        this.password = encodedPw;
    }

    public User toEntity() {

        return  User.builder()
                .userId(userId)
                .oAuth2Id(oAuth2Id)
                .password(password)
                .email(email)
                .nickname(nickname)
                .createdAt(LocalDate.now())
                .role(Role.ROLE_USER)
                .build();
    }
}
