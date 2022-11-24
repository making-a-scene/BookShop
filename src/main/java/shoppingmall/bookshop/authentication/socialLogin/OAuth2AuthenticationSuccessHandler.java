//package shoppingmall.bookshop.authentication.socialLogin;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import shoppingmall.bookshop.authentication.JwtTokenProvider;
//import shoppingmall.bookshop.authentication.PrincipalDetails;
//import shoppingmall.bookshop.authentication.TokenDto;
//
//import javax.servlet.http.HttpServletResponse;
//
//@RequiredArgsConstructor
//@Slf4j
//public class OAuth2AuthenticationSuccessHandler {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    public String onAuthorizationSuccess(Authentication principal, HttpServletResponse response) {
//        log.info("로그인 인증이 성공한 회원에 대해 jwt 토큰을 발급합니다.");
//        final PrincipalDetails principalDetails = (PrincipalDetails) principal.getPrincipal();
//        String userId = principalDetails.getUsername();
//        final TokenDto tokenDto = jwtTokenProvider.getToken(userId);
//    }
//}
