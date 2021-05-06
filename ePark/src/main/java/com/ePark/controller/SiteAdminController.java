package com.ePark.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.model.Bookings;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.model.ChargeRequest;
import com.ePark.model.Mail;
import com.ePark.model.Users;
import com.ePark.model.Bookings.BookingStatus;
import com.ePark.model.ChargeRequest.Currency;
import com.ePark.service.BookingService;
import com.ePark.service.CarParkService;
import com.ePark.service.CarParkSpotService;
import com.ePark.service.EmailService;
import com.ePark.service.StripeService;
import com.ePark.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Controller
public class SiteAdminController {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private AppSecurityConfig appSecurity;

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CarParkSpotService carParkSpotService;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserService userService;

	@RequestMapping("/siteadmin/{carParkId}")
	public ModelAndView siteAdminBookings(@PathVariable("carParkId") long carParkId) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/siteadminbookings");
		mv.addObject("carpark", carParkService.findByCarParkId(carParkId));
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

	@GetMapping(value = "/siteadmin/chargeCustomer")
	public ResponseEntity<String> chargeCustomerFine(@RequestParam("stripeId") String stripeId,
			@RequestParam("amount") BigDecimal amount, Model model)
			throws StripeException, MessagingException, IOException {

		ChargeRequest chargeRequest = new ChargeRequest();

		Users user = userService.findByStripeId(stripeId);

		String paymentMethodId = paymentsService.getCustomer(user.getStripeId()).getInvoiceSettings()
				.getDefaultPaymentMethod();

		chargeRequest.setDescription("Late Departure Fee");
		chargeRequest.setCurrency(Currency.GBP);
		chargeRequest.setAmount(amount.multiply(new BigDecimal(100)).intValue());
		chargeRequest.setPaymentMethodId(paymentMethodId);
		chargeRequest.setCustomerId(user.getStripeId());

		PaymentIntent paymentIntent = paymentsService.paymentIntent(chargeRequest);

		if (paymentIntent.getStatus() == "payment_failed") {
			return new ResponseEntity<>("fail", HttpStatus.OK);
		}

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(user.getEmail());
		mail.setSubject("Late Departure Fee");
		mail.setTemplate("emails/latedeparture");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", user.getFirstName());
		prop.put("receipturl", paymentIntent.getCharges().getData().stream().findFirst().get().getReceiptUrl());
		prop.put("amount", amount);
		mail.setProps(prop);
		emailService.sendEmail(mail);

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

	@GetMapping(value = "/siteadmin/reassign")
	public String reassign(@RequestParam("bookingId") long bookingId, @RequestParam("carParkSpotId") long carParkSpotId,
			@RequestParam("activeSpotId") long activeSpotId, @RequestParam("date") LocalDate date, Model model)
			throws MessagingException, IOException {

		CarParkSpots newCarParkSpot = carParkSpotService.findByCarParkSpotId(carParkSpotId);

		CarParkSpots activeCarParkSpot = carParkSpotService.findByCarParkSpotId(activeSpotId);

		Bookings booking = bookingService.reassign(bookingId, newCarParkSpot);

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(booking.getUsers().getEmail());
		mail.setSubject("Booking Reassigned Confirmation");
		mail.setTemplate("emails/reassignconfirmation");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", booking.getUsers().getFirstName());
		prop.put("bookingId", booking.getBookingId());
		prop.put("spaceNumber", booking.getCarParkSpots().getSpaceNumber());
		mail.setProps(prop);
		emailService.sendEmail(mail);

		model.addAttribute("spotBookings", bookingService
				.findByCarParkSpotsAndStartDateAndBookingStatusNot(activeCarParkSpot, date, BookingStatus.INPROGRESS));

		return "fragments :: spotBookings";
	}

	@GetMapping(value = "/siteadmin/cancel")
	public String refund(@RequestParam("bookingId") long bookingId, @RequestParam("activeSpotId") long activeSpotId,
			@RequestParam("date") LocalDate date, Model model) throws StripeException, MessagingException, IOException {

		CarParkSpots activeCarParkSpot = carParkSpotService.findByCarParkSpotId(activeSpotId);

		Bookings booking = bookingService.cancelBooking(bookingId);

		paymentsService.refundPaymentIntent(booking.getPaymentId());
		PaymentIntent paymentIntent = paymentsService.getPaymentIntent(booking.getPaymentId());

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(booking.getUsers().getEmail());
		mail.setSubject("Booking Cancelled");
		mail.setTemplate("emails/siteadmincancelbooking");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", booking.getUsers().getFirstName());
		prop.put("bookingId", booking.getBookingId());
		prop.put("refundAmount", booking.getAmount());
		prop.put("receipturl", paymentIntent.getCharges().getData().stream().findFirst().get().getReceiptUrl());
		mail.setProps(prop);
		emailService.sendEmail(mail);

		model.addAttribute("spotBookings", bookingService
				.findByCarParkSpotsAndStartDateAndBookingStatusNot(activeCarParkSpot, date, BookingStatus.INPROGRESS));

		return "fragments :: spotBookings";

	}

	@GetMapping(value = "/siteadmin/findByCarParkSpotsAndStartDate")
	public String findByCarParkSpotsAndStartDate(@RequestParam("carParkSpotId") long carParkSpotId,
			@RequestParam("date") LocalDate date, Model model) {

		CarParkSpots carParkSpot = carParkSpotService.findByCarParkSpotId(carParkSpotId);

		model.addAttribute("spotBookings", bookingService.findByCarParkSpotsAndStartDateAndBookingStatusNot(carParkSpot,
				date, BookingStatus.INPROGRESS));

		return "fragments :: spotBookings";
	}

	@GetMapping(value = "/siteadmin/getFreeSpaces")
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

}
