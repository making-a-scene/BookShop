package shoppingmall.bookshop.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import shoppingmall.bookshop.BaseEntity;
import shoppingmall.bookshop.authentication.Role;
import shoppingmall.bookshop.authentication.socialLogin.Provider;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
@Table(name="userDB")
@Builder
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String oAuth2Id;

    private String userId;

    private String password;

    private String email;

    private String nickname;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}
