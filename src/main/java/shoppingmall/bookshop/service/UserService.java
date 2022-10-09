package shoppingmall.bookshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public User findUserByoAuth2Id(String oAuth2Id) {
        return userRepository.findByOAuth2Id(oAuth2Id);
    }

    @Transactional
    public void register(User user) {
        userRepository.save(user);
    }


}
