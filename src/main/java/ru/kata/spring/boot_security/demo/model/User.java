package ru.kata.spring.boot_security.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Электронная почта обязательна для заполнения")
    @Email(message = "Некорректный формат электронной почты")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Positive(message = "Возраст должен быть положительным числом")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    @Column(name = "age", nullable = false)
    private int age; // Добавлено поле age

    @NotBlank(message = "Пароль обязателен для заполнения")
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // Геттеры и сеттеры (уже включены благодаря аннотации @Data из Lombok)

    // Реализация методов UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}