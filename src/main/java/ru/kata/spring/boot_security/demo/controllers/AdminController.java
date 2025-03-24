package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Отображение списка всех пользователей
    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin"; // Возвращаем имя представления (view)
    }

    // Форма для редактирования пользователя
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        // Получаем пользователя по ID
        User user = userService.getUserById(id);

        // Добавляем пользователя и список всех ролей в модель
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());

        return "edit-user"; // Возвращаем шаблон edit-user.html
    }

    // Сохранение пользователя (создание или обновление)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin"; // Перенаправляем на страницу администратора
    }

    // Удаление пользователя
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin"; // Перенаправляем на страницу администратора
    }
}