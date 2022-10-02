package shoppingmall.bookshop.authentication.OAuth2;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;

    private String nameAttributeKey;
    private String nickname;
    private String email;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId))  return ofNaver("id", attributes);

        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .nickname((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .createdAt(LocalDate.now())
                .build();
    }
}
