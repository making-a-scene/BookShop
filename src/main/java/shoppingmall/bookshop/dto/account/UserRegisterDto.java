package shoppingmall.bookshop.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
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
        log.info("새로운 회원 객체를 생성합니다.");

        return  User.builder()
                .userId(userId)
                .oAuth2Id(oAuth2Id)
                .password(password)
                .email(email)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();
    }
}
