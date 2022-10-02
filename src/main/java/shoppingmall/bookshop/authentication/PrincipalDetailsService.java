package shoppingmall.bookshop.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        return new PrincipalDetails(user);
    }
}
