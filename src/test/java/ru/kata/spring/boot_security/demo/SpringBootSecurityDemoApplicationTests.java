package ru.kata.spring.boot_security.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Используем профиль "test"
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Очистка контекста перед каждым тестом
public class SpringBootSecurityDemoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll(); // Очистка базы данных перед каждым тестом
	}

	@Test
	@Transactional
	void contextLoads() {
		// Этот тест проверяет, что контекст Spring Boot загружается корректно
	}

	@Test
	@Transactional
	void testUserCreation() {
		// Создаем уникальные данные для теста
		String uniqueEmail = "test-" + UUID.randomUUID() + "@example.com";
		String uniqueUsername = "testuser-" + UUID.randomUUID();

		// Создаем нового пользователя
		User newUser = new User();
		newUser.setName("Test User"); // Убедитесь, что имя установлено
		newUser.setEmail(uniqueEmail);
		newUser.setUsername(uniqueUsername);
		newUser.setPassword("password123"); // Устанавливаем пароль
		newUser.setAge(25); // Пример дополнительного поля

		// Сохраняем пользователя
		userService.saveUser(newUser);

		// Проверяем, что пользователь был сохранен
		Optional<User> savedUserOptional = userRepository.findByEmail(uniqueEmail);
		assertTrue(savedUserOptional.isPresent(), "Пользователь не был сохранен корректно");

		// Проверяем, что данные пользователя совпадают
		User savedUser = savedUserOptional.get();
		assertEquals(uniqueEmail, savedUser.getEmail(), "Email пользователя не совпадает");
		assertEquals(uniqueUsername, savedUser.getUsername(), "Username пользователя не совпадает");
		assertNotEquals("password123", savedUser.getPassword(), "Пароль пользователя не должен совпадать с оригиналом (должен быть зашифрован)");
		assertEquals(25, savedUser.getAge(), "Возраст пользователя не совпадает");
	}
}
