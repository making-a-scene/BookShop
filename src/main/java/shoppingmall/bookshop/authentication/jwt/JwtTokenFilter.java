package shoppingmall.bookshop.authentication.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// http request마다 1회만 실행되는 필터.
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal (
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // http 헤더에서 요청 클라이언트의 토큰 받아오기
        String token = jwtTokenProvider.resolveToken(request);

        try {
            // 토큰이 존재하고 유효하다면 해당 토큰의 인증 정보를 SecurityContextHolder에 저장
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch(Exception e) {
            System.out.println("다시 로그인해주세요.");
        }
        filterChain.doFilter(request, response); // 다음 필터 체인 실행
    }
}
