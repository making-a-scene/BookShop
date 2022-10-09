package shoppingmall.bookshop.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
