package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SpringBootSecurityDemoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		// Создание уникальных тестовых данных перед каждым тестом
		User user = new User();
		user.setEmail("unique-" + UUID.randomUUID() + "@example.com");
		user.setUsername("user" + UUID.randomUUID());
		// Установите остальные поля, если необходимо
		userRepository.save(user);
	}

	@Test
	@Transactional
	void contextLoads() {
		// Этот тест проверяет, что контекст Spring Boot загружается корректно
	}

	@Test
	@Transactional
	void testUserCreation() {
		// Пример теста, который проверяет создание пользователя
		User newUser = new User();
		newUser.setEmail("test-" + UUID.randomUUID() + "@example.com");
		newUser.setUsername("testuser" + UUID.randomUUID());
		// Установите остальные поля, если необходимо
		userService.saveUser(newUser);

		Optional<User> savedUserOptional = userRepository.findByEmail(newUser.getEmail());
		assertTrue(savedUserOptional.isPresent(), "Пользователь не был сохранен корректно");

		User savedUser = savedUserOptional.get();
		assertEquals(newUser.getEmail(), savedUser.getEmail(), "Email пользователя не совпадает");
		assertEquals(newUser.getUsername(), savedUser.getUsername(), "Username пользователя не совпадает");
		// Дополнительные проверки полей, если необходимо
	}
}
