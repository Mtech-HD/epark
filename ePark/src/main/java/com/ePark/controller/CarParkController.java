package com.ePark.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;
import com.ePark.model.Users;
import com.ePark.repository.UserRepository;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;
import com.ePark.service.CarParkTimeService;

@RestController
public class CarParkController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private CarParkSpotService carParkSpotService;
	
	@Autowired
	private CarParkTimeService carParkTimeService;

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/addcarpark")
	public ModelAndView showAddCarPark() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("addcarpark");
		return mv;
	}

	@ModelAttribute("carpark")
	public CarParkDto carParkDto() {
		CarParkDto carParkDto = new CarParkDto();
		carParkDto.setCarParkStatus(CarParkStatus.SUBMITTED);
		return carParkDto;
	}

	@PostMapping("/addcarpark")
	public ModelAndView submitCarPark(@ModelAttribute("carpark") CarParkDto carParkDto) {
		ModelAndView mv = new ModelAndView();
		System.out.println(carParkDto.getIsDisabled());
		carParkService.save(carParkDto);
		mv.setViewName("redirect:/addcarpark?success");
		return mv;
	}

	@GetMapping("/viewcarpark")
	public ModelAndView viewCarPark() {
		List<CarParks> carParks = carParkService.findAll();

		ModelAndView mv = new ModelAndView("viewcarpark");
		mv.addObject("carparklist", carParks);
		mv.addObject("carParkDto", new CarParkDto());
		return mv;
	}

	@GetMapping("/viewcarparkforuser")
	public ModelAndView viewCarParkForUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Users user = userRepo.findByUsername(auth.getName());

		List<CarParks> carParks = carParkService.findByUsers(user);
		ModelAndView mv = new ModelAndView("viewcarpark");
		mv.addObject("carparklist", carParks);
		mv.addObject("carParkDto", new CarParkDto());

		return mv;
	}

	@RequestMapping("/home")
	public ModelAndView home() {

		Collection<CarParks> carParks = carParkService.findByCarParkStatus(CarParkStatus.APPROVED);
		List<String> carParkAddresses = new ArrayList<>();
		for (CarParks carPark : carParks) {
			String fullAddress = "";
			fullAddress += carPark.getCarParkAddress1() + ", " + carPark.getCarParkAddress2() + ", "
					+ carPark.getCarParkCity() + ", " + carPark.getCarParkPostcode();
			carParkAddresses.add(fullAddress);
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("carParkAddresses", carParkAddresses);
		mv.addObject("carParks", carParks);
		return mv;
	}
	
	/*
	 * @RequestMapping("/home/getapproved") public List<CarParks> getApproved() {
	 * 
	 * List<CarParks> carParks =
	 * carParkService.findByCarParkStatus(CarParkStatus.APPROVED);
	 * 
	 * return carParks; }
	 */

	@RequestMapping("/viewcarpark/approve/{carParkId}")
	public ModelAndView approve(@PathVariable(name = "carParkId") long carParkId, @ModelAttribute("carParkDto") CarParkDto carParkDto) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);
		carPark.setCarParkComment(carParkDto.getComment());
		carPark.setDateModified(new Date());
		carParkService.save(carPark);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarpark?success");
		return mv;
	}

	@RequestMapping("/viewcarpark/reject/{carParkId}")
	public ModelAndView reject(@PathVariable(name = "carParkId") long carParkId, @ModelAttribute("carParkDto") CarParkDto carParkDto) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.REJECTED);
		carPark.setCarParkComment(carParkDto.getComment());
		carPark.setDateModified(new Date());
		carParkService.save(carPark);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarpark?success");
		return mv;
	}

	@RequestMapping("/viewcarpark/{carParkId}")
	public ModelAndView viewCarParkDetails(@PathVariable(name = "carParkId") long carParkId) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		List<CarParkSpots> carParkSpots = carParkSpotService.findByCarParks(carPark);
		
		List<CarParkTimes> carParkTimes = carParkTimeService.getCarParkTimes(carPark.getCarParkId());
		int isDisabled = 0;
		for (CarParkSpots carParkSpot : carParkSpots) {
			if (carParkSpot.isDisabled()) {
				isDisabled++;
			}
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("viewcarparkdetails");
		mv.addObject("carparkcomment", carPark.getCarParkComment());
		mv.addObject("isDisabled", isDisabled);
		mv.addObject("owners", carPark.getUsers());
		mv.addObject("carparkspots", carParkSpots);
		mv.addObject("carparktimes", carParkTimes);

		return mv;
	}

}
