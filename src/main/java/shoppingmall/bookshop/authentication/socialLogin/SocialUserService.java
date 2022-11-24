package shoppingmall.bookshop.authentication.socialLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shoppingmall.bookshop.authentication.PrincipalDetails;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialUserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private String oAuth2Id;
    private String nickname;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        switch(provider) {
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                oAuth2Id = (String) response.get("email");
                nickname = (String) response.get("nickname");
            }
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                Map<String, Object> kakaoProfile = (Map<String, Object>) oAuth2User.getAttributes().get("profile");

                oAuth2Id = (String) kakaoAccount.get("email");
                nickname = (String) kakaoProfile.get("nickname");

            }
            default -> {
                oAuth2Id = null;
            }
        }

        User user = userService.findUserByEmail(oAuth2Id).orElse(
                User.builder()
                        .oAuth2Id(oAuth2Id)
                        .nickname(nickname)
                        .email(oAuth2Id)
                        .role(Role.ROLE_USER)
                        .provider(Provider.getEnumClass(provider))
                        .build()
        );
        userService.register(user);


        return new PrincipalDetails(user, oAuth2User);

    }
}
