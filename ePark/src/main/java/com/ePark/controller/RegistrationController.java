package com.ePark.controller;


import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ePark.dto.UserRegistrationDto;
import com.ePark.model.Roles;
import com.ePark.model.Users;
import com.ePark.repository.UserRepository;
import com.ePark.service.RoleService;
import com.ePark.service.UserService;

@Controller
public class RegistrationController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;


	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		UserRegistrationDto registrationDto = new UserRegistrationDto();
		return registrationDto;
	}

	@GetMapping("/registration")
	public String showRegistration(Model model) {
		Collection<Roles> roleList = Arrays.asList();
		roleList = (Collection<Roles>) roleService.findAll();
		model.addAttribute("roles", roleList);
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto) {
		Roles role = roleService.findByName(registrationDto.getRoleName());
		registrationDto.setRoles(role);		
		userService.save(registrationDto);
		return "redirect:/login?registrationSuccess";
		
	}
	
	@RequestMapping("/registration/{username}")
	@ResponseBody
	public ResponseEntity<String> checkUserExists(@PathVariable(name = "username") String username) {
		String result = "";
		
		Users user = userService.findByUsername(username);
		
		if (user == null) {
			result = "1";
		} else if (user != null) {
			result = "2";
		}
 
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	

}
