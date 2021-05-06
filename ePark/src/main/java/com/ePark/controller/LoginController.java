package com.ePark.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.model.Mail;
import com.ePark.model.Users;
import com.ePark.service.BookingFlow;
import com.ePark.service.EmailService;
import com.ePark.service.UserService;

import net.bytebuddy.utility.RandomString;

@Controller
public class LoginController {
	
	@Autowired
	private AppSecurityConfig appSecurity;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;

	@GetMapping("/login")
	public ModelAndView showLogin() {
		ModelAndView mv = new ModelAndView();
		if (appSecurity.isAuthenticated()) {
			mv.setViewName("redirect:/home");
	    } else {
			mv.setViewName("login");    	
	    }
		return mv;
	}
	
	@PostMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		return mv;
	}
	
	@RequestMapping(value = "/logout", params = "expired")
	public ModelAndView expiredLogout(BookingFlow bookingFlow) {
		ModelAndView mv = new ModelAndView();
		
		System.out.print(bookingFlow.getBooking().getCarParks());
		mv.setViewName("login");
		return mv;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		return mv;
	}
	
	@PostMapping("/forgotpassword")
	public String forgotPassword(@RequestParam("email") String email, Model model, HttpServletRequest request) throws MessagingException, IOException {
		
		String token = RandomString.make(45);
		
		Users user = userService.findByEmail(email);
		
		if (user != null) {
			userService.updateResetPasswordToken(token, user);
			
			String resetPasswordLink = appSecurity.getSiteUrl(request) + "/resetpassword?token=" + token;
			
			Mail mail = new Mail();
	        mail.setFrom("ePark Admin <official-epark@outlook.com>");//replace with your desired email
	        mail.setMailTo(email);//replace with your desired email
	        mail.setSubject("Password reset");
	        mail.setTemplate("emails/passwordreset");
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("name", user.getFirstName());
	        params.put("resetlink", resetPasswordLink);
	        mail.setProps(params);
	        emailService.sendEmail(mail);
			
			model.addAttribute("resetlinksent", "Password reset link sent");
		} else {
			model.addAttribute("error", "No user found with specified email");
		}

		return "/login";
	}
	
	@GetMapping("/resetpassword")
	public String resetPassword(@RequestParam("token") String token, Model model) {
		
		userService.findByResetPasswordToken(token);
		model.addAttribute("token", token);
		
		return "/resetpassword";
	}
	
	@PostMapping("/resetpassword")
	public String ProcessResetPassword(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
		
		Users user = userService.findByResetPasswordToken(token);
		
		if (user != null) {
			userService.updatePassword(password, user);
			model.addAttribute("resetsuccessful", "You have successfully changed your password");
		}
		
		return "/login";
	}
	
	
}
