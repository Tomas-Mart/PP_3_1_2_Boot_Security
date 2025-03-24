package ru.kata.spring.boot_security.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data // Автоматически генерирует геттеры, сеттеры, toString, equals и hashCode
@NoArgsConstructor // Автоматически генерирует конструктор без аргументов
@AllArgsConstructor // Автоматически генерирует конструктор со всеми аргументами
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Логика для проверки, не истек ли срок действия аккаунта
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Логика для проверки, не заблокирован ли аккаунт
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Логика для проверки, не истек ли срок действия учетных данных
    }

    @Override
    public boolean isEnabled() {
        return true; // Логика для проверки, активен ли аккаунт
    }
}