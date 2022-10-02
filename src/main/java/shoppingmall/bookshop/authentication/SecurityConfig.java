package shoppingmall.bookshop.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import shoppingmall.bookshop.authentication.OAuth2.OAuth2UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // spring security filter를 spring filter chain에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    // HttpSecurity : http 요청이 발생했을 때 적용할 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated()
                    .antMatchers("/super/**").access("hasRole('ROLE_SUPER')")
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .usernameParameter("userId")
                    .loginPage("/loginForm")
                    .loginProcessingUrl("/login") // 해당 url에 접속되면 spring security가 대신 로그인을 해준다.
                    .defaultSuccessUrl("/")
                    .usernameParameter("username")
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .and()
                .oauth2Login()
                    .loginPage("/loginForm");

        return httpSecurity.build(); // spring security filter 생성, 스프링 빈으로 등록

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
