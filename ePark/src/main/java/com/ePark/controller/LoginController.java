package com.ePark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;

@Controller
public class LoginController {
	
	@Autowired
	private AppSecurityConfig appSecurity;

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
	
	@RequestMapping("/logout")
	public ModelAndView logout() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		return mv;
	}
	

	
	
}
