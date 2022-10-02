package shoppingmall.bookshop.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

// SecurityConfig.java를 통해 설정한 Security Filter Chain configuration을 웹 애플리케이션에 적용
@Configuration
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {


    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }

}
