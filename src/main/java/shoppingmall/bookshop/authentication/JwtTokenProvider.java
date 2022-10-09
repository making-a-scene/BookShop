package shoppingmall.bookshop.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shoppingmall.bookshop.authentication.formLogin.FormUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final long ACCESS_TOKEN_VALID_MILISECOND = 1000L * 60 * 60;
    private final long REFRESH_TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 24 * 14;
    private final FormUserDetailsService formUserDetailsService;

    private String secretKey = "secretkeyyyy12345678901234567890";

    public TokenDto getToken(String userId) {
        return new TokenDto(createAccessToken(userId), createRefreshToken(userId));
    }

    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId); // 토큰의 이름 지정
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILISECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String createRefreshToken(String userId) {
        Claims claims = Jwts.claims().setSubject("RefreshToken"); // 토큰의 이름 지정
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILISECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    // token의 인증 정보 반환
    public Authentication getAuthentication(String token) {
        String userId = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build()
                .parseClaimsJws(token).getBody().getSubject();

        /*
         - Jwts.parserBuilder()
            JWT 토큰은 String 타입으로 생성된다. 이 String 형태의 토큰을 우리가 사용하기 위한 형태로 파싱하기 위해 사용하는 메소드이다.
        - setSigningKey()
            복호화를 위해 해당 토큰을 생성할 때 해싱 알고리즘을 통한 서명에 사용했던 key를 set해주기 위한 메소드이다.
        - parseClaimsJws(token)
            String 타입으로 인코딩되어 있는 토큰을 JWS로 파싱하는 메소드이다.
        - getBody()
           payload에 접근
        - getSubject()
            해당 토큰의 username 반환
         */

        UserDetails userDetails = formUserDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }

    // 만료되지 않은(유효한) 토큰인지 검증
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
        return claims.getBody().getExpiration().after(new Date());
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


}
