package shoppingmall.bookshop.authentication.OAuth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import shoppingmall.bookshop.authentication.jwt.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        SocialUserDto socialUserDto = userRequestMapper.getUser(oAuth2User);

        TokenDto tokenDto = jwtTokenProvider.getToken(socialUserDto.getEmail());
        writeTokenResponse(response, tokenDto);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDto tokenDto) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Access", tokenDto.getAccessToken());
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(tokenDto));
        writer.flush();
    }
}
