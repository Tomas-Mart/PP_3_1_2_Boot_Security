package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Получаем роли пользователя
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Логика перенаправления в зависимости от роли
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin"); // Перенаправление для администратора
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/user"); // Перенаправление для обычного пользователя
        } else {
            response.sendRedirect("/"); // Перенаправление для всех остальных (например, анонимных пользователей)
        }
    }
}
