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
import shoppingmall.bookshop.authentication.formLogin.FormUserDetailsService;
import shoppingmall.bookshop.authentication.socialLogin.SocialUserService;
import shoppingmall.bookshop.service.UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true) // spring security filter를 spring filter chain에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final FormUserDetailsService formUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final SocialUserService socialUserService;

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

        authProvider.setUserDetailsService(formUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // HttpSecurity : http 요청이 발생했을 때 적용할 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new AuthorizationFilter(jwtTokenProvider),UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
        http.httpBasic().disable();
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
        .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
        .formLogin()
                .usernameParameter("userId")
                .defaultSuccessUrl("/user")
                .successHandler(new CustomAuthenticationSuccessHandler(jwtTokenProvider, userService))
                .failureHandler(new CustomAuthenticationFailureHandler())
                .and()
        .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/user")
                .and()
        .oauth2Login()
                .loginPage("/oauth2/login")
                .successHandler(new CustomAuthenticationSuccessHandler(jwtTokenProvider, userService))
                .failureHandler(new CustomAuthenticationFailureHandler())
                .userInfoEndpoint().userService(socialUserService);

        return http.build(); // spring security filter 생성, 스프링 빈으로 등록

    }

}
