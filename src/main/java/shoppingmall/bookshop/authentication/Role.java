package shoppingmall.bookshop.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 회원"),
    SUPER("ROLE_SUPER", "관리자");

    private final String key;
    private final String title;
}
