package ch.bbw.pr.tresorbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // don't disable this in prod
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/user/login", "/user/register").permitAll()
                .requestMatchers("/user/users/**").hasRole("ADMIN")
                .anyRequest().hasAnyRole("USER", "ADMIN")
            )
            .httpBasic(httpBasic -> {}); // for testing, use .formLogin() if you want UI form

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
            .username("testuser@example.com")
            .password(passwordEncoder().encode("password123"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("adminuser@example.com")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
