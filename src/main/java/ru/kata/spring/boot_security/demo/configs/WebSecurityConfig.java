package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login").permitAll() // Разрешить доступ для всех
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Только администраторы
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // Пользователи и администраторы
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница входа
                        .successHandler(successUserHandler()) // Обработчик успешной аутентификации
                        .permitAll() // Разрешить доступ к странице входа всем
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll() // Разрешить выход всем
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Использование BCrypt для хэширования паролей
    }

    @Bean
    public SuccessUserHandler successUserHandler() {
        return new SuccessUserHandler(); // Обработчик успешной аутентификации
    }
}
