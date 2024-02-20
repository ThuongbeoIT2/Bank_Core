package com.example.secumix.security.config;

import com.example.secumix.security.Oauth2.CustomOAuth2UserService;
import com.example.secumix.security.Oauth2.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import reactor.core.publisher.Mono;

import static com.example.secumix.security.user.Permission.*;
import static com.example.secumix.security.user.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-resources", "/configuration/ui", "/configuration/security"

            , "/webjars/**","/api/v1/auth/registrationConfirm.html" ,"/login/oauth2/code/google","/login/oauth2/code/facebook"};

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CustomOAuth2UserService oauth2UserService;
    @Autowired
    @Lazy
    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(req -> req
                        .antMatchers(WHITE_LIST_URL).permitAll()
                        .antMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                        .antMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                        .antMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(),
                                MANAGER_CREATE.name())
                        .antMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(),
                                MANAGER_UPDATE.name())
                        .antMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(),
                                MANAGER_DELETE.name())
                        .anyRequest().authenticated())
               .oauth2Login()
                .userInfoEndpoint()
					.userService(oauth2UserService)
				.and()
				.successHandler(oauthLoginSuccessHandler)
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        // Add JWT authentication filter after OAuth2Login filter
        http.addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}













//    public class OAuth2Config {
//        public static ClientRegistration facebookClientRegistration() {
//
//            var clientRegistration=ClientRegistration.withRegistrationId("facebook")
//                    .clientId("CLIENT_ID")
//                    .clientSecret("511c1cce66db75b519fc74752441d7e0")
//                    .redirectUri("http://localhost:9000/login/oauth2/facebook")
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .authorizationUri("http://localhost:9000/api/v1/auth/facebook")
//                    .tokenUri("https://graph.facebook.com/v12.0/oauth/access_token")
//                    .userInfoUri("https://graph.facebook.com/v12.0/me")
//                    .userNameAttributeName("id")  // You can adjust this based on the Facebook Graph API response
//                    .clientName("Facebook")
//                    .build();
//            System.out.println( clientRegistration.toString());
//            try {
//                return clientRegistration;
//
//            } catch (Exception e) {
//                // Handle the exception appropriately (e.g., log it or throw a custom exception)
//                e.printStackTrace();
//                throw new RuntimeException("Error creating Facebook ClientRegistration", e);
//            }
//        }
//
//        @Bean
//        public InMemoryReactiveClientRegistrationRepository inMemoryReactiveClientRegistrationRepository() {
//            return new InMemoryReactiveClientRegistrationRepository(OAuth2Config.facebookClientRegistration());
//        }
//    }