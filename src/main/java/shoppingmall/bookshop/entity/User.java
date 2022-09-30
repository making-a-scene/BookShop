package shoppingmall.bookshop.entity;

import lombok.*;
import shoppingmall.bookshop.authentication.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
@Table(name="userDB")
@Builder
public class User {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String social;

    private String email;

    private String refreshToken;

    private String nickname;

    private LocalDate createdAt;

    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

}
