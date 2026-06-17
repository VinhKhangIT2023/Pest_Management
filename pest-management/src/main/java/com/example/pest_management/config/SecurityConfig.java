package com.example.pest_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/products/**", "/uploads/**", "/api/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "ROOT_ADMIN")
                        .requestMatchers("/detect", "/cart/**", "/checkout", "/orders").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            // Logic phân luồng sau khi Đăng nhập thành công
                            boolean isRootAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ROOT_ADMIN"));
                            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isRootAdmin) {
                                response.sendRedirect("/admin/dashboard"); // Admin Gốc vào Thống kê
                            } else if (isAdmin) {
                                response.sendRedirect("/admin/orders"); // Admin Con vào Đơn hàng
                            } else {
                                response.sendRedirect("/"); // Khách hàng vào Web
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll());
        return http.build();
    }
}