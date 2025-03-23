package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        // Используем Optional для безопасного получения пользователя по ID
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        // Шифруем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        // Удаляем пользователя по ID
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        // Используем Optional для безопасного получения пользователя по username
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Возвращаем пользователя, если он существует, или выбрасываем исключение
        return userOptional.orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));
    }
}