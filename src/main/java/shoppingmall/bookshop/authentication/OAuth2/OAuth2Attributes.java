package shoppingmall.bookshop.authentication.OAuth2;

import lombok.Builder;
import lombok.Getter;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class OAuth2Attributes {

    private Map<String, Object> attributes;

    private String nameAttributeKey;
    private String nickname;
    private String email;

    public static OAuth2Attributes of(String provider, String attributeKey, Map<String, Object> attributes) {
        if("naver".equals(provider))  return ofNaver(attributeKey, attributes);

        return ofKakao(attributeKey, attributes);
    }

    private static OAuth2Attributes ofNaver(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(attributeKey)
                .build();
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .nickname((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nameAttributeKey);
        map.put("key", nameAttributeKey);
        map.put("nickname", nickname);
        map.put("email", email);

        return map;
    }


}
