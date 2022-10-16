package shoppingmall.bookshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
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

    @Transactional
    public void setRefreshToken(String userId, String token) {

        findUserByUserId(userId).ifPresentOrElse(
                (user) -> {
                    user.setRefreshToken(token);
                    register(user);},
                () ->  {findUserByoAuth2Id(userId).setRefreshToken(token);
                    register(findUserByoAuth2Id(userId));}
        );

    }

}
