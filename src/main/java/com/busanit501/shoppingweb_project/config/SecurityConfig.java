package com.busanit501.shoppingweb_project.config;

import com.busanit501.shoppingweb_project.repository.AddressRepository;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import com.busanit501.shoppingweb_project.security.CustomOAuth2UserService;
import com.busanit501.shoppingweb_project.security.handler.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

//    @Bean
//    public CustomOAuth2UserService customOAuth2UserService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
//        return new CustomOAuth2UserService(memberRepository, passwordEncoder, addressRepository);
//    }

    // 비밀번호 암호화에 사용될 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(memberRepository, passwordEncoder(), addressRepository);
    }

    // 보안 필터 체인 설정 (접근 권한, 로그인/로그아웃 설정 등)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 허용할 경로
                        .requestMatchers(HttpMethod.GET, "/api/image/display/{fileName}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/{productId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/{id}").permitAll()
                        .requestMatchers("/home", "/complete-profile","/complete-profile/**", "/signup", "/login", "/css/**", "/js/**", "/static/**", "/webjars/**", "/images/**", "/api/check-id","/api/products").permitAll()
                        .requestMatchers("/mypage/**","/api/members/me", "/api/orders","/api/orders/**","/api/cart/**", "/userInfo-update").authenticated()
                        // 관리자 권한 필요
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecretKey")
                        .rememberMeParameter("remember-Me")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                        .userDetailsService(customUserDetailsService) // UserDetailsService 명확히 지정
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        return http.build();
    }
}
