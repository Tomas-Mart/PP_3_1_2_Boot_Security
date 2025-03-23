package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
class SpringBootSecurityDemoApplicationTests {

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void contextLoads() {
        // Тест с аутентифицированным пользователем
    }
}