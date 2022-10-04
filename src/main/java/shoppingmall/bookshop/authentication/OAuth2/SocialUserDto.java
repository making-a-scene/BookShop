package shoppingmall.bookshop.authentication.OAuth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shoppingmall.bookshop.authentication.Role;

@Getter
@Builder
@AllArgsConstructor
public class SocialUserDto {

    private String nickname;
    private String email;
    private Role role;
}
