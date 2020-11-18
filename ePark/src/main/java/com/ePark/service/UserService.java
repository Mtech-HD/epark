package com.ePark.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ePark.dto.UserRegistrationDto;
import com.ePark.model.Users;

@Service
public interface UserService extends UserDetailsService{

	Users save(UserRegistrationDto registration);

	Users findByUserId(long userId);
	
	Users findByUsername(String username);
}
