package by.belaya.coworking.config;

import by.belaya.coworking.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}", "/api/users/{userId}/reservations")
                        .hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/users/{userId}/reservations")
                        .hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/workspaces", "/api/workspaces/available",
                                "/api/workspaces/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/reservations").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}", "/api/workspaces/{id}",
                                "/api/reservations/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/workspaces").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}", "/api/workspaces/{id}",
                                "/api/reservations/{id}").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}