package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exception.UserAlreadyExistsException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("Получение списка всех пользователей");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        logger.info("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> {
            logger.error("Пользователь с ID {} не найден в методе getUserById", id);
            return  new UserNotFoundException("Пользователь с ID " + id + " не найден");
        });
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        logger.info("Попытка сохранения пользователя с email: {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Пользователь с email {} уже существует", user.getEmail());
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            logger.error("Имя обязательно для заполнения");
            throw new IllegalArgumentException("Имя обязательно для заполнения");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            logger.info("Роль ROLE_USER не найдена, создание новой роли");
            Role newRole = new Role("ROLE_USER");
            return roleRepository.save(newRole);
        });

        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
        logger.info("Пользователь с email {} успешно сохранен", user.getEmail());
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        logger.info("Попытка обновления пользователя с ID: {}", user.getId());
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    logger.error("Пользователь с ID {} не найден", user.getId());
                    return new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден");
                });

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("Пароль пользователя с ID {} обновлен", user.getId());
        }
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAge(user.getAge());
        existingUser.setRoles(user.getRoles());
        userRepository.save(existingUser);
        logger.info("Пользователь с ID {} успешно обновлен", user.getId());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Попытка удаления пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("Пользователь с ID {} не найден в методе deleteUser", id);
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
        logger.info("Пользователь с ID {} успешно удален", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        logger.info("Поиск пользователя по email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        logger.info("Поиск пользователя по username: {}", username);
        return userRepository.findByUsername(username);
    }
}