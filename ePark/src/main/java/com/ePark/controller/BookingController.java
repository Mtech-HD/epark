package com.ePark.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.ePark.dto.CarParkDto;
import com.ePark.entity.Bookings;
import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParkTimes;
import com.ePark.entity.CarParks;
import com.ePark.entity.ChargeRequest;
import com.ePark.entity.ChargeRequest.Currency;
import com.ePark.entity.Users;
import com.ePark.entity.Vehicles;
import com.ePark.service.BookingFlow;
import com.ePark.service.BookingService;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;
import com.ePark.service.StripeService;
import com.ePark.service.VehicleService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Controller
@SessionAttributes("bookingFlow")
public class BookingController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private CarParkSpotService carParkSpotService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private StripeService paymentsService;
	
	@Autowired
	private AppSecurityConfig appSecurity;

	@Value("${stripe.secret.key}")
	private String stripePublicKey;

	@ModelAttribute("bookingFlow")
	public BookingFlow getBookingFlow() {
		return new BookingFlow();
	}

	@GetMapping("/booking/{carParkId}")
	public String setCarPark(@PathVariable(name = "carParkId") long carParkId,
			@ModelAttribute("carParkDto") CarParkDto carParkDto,
			@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {

		CarParks carPark = carParkService.findByCarParkId(carParkId);
		bookingFlow.getBooking().setCarParks(carPark);

		return "redirect:/booking/dates";
	}

	@GetMapping("/booking/dates")
	public String getDatesPage(@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {

		bookingFlow.enterStep(BookingFlow.Step.Dates);

		return "/bookingdates";
	}

	@PostMapping("/booking/dates")
	public String dates(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "/bookingdates";
		}

		CarParkSpots spot = carParkSpotService.checkAvailable(bookingFlow.getBooking().getCarParks().getCarParkId(),
				bookingFlow.getBooking().getStartDate(), bookingFlow.getBooking().getStartTime(),
				bookingFlow.getBooking().getEndTime(), bookingFlow.getBooking().getIsDisabled());

		if (spot == null) {
			bindingResult.rejectValue("booking.endTime", "", "No available spaces");
			return "bookingdates";
		}

		bookingFlow.getBooking().setCarParkSpots(spot);

		bookingFlow.completeStep(BookingFlow.Step.Dates);

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);

		return "redirect:/booking/vehicles";
	}

	@PostMapping(value = "/booking/dates", params = "cancel")
	public String cancelDates(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/home";
	}

	@PostMapping(value = "/booking/dates", params = "prices")
	public String bookingPriceFragment(@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {
		return "bookingfragment :: quickSummary";
	}

	@PostMapping(value = "/booking/dates", params = "endDate")
	public ResponseEntity<Object> getBookingEndDate(@RequestParam(value = "dayOfWeek") String dayOfWeek,
			@ModelAttribute("bookingFlow") BookingFlow bookingFlow) {

		List<CarParkTimes> openingTimes = bookingFlow.getBooking().getCarParks().orderTimes();
		CarParkTimes carParkTime = openingTimes.stream().filter(t -> t.getDayOfWeek().toString().equals(dayOfWeek))
				.findFirst().orElse(null);
		;

		return new ResponseEntity<>(carParkTime, HttpStatus.OK);
	}

	@GetMapping("/booking/vehicles")
	public String getDetails(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, Model model) {
		bookingFlow.enterStep(BookingFlow.Step.Vehicle);

		model.addAttribute("user", appSecurity.getCurrentUser());
		
		return "/bookingvehicles";
	}

	@PostMapping(value = "/booking/vehicles", params = "back")
	public String fromVehiclesToDates(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		return "redirect:/booking/dates";
	}

	@PostMapping("/booking/vehicles")
	public String postVehiclesToReview(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		bookingFlow.completeStep(BookingFlow.Step.Vehicle);

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);

		return "redirect:/bookingreview";
	}

	@GetMapping("/bookingreview")
	public String getReviewPage(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, Model model) {
		bookingFlow.enterStep(BookingFlow.Step.Review);

		model.addAttribute("amount", 50 * 100); // in pennies
		model.addAttribute("stripePublicKey", stripePublicKey);
		return "/bookingreview";
	}

	@PostMapping(value = "/booking/review", params = "back")
	public String fromReviewToVehicles(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		return "redirect:/booking/vehicles";
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

		model.addAttribute("amount", 50 * 100); // in cents
		model.addAttribute("stripePublicKey", stripePublicKey);

		model.addAttribute("cards", paymentsService.getCards());

		return "/bookingpayment";
	}

	@PostMapping(value = "/booking/payment", params = "back")
	public String fromPaymentToReview(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("bookingFlow", bookingFlow);
		return "redirect:/bookingreview";
	}

	@PostMapping("/booking/payment")
	public String postPayment(@ModelAttribute("bookingFlow") BookingFlow bookingFlow,
			// @Valid @ModelAttribute("pendingPayment") PendingPayment pendingPayment,
			@RequestParam("paymentMethodId") String paymentMethodId, BindingResult bindingResult,
			SessionStatus sessionStatus, ChargeRequest chargeRequest, Model model) throws StripeException {

		Users user = appSecurity.getCurrentUser();

		if (bindingResult.hasErrors()) {
			return "bookingpayment";
		}

		Bookings booking = bookingFlow.getBooking();

		booking.setUsers(user);
		booking.setAmount(booking.calculatePrice());
		booking.setBookingCreated(LocalDate.now());

		chargeRequest.setDescription("Example charge");
		chargeRequest.setCurrency(Currency.GBP);
		chargeRequest.setAmount(booking.calculatePrice().intValue() * 100);
		chargeRequest.setPaymentMethodId(paymentMethodId);
		chargeRequest.setCustomerId(user.getStripeId());

		PaymentIntent paymentIntent = paymentsService.paymentIntent(chargeRequest);

		if (paymentIntent.getStatus() == "payment_failed") {
			return "redirect:/booking/payment";
		} else {
			booking.setPaymentId(paymentIntent.getId());
			bookingService.save(booking);
			sessionStatus.setComplete();
		}

		
		// model.addAttribute("status", paymentIntent.getStatus());
		// model.addAttribute("chargeId", paymentIntent.getId());
		// model.addAttribute("balance_transaction",
		// paymentIntent.getBalanceTransaction());

		bookingFlow.completeStep(BookingFlow.Step.Payment);
		return "redirect:/booking/complete";
	}

	@GetMapping("/booking/complete")
	public String getCompletePage(@ModelAttribute("bookingFlow") BookingFlow bookingFlow, Model model) {

		return "/bookingcomplete";
	}

	@GetMapping(value = "/clear")
	public String exit(SessionStatus sessionStatus, @RequestParam(value = "page") String page) {

		sessionStatus.setComplete();

		String view = "redirect:/" + page;

		return view;
	}


}
