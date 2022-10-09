package shoppingmall.bookshop.authentication.socialLogin;

import org.springframework.security.oauth2.core.user.OAuth2User;
import shoppingmall.bookshop.authentication.Role;

import java.util.Map;

public interface OAuth2UserInfo extends OAuth2User {
    Map<String, Object> getAttributes();
    String getProviderId();
    Provider getProvider();
    String getOAuth2Id();
    String getEmail();
    Role getRole();
    String getNickname();

}

