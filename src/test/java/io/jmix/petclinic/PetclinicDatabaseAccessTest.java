package io.jmix.petclinic;

import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.petclinic.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PetclinicDatabaseAccessTest {

	@Autowired
	SystemAuthenticator authenticator;

	@Autowired
	DataManager dataManager;

	@BeforeEach
	void setUp() {
		authenticator.begin();
	}

	@AfterEach
	void tearDown() {
		authenticator.end();
	}

	@Test
	void adminUserExists() {
		Optional<User> adminOpt = dataManager.load(User.class)
				.query("e.username = ?1", "admin")
				.optional();
		assertTrue(adminOpt.isPresent());
	}

}
