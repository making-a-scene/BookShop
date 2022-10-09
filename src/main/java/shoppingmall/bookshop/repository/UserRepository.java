package shoppingmall.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shoppingmall.bookshop.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);

    @Query("select u from User u where u.oAuth2Id = :oAuth2Id")
    User findByOAuth2Id(@Param("oAuth2Id") String oAuth2Id);
}

