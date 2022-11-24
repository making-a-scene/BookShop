package shoppingmall.bookshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

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


    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void register(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void setRefreshToken(String userId, String token) {

        Supplier<Optional<User>> supplier = () -> findUserByUserId(userId);
        Optional<User> nullableUser = Optional.ofNullable(findUserByUserId(userId)).orElseGet(supplier);
        User user = nullableUser.orElseThrow(
                () -> {throw new NoSuchElementException("해당 회원이 존재하지 않습니다.");}
        );

        user.setRefreshToken(token);
    }

}
