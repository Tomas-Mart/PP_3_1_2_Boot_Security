package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Главная страница
        registry.addViewController("/").setViewName("index");

        // Страницы для администратора и пользователя
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/user").setViewName("user");

        // Страница логина
        registry.addViewController("/login").setViewName("login");

        // Страница редактирования пользователя
        registry.addViewController("/edit-user").setViewName("edit-user");

        // Страница ошибки 403 (доступ запрещен)
        registry.addViewController("/403").setViewName("403");
    }
}