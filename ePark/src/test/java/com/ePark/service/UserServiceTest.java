package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.dto.UserRegistrationDto;
import com.ePark.model.Roles;
import com.ePark.model.Users;
import com.ePark.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

	@Mock
	private UserRepository userRepo;
	
	@Mock
	private Users users;

	@InjectMocks
	private UserService userService;
	
	@Test
	void testSave() {
		Roles role = new Roles("USER");
		
		Users user = new Users("test", "test", "test", "test@test.com", "test", "test", role);
		
		UserRegistrationDto userDto = new UserRegistrationDto("test", "test", "test", "test@test.com", "test", "test", role);	

		Mockito.when(userRepo.save(any(Users.class))).thenReturn(user);
		
		assertThat(userService.save(userDto)).isEqualTo(user);
	}
	

	@Test
	void testLoadUserByUsername() {
		Roles role = new Roles("USER");
		
		Users user = new Users("test", "test", "test", "test", "test", "test", role);

		Set<Roles> roles = new HashSet<Roles>();
		roles.add(role);
		
		Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
		
		org.springframework.security.core.userdetails.User expectedUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
		
		assertThat(userService.loadUserByUsername(user.getUsername())).isEqualTo(expectedUser);
	}

	@Test
	void testFindByUserId() {
		Users user = new Users(1000L);

		Mockito.when(userRepo.findByUserId(1000L)).thenReturn(user);

		assertThat(userService.findByUserId(1000L)).isEqualTo(user);
	}

	@Test
	void testFindByUsername() {
		Roles role = new Roles("USER");
		
		Users user = new Users("test", "test", "test", "test", "test", "test", role);

		Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

		assertThat(userService.findByUsername(user.getUsername())).isEqualTo(user);
	}

	@Test
	void testFindByStripeId() {
		
		Roles role = new Roles("USER");
		
		Users user = new Users("test", "test", "test", "test", "test", "test", role);

		Mockito.when(userRepo.findByStripeId(user.getStripeId())).thenReturn(user);

		assertThat(userService.findByStripeId(user.getStripeId())).isEqualTo(user);
	}

	@Test
	void testFindByEmail() {

		Roles role = new Roles("USER");
		
		Users user = new Users("test", "test", "test", "test@test.com", "test", "test", role);

		Mockito.when(userRepo.findByEmail(user.getEmail())).thenReturn(user);

		assertThat(userService.findByEmail(user.getEmail())).isEqualTo(user);
	}

	@Test
	void testFindByResetPasswordToken() {
		
		Users user = new Users();
		
		user.setResetPasswordToken("12345");

		Mockito.when(userRepo.findByResetPasswordToken(user.getResetPasswordToken())).thenReturn(user);

		assertThat(userService.findByResetPasswordToken(user.getResetPasswordToken())).isEqualTo(user);
	}

	@Test
	void testUpdateResetPasswordToken() {

		Users user = new Users();
		user.setResetPasswordToken("12345");

		Mockito.when(userRepo.save(user)).thenReturn(user);

		assertThat(userService.updateResetPasswordToken("12345", user)).isEqualTo(user);
	}

	@Test
	void testUpdatePassword() {

		Users user = new Users();
		user.setPassword("12345");

		Mockito.when(userRepo.save(user)).thenReturn(user);

		assertThat(userService.updateResetPasswordToken("12345", user)).isEqualTo(user);
	}

}
