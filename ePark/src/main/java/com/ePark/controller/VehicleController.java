package com.ePark.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.dto.VehicleDto;
import com.ePark.model.Users;
import com.ePark.model.Vehicles;
import com.ePark.repository.UserRepository;
import com.ePark.service.VehicleService;

@Controller
public class VehicleController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VehicleService vehicleService;

	@ModelAttribute("vehicle")
	public VehicleDto vehicleDto() {
		VehicleDto vehicleDto = new VehicleDto();
		return vehicleDto;
	}
	
	
	@GetMapping("/viewvehicles")
	public ModelAndView showViewVehicles() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("viewvehicles");
		return mv;
	}
	
	@GetMapping("/addvehicles")
	public ModelAndView showAddVehicle() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("addvehicles");
		return mv;
	}
	
	
	@PostMapping("/addvehicles")
	public ModelAndView addVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<Users> users = new HashSet<Users>();
		Users user = userRepo.findByUsername(auth.getName());
		users.add(user);
		
		Vehicles vehicle = new Vehicles(vehicleDto.getRegistration(), vehicleDto.getMake(), vehicleDto.getColour(), users);
		
		vehicleService.save(vehicle);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/addvehicles?success");
		return mv;
	}
}

