package shoppingmall.bookshop.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import shoppingmall.bookshop.entity.User;


import java.util.*;

@NoArgsConstructor
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    // 일반 로그인 constructor
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2 로그인 constructor
    public PrincipalDetails(User user, OAuth2User oAuth2User) {
        this.user = user;
        this.attributes = oAuth2User.getAttributes();
    }


    //// OAuth2User implementation ////
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //// UserDetails implementation ////
    @Override
    // 해당 User가 가지고 있는 권한 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole().value();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getName() {
        if(user.getUserId() == null) {
            return user.getOAuth2Id();
        }
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        if(user.getUserId() == null) {
            return user.getOAuth2Id();
        }
        return user.getUserId();

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
