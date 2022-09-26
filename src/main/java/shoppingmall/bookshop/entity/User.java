package shoppingmall.bookshop.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PROTECTED)
@Builder
public class User {

    @Id @GeneratedValue
    private Long id;

    private String social;

    private String email;

    private String refreshToken;

    private String nickname;

    private LocalDate createdAt;

    private Date updatedAt;

    private Role role;

}
