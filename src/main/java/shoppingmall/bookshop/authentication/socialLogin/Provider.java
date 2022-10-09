package shoppingmall.bookshop.authentication.socialLogin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    NAVER("naver", "네이버 회원"),
    KAKAO("kakao", "카카오 회원");

    private final String key;
    private final String title;

    public static Provider getEnumClass(String key) {
        switch (key) {
            case "naver" -> {
                return NAVER;
            }
            case "kakao" -> {
                return KAKAO;
            }

        }
        return null;

    }
}


