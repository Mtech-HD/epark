package com.ePark.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ePark.dto.UserRegistrationDto;
import com.ePark.model.Roles;
import com.ePark.model.Users;
import com.ePark.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	public Users save(UserRegistrationDto userDto) {
		
		BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(userDto.getPassword());

		Users user = new Users(userDto.getUsername(), encodedPassword,
				userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
				userDto.getCustomerId(), userDto.getRoles());
		
		return userRepo.save(user);
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}
		Set<Roles> roles = new HashSet<Roles>();
		roles.add(user.getRoles());
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(roles));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	public Users findByUserId(long userId) {
		return userRepo.findByUserId(userId);
	}

	public Users findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public Users findByStripeId(String stripeId) {
		return userRepo.findByStripeId(stripeId);
	}

	public Users findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	public Users findByResetPasswordToken(String token) {
		return userRepo.findByResetPasswordToken(token);
	}

	public Users updateResetPasswordToken(String token, Users user) {

		user.setResetPasswordToken(token);
		return userRepo.save(user);
	}
	
	public void updatePassword(String newPassword, Users user) {
		BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
		
		String encodedPassword = passwordEncoder.encode(newPassword);
		
		user.setPassword(encodedPassword);
		user.setResetPasswordToken(null);
		userRepo.save(user);
	}

}
