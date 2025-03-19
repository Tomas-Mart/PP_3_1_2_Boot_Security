package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

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

    // Личный кабинет пользователя
    @GetMapping
    public String userPage(@AuthenticationPrincipal User user, Model model) {
        logger.info("Открытие личного кабинета пользователя: {}", user.getUsername());
        model.addAttribute("user", user);
        return "user";
    }

    // Детали пользователя
    @GetMapping("/details")
    public String userDetails(@RequestParam Long id, Model model) {
        logger.info("Запрос деталей пользователя с ID: {}", id);
        model.addAttribute("user", userService.getUserById(id));
        return "user-details";
    }

    // Поиск пользователя по email
    @GetMapping("/by-email")
    public String getUserByEmail(@RequestParam String email, Model model) {
        logger.info("Поиск пользователя по email: {}", email);
        Optional<User> user = userService.findByEmail(email);
        user.ifPresent(u -> model.addAttribute("user", u));
        return "user-details";
    }

    // Регистрация пользователя
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        logger.info("Попытка регистрации пользователя с email: {}", user.getEmail());
        try {
            userService.saveUser(user);
            logger.info("Пользователь с email {} успешно зарегистрирован", user.getEmail());
            return "redirect:/login";
        } catch (RuntimeException e) {
            logger.error("Ошибка при регистрации пользователя: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}