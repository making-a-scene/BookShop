package shoppingmall.bookshop.authentication.socialLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import shoppingmall.bookshop.authentication.TokenDto;
import shoppingmall.bookshop.authentication.JwtTokenProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        final String oAuth2Id = String.valueOf(authentication.getPrincipal());
        final TokenDto tokenDto = jwtTokenProvider.getToken(oAuth2Id);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("access_token", tokenDto.getAccessToken());
        body.put("refresh_token", tokenDto.getRefreshToken());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.sendRedirect("/user");

        new ObjectMapper().writeValue(response.getOutputStream(), body);

    }
}
