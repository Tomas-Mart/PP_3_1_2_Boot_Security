package ru.kata.spring.boot_security.demo.config;

import org.springframework.boot.CommandLineRunner;
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
public class DataInitializer implements CommandLineRunner {

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
        // Создание ролей, если они отсутствуют
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_USER");
            return roleRepository.save(role);
        });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            return roleRepository.save(role);
        });

        // Добавление пользователя с ролью USER
        if (userService.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setName("User");
            user.setEmail("user@example.com");
            user.setAge(25);
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(Collections.singleton(userRole));
            userService.saveUser(user);
        }

        // Добавление пользователя с ролью ADMIN
        if (userService.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@example.com");
            admin.setAge(30);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Arrays.asList(userRole, adminRole)));
            userService.saveUser(admin);
        }
    }
}