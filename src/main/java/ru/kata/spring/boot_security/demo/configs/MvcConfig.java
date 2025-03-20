package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index"); // Главная страница
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/edit-user").setViewName("edit-user");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/user").setViewName("user");
        registry.addViewController("/user-details").setViewName("user-details");
        registry.addViewController("/users").setViewName("users");
        registry.addViewController("/access-denied").setViewName("access-denied"); // Добавлено для обработки ошибки 403
    }
}