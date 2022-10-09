package shoppingmall.bookshop.authentication.socialLogin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import shoppingmall.bookshop.authentication.Role;

import java.util.Map;

@Getter
@Builder
public class OAuth2Attributes {

    private Map<String, Object> attributes;
    private String userNameAttribute;
    private String oAuth2Id;
    private String nickname;
    private String email;
    private Role role;


    public static OAuth2UserInfo of(String provider, String userNameAttribute, Map<String, Object> attributes) {


        switch (provider) {
            case "naver" -> {
                OAuth2Attributes oAuth2Attributes = OAuth2Attributes.ofNaver(userNameAttribute, attributes);
                return new NaverUserInfo(oAuth2Attributes.getAttributes());
            }
            case "kakao" -> {
                OAuth2Attributes oAuth2Attributes = OAuth2Attributes.ofKakao(userNameAttribute, attributes);
                return new KakaoUserInfo(oAuth2Attributes.getAttributes());
            }
        }
        return null;
    }

    private static OAuth2Attributes ofNaver(String userNameAttribute, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .oAuth2Id((String) response.get("email"))
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .userNameAttribute(userNameAttribute)
                .attributes(response)
                .role(Role.ROLE_USER)
                .build();
    }

    private static OAuth2Attributes ofKakao(String userNameAttribute, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .oAuth2Id((String) kakaoAccount.get("email"))
                .nickname((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .userNameAttribute(userNameAttribute)
                .role(Role.ROLE_USER)
                .build();
    }


}
