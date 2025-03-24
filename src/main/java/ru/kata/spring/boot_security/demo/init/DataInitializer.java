package ru.kata.spring.boot_security.demo.init;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        clearDatabase(); // Очистка базы данных перед инициализацией
        logger.info("Начало инициализации данных...");

        try {
            // Инициализация ролей
            Role roleAdmin = initializeRole("ROLE_ADMIN");
            Role roleUser = initializeRole("ROLE_USER");

            // Создание администратора
            initializeUser("admin", "admin", Set.of(roleAdmin, roleUser));

            // Создание обычного пользователя
            initializeUser("user", "user", Set.of(roleUser));

            logger.info("Инициализация данных завершена.");
        } catch (Exception e) {
            logger.error("Ошибка при инициализации данных: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при инициализации данных", e);
        }
    }

    @Transactional
    public void clearDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        logger.info("База данных очищена.");
    }

    private Role initializeRole(String roleName) {
        try {
            List<Role> roles = roleRepository.findByName(roleName);
            if (roles.isEmpty()) {
                logger.info("Роль {} не найдена, создаем новую...", roleName);
                Role newRole = new Role(roleName);
                return roleRepository.save(newRole);
            } else if (roles.size() == 1) {
                return roles.get(0); // Возвращаем существующую роль
            } else {
                logger.error("Найдено несколько ролей с одинаковым именем: {}", roleName);
                throw new IllegalStateException("Найдено несколько ролей с именем: " + roleName);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("Ошибка при поиске роли: {}", e.getMessage());
            throw new IllegalStateException("Ошибка при инициализации роли: " + roleName, e);
        }
    }

    private void initializeUser(String username, String password, Set<Role> roles) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                logger.error("Пользователь с таким именем уже существует: {}", username);
                throw new RuntimeException("Пользователь с таким именем уже существует: " + username);
            }

            logger.info("Создание пользователя: {}", username);
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(roles);
            userRepository.save(user);
            logger.info("Пользователь {} успешно создан.", username);
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Ошибка при создании пользователя: " + username, e);
        }
    }
}
