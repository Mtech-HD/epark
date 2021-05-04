package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ePark.model.Users;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepo;

	@Test
	void testFindByUsername() {

		Users user = new Users();
		user.setUsername("Test User");
		
		userRepo.save(user);
		
		assertThat(userRepo.findByUsername(user.getUsername())).isEqualTo(user);	
	}

	@Test
	void testFindByUserId() {
		
		Users user = new Users();
		
		user = userRepo.save(user);
		
		assertThat(userRepo.findByUserId(user.getUserId())).isEqualTo(user);	
	}

	@Test
	void testFindByStripeId() {
		
		Users user = new Users();
		user.setStripeId("Test StripeId");
		
		userRepo.save(user);
		
		assertThat(userRepo.findByStripeId(user.getStripeId())).isEqualTo(user);	
	}

	@Test
	void testFindByEmail() {
		
		Users user = new Users();
		user.setEmail("Test@test.com");
		
		userRepo.save(user);
		
		assertThat(userRepo.findByEmail(user.getEmail())).isEqualTo(user);	
	}

	@Test
	void testFindByResetPasswordToken() {
		
		Users user = new Users();
		user.setResetPasswordToken("Test Token");
		
		userRepo.save(user);
		
		assertThat(userRepo.findByResetPasswordToken(user.getResetPasswordToken())).isEqualTo(user);	
	}

}
