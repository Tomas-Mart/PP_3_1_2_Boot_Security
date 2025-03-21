package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PreDestroy;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreDestroy
    public void cleanup() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Ошибка при отмене регистрации драйвера в классе UserController: {}", e.getMessage(), e);
            }
        }
    }

    @GetMapping
    public String userPage(@AuthenticationPrincipal User user, Model model) {
        logger.info("Открытие личного кабинета пользователя: {}", user.getUsername());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/details")
    public String userDetails(@RequestParam Long id, Model model) {
        logger.info("Запрос деталей пользователя с ID: {}", id);
        model.addAttribute("user", userService.getUserById(id));
        return "user-details";
    }

    @GetMapping("/by-email")
    public String getUserByEmail(@RequestParam String email, Model model) {
        logger.info("Поиск пользователя по email: {}", email);
        Optional<User> user = userService.findByEmail(email);
        user.ifPresent(u -> model.addAttribute("user", u));
        return "user-details";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        logger.info("Попытка регистрации пользователя с email: {}", user.getEmail());
        try {
            userService.saveUser(user);
            logger.info("Пользователь с email {} успешно зарегистрирован", user.getEmail());
            return "redirect:/login";
        } catch (RuntimeException e) {
            logger.error("Ошибка при регистрации пользователя: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}