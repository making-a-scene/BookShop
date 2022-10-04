package shoppingmall.bookshop.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shoppingmall.bookshop.authentication.OAuth2.OAuth2SuccessHandler;
import shoppingmall.bookshop.authentication.OAuth2.OAuth2UserServiceImpl;
import shoppingmall.bookshop.authentication.jwt.JwtAuthenticationFilter;
import shoppingmall.bookshop.authentication.jwt.JwtTokenFilter;
import shoppingmall.bookshop.authentication.jwt.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // spring security filter를 spring filter chain에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserServiceImpl oAuth2UserServiceImpl;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(principalDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    // HttpSecurity : http 요청이 발생했을 때 적용할 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        JwtTokenFilter customFilter = new JwtTokenFilter((jwtTokenProvider));
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable()
                .httpBasic().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/super/**").access("hasRole('ROLE_SUPER')")
                .anyRequest().permitAll()
                .and()
        .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
        .formLogin()
                .usernameParameter("userId")
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // 해당 url에 접속되면 spring security가 대신 로그인을 해준다.
                .defaultSuccessUrl("/")
                .and()
        .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
        .oauth2Login()
                .loginPage("/oauth2/login")
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(oAuth2UserServiceImpl);

        return http.build(); // spring security filter 생성, 스프링 빈으로 등록

    }

}
