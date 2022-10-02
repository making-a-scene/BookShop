package shoppingmall.bookshop.authentication;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import shoppingmall.bookshop.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// login processing url인 "/login"에 접속되면 spring security가 낚아채서 이 클래스의 객체로 로그인을 수행해준다.
@NoArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;


    // 일반 로그인 constructor
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2 로그인 constructor
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //// OAuth2User implementation ////
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }


    //// UserDetails implementation ////
    @Override
    // 해당 User가 가지고 있는 권한 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole().getKey());
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
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
