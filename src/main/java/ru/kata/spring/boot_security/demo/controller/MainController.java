package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    // Главная страница
    @GetMapping("/")
    public String index() {
        return "redirect:/index"; // Возвращает index.html
    }

    @GetMapping("/index")
    public String homePage() {
        return "index"; // Имя шаблона (home.html)
    }

    // Страница входа
    @GetMapping("/login")
    public String login() {
        return "login"; // Возвращает login.html
    }
}