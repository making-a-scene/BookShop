package shoppingmall.bookshop.authentication.OAuth2;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRequestMapper {
    public SocialUserDto getUser(OAuth2User oAuth2User) {

       Map<String, Object> attributes = oAuth2User.getAttributes();
       return SocialUserDto.builder()
               .nickname((String) attributes.get("nickname"))
               .email((String) attributes.get("email"))
               .build();
    }
}
