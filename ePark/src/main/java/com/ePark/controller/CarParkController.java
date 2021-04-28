package com.ePark.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.dto.CarParkDto;
import com.ePark.entity.Bookings;
import com.ePark.entity.CarParkComments;
import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParks;
import com.ePark.entity.CarParks.AccessControlTypes;
import com.ePark.entity.CarParks.CarParkStatus;
import com.ePark.entity.ClosureDates;
import com.ePark.entity.Mail;
import com.ePark.entity.Users;
import com.ePark.entity.Week;
import com.ePark.service.BookingService;
import com.ePark.service.CarParkCommentService;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;
import com.ePark.service.CarParkTimeService;
import com.ePark.service.EmailService;
import com.ePark.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Balance;
import com.stripe.model.LoginLink;
import com.stripe.net.RequestOptions;
import com.sun.mail.handlers.image_gif;

@Controller
public class CarParkController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private CarParkTimeService carParkTimeService;

	@Autowired
	private CarParkSpotService carParkSpotService;

	@Autowired
	private CarParkCommentService carParkCommentService;

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AppSecurityConfig appSecurity;

	@GetMapping("/addcarpark")
	public ModelAndView showAddCarPark() {
		CarParkDto carParkDto = new CarParkDto();

		ModelAndView mv = new ModelAndView();
		mv.addObject("accessTypes", AccessControlTypes.values());
		mv.addObject("carpark", carParkDto);
		mv.setViewName("addcarpark");
		return mv;
	}

	@GetMapping("/addcarpark/edit/{carParkId}")
	public ModelAndView editAddCarPark(@PathVariable("carParkId") long carParkId) {

		CarParks carPark = carParkService.findByCarParkId(carParkId);

		CarParkDto carParkDto = new CarParkDto(carPark);

		ModelAndView mv = new ModelAndView();
		mv.addObject("accessTypes", AccessControlTypes.values());
		mv.addObject("accessTypeSelected", carPark.getAccessControl().toString());
		mv.addObject("carpark", carParkDto);
		mv.setViewName("addcarpark");
		return mv;
	}

	@PostMapping("/addcarpark")
	public ModelAndView submitCarPark(@ModelAttribute("carpark") CarParkDto carParkDto)
			throws MessagingException, IOException, StripeException {
		ModelAndView mv = new ModelAndView();

		Users user = appSecurity.getCurrentUser();

		if (carParkDto.getCarParkId() == 0) {

			Account account = paymentsService.createConnectedAccount(carParkDto.getEmail());
			carParkDto.setStripeId(account.getId());

			carParkService.save(carParkDto);
		} else {
			CarParks carPark = carParkService.findByCarParkId(carParkDto.getCarParkId());

			carParkService.update(carParkDto);
			carParkTimeService.updateTimes(carPark, carParkDto);
			carParkSpotService.updateSpots(carParkDto.getSpaces(), carParkDto.getIsDisabled(), carPark);
		}

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(user.getEmail());
		mail.setSubject("Car Park Submitted");
		mail.setTemplate("emails/carparksubmitted");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", user.getFirstName());
		prop.put("carparkname", carParkDto.getName());
		mail.setProps(prop);
		emailService.sendEmail(mail);

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
	public ModelAndView viewCarParkDetails(@PathVariable("carParkId") long carParkId) throws StripeException {

		CarParks carPark = carParkService.findByCarParkId(carParkId);

		// AccountLink accountLink = paymentsService.createAccountLink(account.getId(),
		// carPark.getCarParkId());

		ModelAndView mv = new ModelAndView("viewcarparkdetails");
		// mv.addObject("cards", paymentsService.getCards(carPark.getStripeId()));
		// mv.addObject("defaultCard",
		// paymentsService.getCustomer(carPark.getStripeId()).getInvoiceSettings().getDefaultPaymentMethod());
		mv.addObject("carpark", carPark);

		mv.addObject("stripeUrl", paymentsService.getStripeAccountLink(carPark.getStripeId(), carPark.getCarParkId()));

		mv.addObject("revenueForMonth", bookingService.getRevenueForMonth(carPark));

		long balanceAvailable = 0;

		long balancePending = 0;

		if (carPark.getStripeId() != null) {

			Balance balance = paymentsService.getAccountBalance(carPark.getStripeId());

			// balance.getAvailable().stream().filter(g ->
			// g.getCurrency().equals("gbp")).findFirst().orElse(null)
			
			balanceAvailable = balance.getAvailable().get(0).getAmount();
			balancePending = balance.getPending().get(0).getAmount();
		}
		
		mv.addObject("balanceAvailable", balanceAvailable);

		mv.addObject("balancePending", balancePending);

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

		List<Week> weekValues = Arrays.asList(Week.values());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("weekValues", weekValues);
		return mv;
	}

	@RequestMapping("/")
	public ModelAndView rootHome() {

		List<Week> weekValues = Arrays.asList(Week.values());

		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		mv.addObject("weekValues", weekValues);
		return mv;
	}

	@GetMapping("/getapproved")
	public ResponseEntity<Object> getApproved() {

		List<CarParks> carParks = carParkService.findByCarParkStatus(CarParkStatus.APPROVED);

		return new ResponseEntity<>(carParks, HttpStatus.OK);
	}

	@RequestMapping("/viewcarparkdetails/approve/{carParkId}")
	public ModelAndView approve(@PathVariable(name = "carParkId") long carParkId)
			throws MessagingException, IOException {

		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);

		carPark.setDateModified(new Date());
		carParkService.save(carPark);

		Users user = appSecurity.getCurrentUser();

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(user.getEmail());
		mail.setSubject("Car Park Submitted");
		mail.setTemplate("emails/carparkapproved");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", user.getFirstName());
		prop.put("carparkname", carPark.getName());
		mail.setProps(prop);
		emailService.sendEmail(mail);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/viewcarparkdetails/" + carParkId);
		return mv;
	}

	@RequestMapping("/viewcarparkdetails/reject/{carParkId}")
	public ModelAndView reject(@PathVariable(name = "carParkId") long carParkId)
			throws MessagingException, IOException {
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		carPark.setCarParkStatus(CarParkStatus.REJECTED);

		carPark.setDateModified(new Date());
		carParkService.save(carPark);

		Users user = appSecurity.getCurrentUser();

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(user.getEmail());
		mail.setSubject("Car Park Submitted");
		mail.setTemplate("emails/carparkrejected");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", user.getFirstName());
		prop.put("carparkname", carPark.getName());
		mail.setProps(prop);
		emailService.sendEmail(mail);

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

	@GetMapping(value = "/findByCarParksAndStartDate")
	@ResponseBody
	public ResponseEntity<Integer> findByCarParksAndStartDate(@RequestParam("carParkId") long carParkId,
			@RequestParam("date") LocalDate date, Model model) {

		CarParks carPark = carParkService.findByCarParkId(carParkId);

		List<Bookings> bookings = bookingService.findByCarParksAndStartDate(carPark, date);

		if (bookings != null) {
			return new ResponseEntity<Integer>(bookings.size(), HttpStatus.OK);
		}

		return new ResponseEntity<Integer>(0, HttpStatus.OK);
	}

	@GetMapping(value = "/findByCarParkSpotsAndStartDate")
	public String findByCarParkSpotsAndStartDate(@RequestParam("carParkSpotId") long carParkSpotId,
			@RequestParam("date") LocalDate date, Model model) {

		CarParkSpots carParkSpot = carParkSpotService.findByCarParkSpotId(carParkSpotId);

		model.addAttribute("spotBookings", bookingService.findByCarParkSpotsAndStartDate(carParkSpot, date));

		return "fragments :: spotBookings";
	}

	@GetMapping(value = "/getFreeSpaces")
	public String getFreeSpaces(@RequestParam("bookingId") long bookingId, Model model) {

		Bookings booking = bookingService.findByBookingId(bookingId);

		List<CarParkSpots> freeSpaces = carParkSpotService.getFreeSpaces(booking.getCarParks().getCarParkId(),
				booking.getStartDate(), booking.getStartTime(), booking.getEndTime(), booking.getIsDisabled(), 4);

		if (!freeSpaces.isEmpty()) {
			model.addAttribute("freeSpaces", freeSpaces);
		} else {
			model.addAttribute("freeSpaces", null);
		}

		return "fragments :: reassignFreeSpots";
	}

	@GetMapping(value = "/reassign")
	public String reassign(@RequestParam("bookingId") long bookingId, @RequestParam("carParkSpotId") long carParkSpotId,
			@RequestParam("activeSpotId") long activeSpotId, @RequestParam("date") LocalDate date, Model model)
			throws MessagingException, IOException {

		CarParkSpots newCarParkSpot = carParkSpotService.findByCarParkSpotId(carParkSpotId);

		CarParkSpots activeCarParkSpot = carParkSpotService.findByCarParkSpotId(activeSpotId);

		bookingService.reassign(bookingId, newCarParkSpot);

		model.addAttribute("spotBookings", bookingService.findByCarParkSpotsAndStartDate(activeCarParkSpot, date));

		return "fragments :: spotBookings";
	}

	@GetMapping(value = "/refund")
	public String refund(@RequestParam("bookingId") long bookingId, @RequestParam("activeSpotId") long activeSpotId,
			@RequestParam("date") LocalDate date, Model model) throws StripeException, MessagingException, IOException {

		CarParkSpots activeCarParkSpot = carParkSpotService.findByCarParkSpotId(activeSpotId);

		bookingService.cancelBooking(bookingId);

		model.addAttribute("spotBookings", bookingService.findByCarParkSpotsAndStartDate(activeCarParkSpot, date));

		return "fragments :: spotBookings";

	}

	@PostMapping("/addClosureDate")
	public String addClosureDate(@RequestParam("carParkId") long carParkId, LocalDate date)
			throws StripeException, MessagingException, IOException {

		CarParks carPark = carParkService.findByCarParkId(carParkId);

		bookingService.cancelBookingsAtDate(carPark, date);

		Set<ClosureDates> closureDates = carPark.getClosureDates();

		ClosureDates newClosureDate = new ClosureDates(date, carPark);
		closureDates.add(newClosureDate);
		carPark.setClosureDates(closureDates);

		carParkService.save(carPark);
		return "redirect:/viewcarparkdetails/" + carParkId;
	}

	/*
	 * @PostMapping("/addBankAccount") public String
	 * addBankAccount(@RequestParam("accountName") String accountName,
	 * 
	 * @RequestParam("accountNumber") String accountNumber,
	 * 
	 * @RequestParam("routingNumber") String routingNumber) throws StripeException {
	 * 
	 * 
	 * Users user = appSecurity.getCurrentUser();
	 * 
	 * Token token = paymentsService.createBankAccount(accountName, routingNumber,
	 * accountNumber);
	 * 
	 * paymentsService.attachBankAccount(token.getId(), user.getStripeId());
	 * 
	 * return "fragments :: bankAccount"; }
	 */

	@GetMapping("/createsepasetupintent")
	public ResponseEntity<String> createSepaSetupIntent(Model model) throws StripeException {

		return new ResponseEntity<String>(paymentsService.createSepaSetupIntent(), HttpStatus.OK);
	}

}
