package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationSuccessHandler successUserHandler;

    public WebSecurityConfig(AuthenticationSuccessHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем доступ к корневому пути и статическим ресурсам для всех
                        .requestMatchers("/", "/index", "/login", "/register", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Доступ к админским страницам только для ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Доступ к пользовательским страницам для USER и ADMIN
                        .requestMatchers("/user/**", "/user", "/user-details", "/edit-user").hasAnyRole("USER", "ADMIN")
                        // Доступ к списку пользователей для USER и ADMIN
                        .requestMatchers("/users").hasAnyRole("USER", "ADMIN")
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница входа
                        .successHandler(successUserHandler) // Обработчик успешной аутентификации
                        .permitAll() // Разрешаем доступ к странице входа всем
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll() // Разрешаем доступ к выходу всем
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied") // Страница для ошибки доступа (403)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}