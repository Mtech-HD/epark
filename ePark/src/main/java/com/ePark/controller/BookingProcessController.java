package com.ePark.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ePark.AppSecurityConfig;
import com.ePark.model.Bookings;
import com.ePark.model.Bookings.BookingStatus;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;
import com.ePark.model.ChargeRequest;
import com.ePark.model.ChargeRequest.Currency;
import com.ePark.model.ClosureDates;
import com.ePark.model.Mail;
import com.ePark.model.Users;
import com.ePark.model.Vehicles;
import com.ePark.model.Week;
import com.ePark.service.BookingFlow;
import com.ePark.service.BookingService;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;
import com.ePark.service.EmailService;
import com.ePark.service.StripeService;
import com.ePark.service.VehicleService;
import com.ePark.util.Utils;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Controller
@SessionAttributes("bookingFlow")
public class BookingProcessController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private CarParkSpotService carParkSpotService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private AppSecurityConfig appSecurity;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private Utils utils;

	@Value("${stripe.secret.key}")
	private String stripePublicKey;

	@ModelAttribute("bookingFlow")
	public BookingFlow getBookingFlow() {
		return new BookingFlow();
	}

	@GetMapping("/booking/{carParkId}")
	public String setCarPark(@PathVariable(name = "carParkId") long carParkId,
			@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		Users user = appSecurity.getCurrentUser();
		
		Bookings booking = bookingFlow.getBooking();
		
		CarParks carPark = carParkService.findByCarParkId(carParkId);
		booking.setBookingStatus(BookingStatus.INPROGRESS);
		booking.setUsers(user);
		booking.setCarParks(carPark);
		booking.setUnitPrice(carPark.getPrice());
		booking.setBookingCreated(new Date());

		return "redirect:/booking/dates";
	}

	@GetMapping("/booking/dates")
	public String getDatesPage(@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {

		bookingFlow.enterStep(BookingFlow.Step.Dates);

		return "booking/dates";
	}

	@PostMapping("/booking/dates")
	public String dates(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {

		if (bindingResult.hasErrors()) {
			return "booking/dates";
		}
		
		Bookings booking = bookingFlow.getBooking();

		List<CarParkSpots> spots = carParkSpotService.getFreeSpaces(
				booking.getCarParks().getCarParkId(), booking.getStartDate(),
				booking.getStartTime(), booking.getEndTime(),
				booking.getIsDisabled(), 1000);

		if (spots == null || spots.isEmpty()) {
			bindingResult.rejectValue("booking.endTime", "", "No available spaces");
			return "booking/dates";
		} else if (booking.getStartTime().equals(booking.getEndTime())) {
			bindingResult.rejectValue("booking.endTime", "", "Start and end times are the same");
			return "booking/dates";
		} else if (booking.getEndTime().isBefore(booking.getStartTime())) {
			bindingResult.rejectValue("booking.endTime", "", "End time is before start time");
			return "booking/dates";
		}

		booking.setCarParkSpots(spots.get(0));
		booking.setAmount(booking.calculatePrice());

		bookingFlow.completeStep(BookingFlow.Step.Dates);
		
		bookingFlow.setBooking(bookingService.save(booking));

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);

		return "redirect:/booking/vehicles";
	}

	@Transactional
	@PostMapping(value = "/booking/dates", params = "cancel")
	public String cancelDates(SessionStatus sessionStatus, @ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		bookingService.delete(bookingFlow.getBooking().getBookingId());
		
		sessionStatus.setComplete();
		return "redirect:/home";
	}

	
	@PostMapping(value = "/booking/dates", params = "prices")
	public String bookingTotal(@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		Bookings booking = bookingFlow.getBooking();

		BigDecimal price = new BigDecimal(0);

		if (booking.getCarParks().getDynamicPricing()) {

			BigDecimal weekPrice = bookingService.revenueBasedPrice(booking.getCarParks(),
					BookingStatus.ACTIVE, booking.getStartDate());

			price = weekPrice;

			if (booking.getStartTime() != null && booking.getEndTime() != null) {

				List<CarParkSpots> spots = carParkSpotService.getFreeSpaces(
						booking.getCarParks().getCarParkId(), booking.getStartDate(),
						booking.getStartTime(), booking.getEndTime(),
						booking.getIsDisabled(), 1000);

				int maximumSpaces = booking.getCarParks().getNormalSpots().size();

				if (booking.getIsDisabled()) {
					maximumSpaces = booking.getCarParks().getDisabledSpots().size();
				}

				price = bookingService.getPriceForDay(booking.getCarParks(),
						booking.getStartDate(), weekPrice, spots.size(), maximumSpaces);
			}

			booking.setUnitPrice(price.setScale(2, RoundingMode.HALF_UP));
		}

		return "fragments :: bookingQuickSummary";
	}

	@PostMapping(value = "/booking/dates", params = "openingTimes")
	public ResponseEntity<Object> getBookingEndDate(@RequestParam(value = "date") LocalDate date,
			@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		CarParkTimes carParkTime;
		
		String dayOfWeekString = utils.getDayOfWeek(date);

		Week weekDay = utils.getWeekEnum(dayOfWeekString);
		
		CarParks carPark =  bookingFlow.getBooking().getCarParks();

		ClosureDates closureDate = carPark.getClosureDates().stream().filter(c -> c.getDate().equals(date)).findFirst().orElse(null);
		
		if (closureDate != null) {
			carParkTime = new CarParkTimes(weekDay, null, null, carPark);
			return new ResponseEntity<>(carParkTime, HttpStatus.OK);
		}	
		
		List<CarParkTimes> openingTimes = carPark.orderTimes();
		carParkTime = openingTimes.stream().filter(t -> t.getDayOfWeek().toString().equalsIgnoreCase(weekDay.toString()))
				.findFirst().orElse(null);

		return new ResponseEntity<>(carParkTime, HttpStatus.OK);
	}

	@GetMapping("/booking/vehicles")
	public String showBookingVehicles(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, Model model) {
		
		bookingFlow.enterStep(BookingFlow.Step.Vehicle);

		if (bookingFlow.getBooking().getVehicles() == null) {
			Users user = appSecurity.getCurrentUser();

			Vehicles vehicle = vehicleService.findByUsersAndIsDefault(user.getUserId());

			bookingFlow.getBooking().setVehicles(vehicle);
		}

		model.addAttribute("user", appSecurity.getCurrentUser());

		return "booking/vehicles";
	}

	@PostMapping(value = "/booking/vehicles", params = "back")
	public String fromVehiclesToDates(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		return "redirect:/booking/dates";
	}

	@Transactional
	@PostMapping(value = "/booking/vehicles", params = "cancel")
	public String cancelVehicles(SessionStatus sessionStatus, @ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		System.out.println(bookingFlow.getBooking().getBookingId());
		bookingService.delete(bookingFlow.getBooking().getBookingId());
		
		sessionStatus.setComplete();
		return "redirect:/home";
	}

	@PostMapping("/booking/vehicles")
	public String postVehiclesToReview(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		bookingFlow.completeStep(BookingFlow.Step.Vehicle);
		
		Bookings booking = bookingFlow.getBooking();
		
		bookingFlow.setBooking(bookingService.save(booking));

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);

		return "redirect:/booking/review";
	}

	@GetMapping("/booking/review")
	public String getReviewPage(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, Model model) {
		bookingFlow.enterStep(BookingFlow.Step.Review);

		model.addAttribute("stripePublicKey", stripePublicKey);
		return "booking/review";
	}

	@PostMapping(value = "/booking/review", params = "back")
	public String fromReviewToVehicles(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		return "redirect:/booking/vehicles";
	}

	@Transactional
	@PostMapping(value = "/booking/review", params = "cancel")
	public String cancelReview(SessionStatus sessionStatus, @ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		
		bookingService.delete(bookingFlow.getBooking().getBookingId());
		
		sessionStatus.setComplete();
		return "redirect:/home";
	}

	@PostMapping(value = "/booking/review")
	public String fromReviewToPayment(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {
		bookingFlow.setActive(BookingFlow.Step.Payment);

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		bookingFlow.completeStep(BookingFlow.Step.Review);

		return "redirect:/booking/payment";
	}

	@GetMapping("/booking/payment")
	public String getPaymentPage(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, Model model)
			throws StripeException {
		bookingFlow.enterStep(BookingFlow.Step.Payment);

		Users user = appSecurity.getCurrentUser();

		model.addAttribute("stripePublicKey", stripePublicKey);
		
		model.addAttribute("defaultCard", paymentsService.getCustomer(user.getStripeId()).getDefaultSource());
		
		model.addAttribute("cards", paymentsService.getCards(user.getStripeId()));

		return "booking/payment";
	}

	@PostMapping(value = "/booking/complete", params = "back")
	public String fromPaymentToReview(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		return "redirect:/booking/review";
	}

	@Transactional
	@PostMapping(value = "/booking/complete", params = "cancel")
	public String cancelPayment(SessionStatus sessionStatus, @ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		
		bookingService.delete(bookingFlow.getBooking().getBookingId());
		
		sessionStatus.setComplete();
		return "redirect:/home";
	}

	@PostMapping("/booking/payment")
	public ResponseEntity<Object> postPayment(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			@RequestParam("cardId") String cardId, BindingResult bindingResult,
			ChargeRequest chargeRequest, Model model) throws StripeException {

		Users user = appSecurity.getCurrentUser();

		PaymentIntent paymentIntent = null;

		Bookings booking = bookingFlow.getBooking();

		chargeRequest.setDescription(Long.toString(booking.getBookingId()));
		chargeRequest.setCurrency(Currency.GBP);
		chargeRequest.setAmount((booking.getAmount().multiply(new BigDecimal(100))).intValue());
		chargeRequest.setPaymentMethodId(cardId);
		chargeRequest.setCustomerId(user.getStripeId());

		paymentIntent = paymentsService.paymentIntent(chargeRequest);

		booking.setPaymentId(paymentIntent.getId());

		Map<String, Object> response = new HashMap<>();
		response.put("clientSecret", paymentIntent.getClientSecret());
		response.put("status", paymentIntent.getStatus());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/booking/complete")
	public String getCompletePage(BookingFlow bookingFlow, @RequestParam("paymentIntentId") String paymentIntentId,
			@RequestParam("cardId") String cardId, Model model, SessionStatus sessionStatus)
			throws StripeException, MessagingException, IOException {

		PaymentIntent paymentIntent = paymentsService.getPaymentIntent(paymentIntentId);

		if (paymentIntent.getStatus().equals("requires_confirmation")) {
			paymentIntent = paymentIntent.confirm();
		}

		bookingFlow.completeStep(BookingFlow.Step.Payment);

		Bookings booking = bookingFlow.getBooking();
		booking.setBookingStatus(BookingStatus.ACTIVE);
		booking.setBookingCreated(new Date());
		bookingService.save(booking);

		String address = booking.getFullAddress();
		Users user = appSecurity.getCurrentUser();

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(user.getEmail());
		mail.setSubject("Booking Confirmation");
		mail.setTemplate("emails/bookingconfirmation");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", user.getFirstName());
		prop.put("receipturl", paymentIntent.getCharges().getData().stream().findFirst().get().getReceiptUrl());
		prop.put("directionsurl", "https://www.google.com/maps/dir/Current+Location/" + address);
		prop.put("booking", booking);
		mail.setProps(prop);
		emailService.sendEmail(mail);
		
		model.addAttribute("directionsurl", "https://www.google.com/maps/dir/Current+Location/" + address);
		model.addAttribute("payment", paymentIntent);
		model.addAttribute("booking", bookingService.findByBookingId(bookingFlow.getBooking().getBookingId()));

		sessionStatus.setComplete();

		return "booking/complete";

	}

	@Transactional
	@GetMapping(value = "/clear")
	public String exit(SessionStatus sessionStatus, 
			@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			@RequestParam(value = "page") String page) {
		
		if (bookingFlow.getBooking() != null) {
			bookingService.delete(bookingFlow.getBooking().getBookingId());
		}

		sessionStatus.setComplete();

		String view = "redirect:/" + page;

		return view;
	}

}
