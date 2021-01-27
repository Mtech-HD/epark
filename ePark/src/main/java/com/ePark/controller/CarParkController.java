package com.ePark.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.dto.CarParkDto;
import com.ePark.entity.CarParkComments;
import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParkStatus;
import com.ePark.entity.CarParks;
import com.ePark.entity.Users;
import com.ePark.entity.Week;
import com.ePark.service.BookingService;
import com.ePark.service.CarParkCommentService;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;
import com.ePark.service.StripeService;

@Controller
public class CarParkController {

	@Autowired
	private CarParkService carParkService;
	
	@Autowired
	private CarParkSpotService carParkSpotService;

	@Autowired
	private CarParkCommentService carParkCommentService;

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private AppSecurityConfig appSecurity;

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

	@GetMapping("/viewcarparkdetails/{carParkId}")
	public ModelAndView viewCarParkDetails(@PathVariable("carParkId") long carParkId) {

		ModelAndView mv = new ModelAndView("viewcarparkdetails");
		mv.addObject("carpark", carParkService.findByCarParkId(carParkId));
		mv.addObject("paymentsService", paymentsService);

		return mv;
	}

	@GetMapping("/viewcarparkforuser")
	public ModelAndView viewCarParkForUser() {

		Users user = appSecurity.getCurrentUser();

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

	@RequestMapping("/viewcarparkdetails/approve/{carParkId}")
	public ModelAndView approve(@PathVariable(name = "carParkId") long carParkId) {

		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);

		carPark.setDateModified(new Date());
		carParkService.save(carPark);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarparkdetails/" + carParkId);
		return mv;
	}

	@RequestMapping("/viewcarparkdetails/reject/{carParkId}")
	public ModelAndView reject(@PathVariable(name = "carParkId") long carParkId) {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.REJECTED);

		carPark.setDateModified(new Date());
		carParkService.save(carPark);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarparkdetails/" + carParkId);
		return mv;
	}

	@RequestMapping("/viewcarparkdetails/addcomment/{carParkId}")
	public ModelAndView addComment(@PathVariable(name = "carParkId") long carParkId,
			@RequestParam(name = "comment") String comment) {

		Users user = appSecurity.getCurrentUser();

		CarParks carPark = carParkService.findByCarParkId(carParkId);

		CarParkComments carParkComment = new CarParkComments(comment, new Date(), user, carPark);

		carParkCommentService.save(carParkComment);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarparkdetails/" + carParkId);
		return mv;
	}

	@RequestMapping("/siteadmin")
	public ModelAndView showSiteAdmin() {

		Users user = appSecurity.getCurrentUser();

		List<CarParks> carParks = carParkService.findByUsers(user);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/siteadmin");
		mv.addObject("carparklist", carParks);
		return mv;
	}

	@RequestMapping("/siteadminbookings/{carParkId}")
	public ModelAndView siteAdminBookings(@PathVariable("carParkId") long carParkId) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/siteadminbookings");
		mv.addObject("carpark", carParkService.findByCarParkId(carParkId));
		return mv;
	}

	@GetMapping(value = "/findByCarParkSpotsAndStartDate")
	public String findByCarParkSpotsAndStartDate(@RequestParam("carParkSpotId") long carParkSpotId,
			@RequestParam("date") LocalDate date, Model model) {
		
		CarParkSpots carParkSpot = carParkSpotService.findByCarParkSpotId(carParkSpotId);

		model.addAttribute("spotBookings", bookingService.findByCarParkSpotsAndStartDate(carParkSpot, date));

		return "bookingfragment :: spotBookings";
	}

}
