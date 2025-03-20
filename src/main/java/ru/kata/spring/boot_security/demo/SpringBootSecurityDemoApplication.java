package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

        // Хеширование пароля для администратора
        String adminRawPassword = "admin";
        String encodedAdminPassword = hashPassword(adminRawPassword);
        System.out.println("Encoded Admin Password: " + encodedAdminPassword);

        // Хеширование пароля для пользователя
        String userRawPassword = "user";
        String encodedUserPassword = hashPassword(userRawPassword);
        System.out.println("Encoded User Password: " + encodedUserPassword);
    }

    /**
     * Метод для хеширования пароля с использованием BCrypt.
     *
     * @param rawPassword необработанный пароль
     * @return захешированный пароль
     */
    public static String hashPassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }
}