package ru.kata.spring.boot_security.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PreDestroy;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PreDestroy
    public void cleanup() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Ошибка при отмене регистрации драйвера в классе AdminController: {}", e.getMessage(), e);
            }
        }
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
