package com.creavispace.project.config.auth;

import com.creavispace.project.config.JwtFilter;
import com.creavispace.project.domain.member.Role;
import com.creavispace.project.domain.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
@Component
public class SecurityConfig {
    private final CustomOauth2Service customOauth2Service;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final MemberService memberService;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HttpSession session) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(httpSecurityCorsConfigurer -> corsFilter()).httpBasic(
                        AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers("/api/auth/**","config/login", "/login/**", "member/login", "/join", "/swagger-ui/**", "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/community/**", "/hashtag/**", "/project/**", "/comment/**", "/recruit/**", "/bookmark/**", "/like/**", "/search/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/member/**", "/review", "/file/**", "/like/**","/project/**", "/comment/**", "/recruit/**", "/bookmark/**", "/report/**")
                                .hasRole(Role.MEMBER.name()).anyRequest()
                                .authenticated())
                .logout(logout -> logout.logoutSuccessHandler(new LogoutHandler()).logoutUrl("/logout"))
                .oauth2Login(login -> login.loginPage("/").userInfoEndpoint(endPoint -> endPoint.userService(customOauth2Service)).successHandler(new LoginSuccessHandler(memberService, session)))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(memberService, jwtSecret, session), UsernamePasswordAuthenticationFilter.class)
                /*.exceptionHandling(exception -> exception
                                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/"))
                        // 시작 페이지로 리디렉션
                )*/;
        System.out.println("SecurityConfig.filterChain");

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        System.out.println("SecurityConfig.corsFilter");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("https://creavispace.vercel.app",
                        "localhost:8080/oauth2/authorization/naver",
                        "https://port-0-creavispace-backend-am952nlsse11uk.sel5.cloudtype.app/login/oauth2/code/naver"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration(),
                this.naverClientRegistration());
    }

    private ClientRegistration naverClientRegistration() {
        return ClientRegistration.withRegistrationId("naver")
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .redirectUri("https://creavispace.vercel.app/login")
                .scope("name", "email")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .clientName("Naver")
                .userNameAttributeName("response")
                .build();
    }

    private ClientRegistration googleClientRegistration() {

        return ClientRegistration.withRegistrationId("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("https://creavispace.vercel.app/login")
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }
}
