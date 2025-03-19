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
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SpringBootSecurityDemoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	@Transactional
	void contextLoads() {
		// Этот тест проверяет, что контекст Spring Boot загружается корректно
	}

	@Test
	@Transactional
	void testUserCreation() {
		String uniqueEmail = "test-" + UUID.randomUUID() + "@example.com";
		String uniqueUsername = "testuser-" + UUID.randomUUID();

		User newUser = new User();
		newUser.setName("Test User");
		newUser.setEmail(uniqueEmail);
		newUser.setUsername(uniqueUsername);
		newUser.setPassword("password");
		newUser.setAge(30);

		userService.saveUser(newUser);

		Optional<User> savedUser = userRepository.findByEmail(uniqueEmail);
		assertTrue(savedUser.isPresent(), "Пользователь не был сохранен корректно");

		User saved = savedUser.get();
		assertEquals(uniqueEmail, saved.getEmail(), "Email пользователя не совпадает");
		assertEquals(uniqueUsername, saved.getUsername(), "Username пользователя не совпадает");
		assertNotEquals("password", saved.getPassword(), "Пароль пользователя не должен совпадать с оригиналом (должен быть зашифрован)");
		assertEquals(30, saved.getAge(), "Возраст пользователя не совпадает");
	}
}
