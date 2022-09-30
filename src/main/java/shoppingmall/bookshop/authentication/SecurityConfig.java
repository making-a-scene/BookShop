package shoppingmall.bookshop.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity // spring security filter를 spring filter chain에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {


    // HttpSecurity : http 요청이 발생했을 때 적용할 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated()
                    .antMatchers("/super/**").access("hasRole('ROLE_SUPER')")
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .loginPage("/loginForm")
                    .loginProcessingUrl("login")
                    .defaultSuccessUrl("/")
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .and()
                .oauth2Login()
                    .loginPage("/loginForm")
                    .userInfoEndpoint()
                    .and()
                .csrf()
                    .disable();

        return httpSecurity.build(); // spring security filter 생성, 스프링 빈으로 등록

    }



}
