package shoppingmall.bookshop.dto.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDto {
    private final String userId;
    private final String password;
}
