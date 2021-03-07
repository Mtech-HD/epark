package com.ePark.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.dto.VehicleDto;
import com.ePark.entity.CarParkStatus;
import com.ePark.entity.CarParks;
import com.ePark.entity.Vehicles;
import com.ePark.service.BookingFlow;
import com.ePark.service.VehicleService;

@Controller
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private AppSecurityConfig appSecurity;

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
	public String addVehicle(@RequestParam("registration") String registration, @RequestParam("make") String make,
			@RequestParam("colour") String colour, Model model, @ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) {
		
		List<Vehicles> vehicles = vehicleService.findAll();
		
		boolean isDefault = false;
		
		if (vehicles != null) {
			isDefault = true;
		}

		Vehicles vehicle = new Vehicles(registration, make, colour, appSecurity.getCurrentUser(), isDefault);

		vehicleService.save(vehicle);

		model.addAttribute("user", appSecurity.getCurrentUser());
		return "bookingfragment :: vehicles";
	}

	@Transactional
	@GetMapping(value = "/removeVehicle", params = "vehicleId")
	public String removeVehicle(@RequestParam("vehicleId") long vehicleId, Model model, @ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) {

		vehicleService.deleteByVehicleId(vehicleId);

		model.addAttribute("user", appSecurity.getCurrentUser());

		return "bookingfragment :: vehicles";
	}
	
	@GetMapping("/setdefaultvehicle")
	public String setDefaultCard(@RequestParam("vehicleId") long vehicleId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) {

		Vehicles defaultVehicle = vehicleService.findByUsersAndIsDefault(appSecurity.getCurrentUser());
		
		Vehicles newDefaultVehicle = vehicleService.findByVehicleId(vehicleId);
		
		defaultVehicle.setIsDefault(false);
		
		newDefaultVehicle.setIsDefault(true);

		vehicleService.save(defaultVehicle);
		
		vehicleService.save(newDefaultVehicle);

		model.addAttribute("user", appSecurity.getCurrentUser());
		
		return "bookingfragment :: vehicles";
	}
}
