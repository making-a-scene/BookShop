package shoppingmall.bookshop.authentication.OAuth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shoppingmall.bookshop.authentication.PrincipalDetails;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;
import shoppingmall.bookshop.service.UserService;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }

        String provider = Objects.requireNonNull(oAuth2UserInfo).getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = passwordEncoder.encode("1234 ");
        String email = oAuth2UserInfo.getEmail();
        Role role = Role.USER;

        Optional<User> user = userRepository.findByUserId(username);

        if(user.isEmpty()) {
            User newUser =
                    User.builder()
                    .userId(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userService.register(newUser);

            return new PrincipalDetails(newUser, oAuth2User.getAttributes());
        }
        return new PrincipalDetails(user.get(), oAuth2User.getAttributes());
    }
}
