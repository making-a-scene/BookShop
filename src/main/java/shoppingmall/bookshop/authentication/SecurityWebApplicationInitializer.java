package shoppingmall.bookshop.authentication;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

// Security Filter Chain 설정을 웹 애플리케이션에 적용
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }

}
