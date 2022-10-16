package shoppingmall.bookshop.authentication.formLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shoppingmall.bookshop.authentication.PrincipalDetails;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("사용자가 입력한 id 값이 db에 존재하는지 확인합니다.");
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        log.info("db를 통해 조회한 회원 정보를 바탕으로 PrincipalDetails를 생성합니다.");
        return new PrincipalDetails(user);
    }
}
