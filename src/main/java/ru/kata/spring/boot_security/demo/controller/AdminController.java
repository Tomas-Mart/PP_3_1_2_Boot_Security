package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collections;
import java.util.HashSet;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // Главная страница администратора
    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    // Сохранение нового пользователя
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("role") String role) {
        roleRepository.findByName(role).ifPresent(userRole -> user.setRoles(new HashSet<>(Collections.singleton(userRole))));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    // Форма редактирования пользователя
    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    // Обновление пользователя
    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("role") String role) {
        roleRepository.findByName(role).ifPresent(userRole -> user.setRoles(new HashSet<>(Collections.singleton(userRole))));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    // Удаление пользователя
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}