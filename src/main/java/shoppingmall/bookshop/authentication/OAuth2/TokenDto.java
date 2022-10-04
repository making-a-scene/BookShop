package shoppingmall.bookshop.authentication.OAuth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
