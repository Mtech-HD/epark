package com.ePark.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.dto.CarParkRegistrationDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParks;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;

@RestController
public class CarParkController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private CarParkSpotService carParkSpotService;


	@GetMapping("/addcarpark")
	public ModelAndView showAddCarPark() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("addcarpark");
		return mv;
	}

	@ModelAttribute("carpark")
	public CarParkRegistrationDto carParkRegistrationDto() {
		CarParkRegistrationDto carParkRegistrationDto = new CarParkRegistrationDto();
		carParkRegistrationDto.setCarParkStatus(CarParkStatus.SUBMITTED);
		return carParkRegistrationDto;
	}

	@PostMapping("/addcarpark")
	public ModelAndView submitCarPark(@ModelAttribute("carpark") CarParkRegistrationDto carParkRegistrationDto) {
		ModelAndView mv = new ModelAndView();
		carParkService.save(carParkRegistrationDto);
		mv.setViewName("redirect:/addcarpark?success");
		return mv;
	}

	@GetMapping("/viewcarkpark")
	public ModelAndView viewCarPark(Model model) {
		List<CarParks> carParks = carParkService.findAll();
		ModelAndView mv = new ModelAndView("viewcarpark");
		model.addAttribute("carparklist", carParks);

		return mv;
	}
	
	@RequestMapping("/home")
	public ModelAndView home() {
		
		List<CarParks> carParks = carParkService.findByCarParkStatus(CarParkStatus.APPROVED);
		List<String> carParkAddresses = new ArrayList<>();
		for (CarParks carPark : carParks) {
			String fullAddress = "";
			fullAddress += carPark.getCarParkAddress1() + "," + carPark.getCarParkAddress2()
			+ "," + carPark.getCarParkCity() + "," + carPark.getCarParkPostcode();
			carParkAddresses.add(fullAddress);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("carParkAddresses", carParkAddresses);

		return mv;
	}
	
	
	@RequestMapping("/viewcarpark/{carParkId}")
	public ModelAndView viewCarParkDetails(@PathVariable(name = "carParkId") long carParkId) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		List<CarParkSpots> carParkSpots = carParkSpotService.findByCarParks(carPark);

		int isDisabled = 0;
		for (CarParkSpots carParkSpot : carParkSpots) {
			if (carParkSpot.isDisabled()) {
				isDisabled++;
			}
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("viewcarparkdetails");
		mv.addObject("isDisabled", isDisabled);
		mv.addObject("owners", carPark.getUsers());
		mv.addObject("carparkspots", carParkSpots);

		return mv;
	}

}
