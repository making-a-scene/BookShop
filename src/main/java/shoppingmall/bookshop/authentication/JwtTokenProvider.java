package shoppingmall.bookshop.authentication;

import com.nimbusds.oauth2.sdk.auth.ClientSecretJWT;
import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shoppingmall.bookshop.authentication.formLogin.FormUserDetailsService;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final long ACCESS_TOKEN_VALID_MILISECOND = 1000L * 60 * 60;
    private final long REFRESH_TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 24 * 14;
    private final FormUserDetailsService formUserDetailsService;
    private final UserService userService;

    private String secretKey = "secretkeyyyy12345678901234567890";

    public Claims getClaimsFromJwt(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }

    }

    public Jws<Claims> getJwsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
    }
 /*
   - Jwts.parserBuilder()
       JWT 토큰은 String 타입으로 생성된다. 이 String 형태의 토큰을 우리가 사용하기 위한 형태로 파싱하기 위해 사용하는 메소드이다.
   - setSigningKey()
       복호화를 위해 해당 토큰을 생성할 때 해싱 알고리즘을 통한 서명에 사용했던 key를 set해주기 위한 메소드이다.
   - parseClaimsJws(token)
       String 타입으로 인코딩되어 있는 토큰을 JWS로 파싱하는 메소드이다.
   - getBody()
      payload에 접근
 */

    public TokenDto getToken(String userId) {
        return new TokenDto(createAccessToken(userId), createRefreshToken(userId));
    }

    public String createAccessToken(String userId) {
        log.info("access token을 생성합니다.");
        Claims claims = Jwts.claims().setSubject(userId); // 토큰의 이름 지정
        System.out.println(claims.getSubject());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILISECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String createRefreshToken(String userId) {
        log.info("refresh token을 생성합니다.");
        Claims claims = Jwts.claims().setSubject(userId); // 토큰의 이름 지정
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILISECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    // 인증 정보 반환
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = formUserDetailsService.loadUserByUsername(getClaimsFromJwt(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 만료되지 않은(유효한) 토큰인지 검증
    public boolean validateToken(String accessToken, HttpServletResponse response) {
        Claims claims = getClaimsFromJwt(accessToken);
        log.info("access token이 만료되었는지 확인합니다.");
        if (claims.getExpiration().after(new Date())) return true;

        log.info("access token이 만료된 경우 refresh token이 만료되었는지 확인합니다.");

        Supplier<Optional<User>> supplier = () -> userService.findUserByEmail(claims.getSubject());
        Optional<User> nullableUser = Optional.ofNullable(userService.findUserByEmail(claims.getSubject())).orElseGet(supplier);
        User user = nullableUser.orElseThrow(
                () -> {throw new NoSuchElementException("해당 회원이 존재하지 않습니다.");}
        );

        String refreshToken = user.getRefreshToken();
        Claims refreshTokenClaims = getClaimsFromJwt(refreshToken);

        if(refreshTokenClaims.getExpiration().after(new Date())) {
            log.info("refresh token이 유효한 경우 access token을 갱신합니다.");
            response.setHeader(HttpHeaders.AUTHORIZATION, createAccessToken(claims.getSubject()));
            return true;
        } else {
            log.error("refresh token이 만료되었습니다.");
            return false;
        }

    }



}
