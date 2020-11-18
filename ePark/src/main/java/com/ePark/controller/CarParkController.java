package com.ePark.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.ePark.model.CarParkComments;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParks;
import com.ePark.model.Users;
import com.ePark.model.Week;
import com.ePark.service.CarParkCommentService;
import com.ePark.service.CarParkService;
import com.ePark.service.UserService;

@RestController
public class CarParkController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private CarParkCommentService carParkCommentService;

	@Autowired
	private UserService userService;

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

		Users user = userService.findByUsername(auth.getName());

		List<CarParks> carParks = carParkService.findByUsers(user);
		ModelAndView mv = new ModelAndView("viewcarpark");
		mv.addObject("carparklist", carParks);
		mv.addObject("carParkDto", new CarParkDto());

		return mv;
	}

	@RequestMapping("/home")
	public ModelAndView home() {

		List<CarParks> carParks = carParkService.findByCarParkStatus(CarParkStatus.APPROVED);
		List<Week> weekValues = Arrays.asList(Week.values());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("weekValues", weekValues);
		mv.addObject("carParks", carParks);
		return mv;
	}


	@RequestMapping("/viewcarpark/approve/{carParkId}")
	public ModelAndView approve(@PathVariable(name = "carParkId") long carParkId,
			@ModelAttribute("carParkDto") CarParkDto carParkDto) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);

		carPark.setDateModified(new Date());
		carParkService.save(carPark);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarpark?success");
		return mv;
	}

	@RequestMapping("/viewcarpark/reject/{carParkId}")
	public ModelAndView reject(@PathVariable(name = "carParkId") long carParkId,
			@ModelAttribute("carParkDto") CarParkDto carParkDto) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.REJECTED);

		carPark.setDateModified(new Date());
		carParkService.save(carPark);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarpark?success");
		return mv;
	}
	
	@RequestMapping("/viewcarpark/addcomment/{carParkId}")
	public ModelAndView addComment(@PathVariable(name = "carParkId") long carParkId,
			@ModelAttribute("carParkDto") CarParkDto carParkDto) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Users user = userService.findByUsername(auth.getName());
		
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		
		CarParkComments carParkComment = new CarParkComments(carParkDto.getComment(), new Date(), user, carPark);
		
		carParkCommentService.save(carParkComment);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarpark?comment");
		return mv;
	}



}
