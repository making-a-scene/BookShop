package shoppingmall.bookshop.authentication.socialLogin;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import shoppingmall.bookshop.authentication.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public String getOAuth2Id() {
        return (String) attributes.get("oAuth2Id");

    }

    @Override
    public Role getRole() {
        return Role.ROLE_USER;
    }

    @Override
    public String getEmail() {
        return getOAuth2Id();
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("nickname");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = Role.ROLE_USER.value();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getName() {
        return getEmail();
    }
}
