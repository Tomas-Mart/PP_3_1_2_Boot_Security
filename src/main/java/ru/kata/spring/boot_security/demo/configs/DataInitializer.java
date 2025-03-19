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
import ru.kata.spring.boot_security.demo.exception.UserAlreadyExistsException;

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
        try {
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
        } catch (UserAlreadyExistsException e) {
            logger.warn("Пользователь 'user' уже существует: {}", e.getMessage());
        }

        // Добавление пользователя с ролью ADMIN
        try {
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
        } catch (UserAlreadyExistsException e) {
            logger.warn("Пользователь 'admin' уже существует: {}", e.getMessage());
        }

        // Проверка существования пользователя по email user@example.com перед вставкой
        try {
            if (userService.findByEmail("user@example.com").isEmpty()) {
                User userByEmail = new User();
                userByEmail.setName("UserByEmail");
                userByEmail.setUsername("user-by-email"); // Исправлено: добавлены дефисы
                userByEmail.setEmail("user@example.com");
                userByEmail.setAge(28);
                userByEmail.setPassword(passwordEncoder.encode("user-by-email")); // Исправлено: добавлены дефисы
                userByEmail.setRoles(Collections.singleton(userRole));
                userService.saveUser(userByEmail);
                logger.info("Пользователь с email user@example.com успешно создан");
            } else {
                logger.info("Пользователь с email user@example.com уже существует, вставка пропущена");
            }
        } catch (UserAlreadyExistsException e) {
            logger.warn("Пользователь с email user@example.com уже существует: {}", e.getMessage());
        }

        // Проверка существования пользователя по email admin@example.com перед вставкой
        try {
            if (userService.findByEmail("admin@example.com").isEmpty()) {
                User adminByEmail = new User();
                adminByEmail.setName("AdminByEmail");
                adminByEmail.setUsername("admin-by-email"); // Исправлено: добавлены дефисы
                adminByEmail.setEmail("admin@example.com");
                adminByEmail.setAge(35);
                adminByEmail.setPassword(passwordEncoder.encode("admin-by-email")); // Исправлено: добавлены дефисы
                adminByEmail.setRoles(new HashSet<>(Arrays.asList(userRole, adminRole)));
                userService.saveUser(adminByEmail);
                logger.info("Пользователь с email admin@example.com успешно создан");
            } else {
                logger.info("Пользователь с email admin@example.com уже существует, вставка пропущена");
            }
        } catch (UserAlreadyExistsException e) {
            logger.warn("Пользователь с email admin@example.com уже существует: {}", e.getMessage());
        }

        logger.info("Инициализация данных завершена");
    }
}