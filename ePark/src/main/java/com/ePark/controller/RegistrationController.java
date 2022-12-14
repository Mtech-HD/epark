package com.ePark.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ePark.AppSecurityConfig;
import com.ePark.dto.UserRegistrationDto;
import com.ePark.model.Mail;
import com.ePark.model.Roles;
import com.ePark.model.Users;
import com.ePark.service.CarParkService;
import com.ePark.service.EmailService;
import com.ePark.service.RoleService;
import com.ePark.service.StripeService;
import com.ePark.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@Controller
public class RegistrationController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CarParkService carParkService;
	
	@Autowired
	private AppSecurityConfig appSecurity;

	@GetMapping("/registration")
	public String showRegistration(Model model, @RequestParam(value = "carParkId", required = false, defaultValue = "0") long carParkId, 
			@RequestParam(value = "returnPage") Optional<String> returnPage, @RequestParam(value = "adminCreated", required = false, defaultValue = "false") Boolean adminCreated) {
		
		List<Roles> roleList = new ArrayList<>();
		List<Roles> userAndCarParkOwner = new ArrayList<>();
		List<Roles> SiteAdminAndCarParkOwner = new ArrayList<>();
		
		roleList = roleService.findAll();
		userAndCarParkOwner = roleList.stream().filter(r -> !r.getName().equalsIgnoreCase("ADMIN") && !r.getName().equalsIgnoreCase("SITEADMIN")).collect(Collectors.toList());
		SiteAdminAndCarParkOwner = roleList.stream().filter(r -> r.getName().equalsIgnoreCase("CARPARKOWNER") || r.getName().equalsIgnoreCase("SITEADMIN")).collect(Collectors.toList());
		
		if (!appSecurity.isAuthenticated()) {
			roleList = userAndCarParkOwner;
		} else {
			if (!appSecurity.getCurrentUser().isAdmin()) {
				roleList = userAndCarParkOwner;
			}
		}

		UserRegistrationDto registrationDto = new UserRegistrationDto();
		registrationDto.setAdminCreated(adminCreated);
		registrationDto.setCarParkId(carParkId);

		if (carParkId != 0) {
			roleList = SiteAdminAndCarParkOwner;
		}
		
		if (returnPage.isPresent()) {
			registrationDto.setReturnPage(returnPage.get());
		}	
		
		model.addAttribute("user", registrationDto);
		model.addAttribute("roleList", roleList);
		
		return "registration";
	}

	@PostMapping("/registration")
	public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto) throws StripeException, MessagingException, IOException {
		
		Roles role = roleService.findByRoleId(registrationDto.getRoleId());
		registrationDto.setRoles(role);		
		
		if (role.getName().equals("USER")) {
			Customer customer = paymentsService.createCustomer(registrationDto.getFirstName() + " " + registrationDto.getLastName(), registrationDto.getEmail());
			registrationDto.setCustomerId(customer.getId());
		}
		
		Users user = userService.save(registrationDto);
		
		if (registrationDto.getCarParkId() != 0) {
			carParkService.addUserToCarPark(registrationDto.getCarParkId(), user);
		}
		
		Mail mail = new Mail();
        mail.setFrom("ePark Admin <official-epark@outlook.com>");
        mail.setMailTo(registrationDto.getEmail());
        mail.setSubject("Registration Confirmation");
        mail.setTemplate("emails/registrationconfirmation");
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", registrationDto.getFirstName());
        model.put("username", registrationDto.getUsername());
        
		if (registrationDto.getAdminCreated()) {
			model.put("resetpasswordprompt", "If an administrator created this account for you, please proceed to reset your password via forgotten password.");
		}
        
        mail.setProps(model);
        emailService.sendEmail(mail);
        
		if (registrationDto.getReturnPage() != null || registrationDto.getReturnPage() != "") {
			return "redirect:/" +registrationDto.getReturnPage();
		}
		
		return "redirect:/login?registrationSuccess";	
	}

	@RequestMapping("/registration/checkUsername/{username}")
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
