package ru.kata.spring.boot_security.demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Component
@Profile("!test") // Инициализация данных не выполняется при активном профиле "test"
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Инициализация данных...");

        // Создание ролей, если они отсутствуют
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            logger.info("Роль ROLE_USER не найдена, создание новой роли");
            Role role = new Role("ROLE_USER");
            return roleRepository.save(role);
        });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            logger.info("Роль ROLE_ADMIN не найдена, создание новой роли");
            Role role = new Role("ROLE_ADMIN");
            return roleRepository.save(role);
        });

        // Добавление пользователя с ролью USER
        if (userService.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setName("User");
            user.setUsername("user");
            user.setEmail("user@example.com");
            user.setAge(25);
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(Collections.singleton(userRole));
            userService.saveUser(user);
            logger.info("Пользователь 'user' успешно создан");
        } else {
            logger.info("Пользователь 'user' уже существует");
        }

        // Добавление пользователя с ролью ADMIN
        if (userService.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setAge(30);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Arrays.asList(userRole, adminRole)));
            userService.saveUser(admin);
            logger.info("Пользователь 'admin' успешно создан");
        } else {
            logger.info("Пользователь 'admin' уже существует");
        }

        logger.info("Инициализация данных завершена");
    }
}