package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserPage(@AuthenticationPrincipal User currentUser, Model model) {
        // Получаем текущего пользователя по его имени
        User user = userService.findByUsername(currentUser.getUsername());

        // Добавляем пользователя в модель
        model.addAttribute("user", user);
        return "user"; // Возвращаем имя представления (view)
    }
}