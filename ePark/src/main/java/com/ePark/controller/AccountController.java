package com.ePark.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.model.Bookings;
import com.ePark.model.Mail;
import com.ePark.model.Users;
import com.ePark.service.BookingFlow;
import com.ePark.service.BookingService;
import com.ePark.service.EmailService;
import com.ePark.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Controller
public class AccountController {

	@Autowired
	private StripeService paymentsService;

	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private AppSecurityConfig appSecurity;

	@GetMapping("/accountdetails")
	public ModelAndView showAccountDetails() throws StripeException {

		Users user = appSecurity.getCurrentUser();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("accountdetails");

		if (user.getStripeId() != null) {
			mv.addObject("cards", paymentsService.getCards(user.getStripeId()));
			mv.addObject("defaultCard", paymentsService.getCustomer(user.getStripeId()).getDefaultSource());
		}

		mv.addObject("user", user);

		return mv;
	}

	@GetMapping(value = "/removeCard")
	public String removeCardFromCustomer(@RequestParam("cardId") String cardId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) throws StripeException {

		Users user = appSecurity.getCurrentUser();

		paymentsService.removeCard(cardId, user.getStripeId());

		model.addAttribute("cards", paymentsService.getCards(user.getStripeId()));

		model.addAttribute("defaultCard", paymentsService.getCustomer(user.getStripeId()).getDefaultSource());

		return "fragments :: cardsTable";
	}

	@GetMapping(value = "/addcard")
	public String addCardToCustomer(@RequestParam("tokenId") String tokenId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) throws StripeException {

		Users user = appSecurity.getCurrentUser();

		paymentsService.createCard(tokenId, user.getStripeId());

		model.addAttribute("cards", paymentsService.getCards(user.getStripeId()));

		model.addAttribute("defaultCard", paymentsService.getCustomer(user.getStripeId()).getDefaultSource());

		return "fragments :: cardsTable";
	}

	@GetMapping(value = "/setdefaultcard")
	public String setDefaultCard(@RequestParam("cardId") String cardId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) throws StripeException {

		Users user = appSecurity.getCurrentUser();

		paymentsService.setDefaultCard(cardId, user.getStripeId());

		model.addAttribute("cards", paymentsService.getCards(user.getStripeId()));

		model.addAttribute("defaultCard", paymentsService.getCustomer(user.getStripeId()).getDefaultSource());

		return "fragments :: cardsTable";
	}

	@GetMapping(value = "/getBooking")
	public String getBooking(@RequestParam("bookingId") long bookingId, Model model) throws StripeException {

		Bookings booking = bookingService.findByBookingId(bookingId);

		PaymentIntent payment = paymentsService.getPaymentIntent(booking.getPaymentId());

		model.addAttribute("booking", booking);

		model.addAttribute("payment", payment);

		return "fragments :: bookingsInfo";
	}
	
	@GetMapping(value = "/cancelbooking")
	public String cancelBooking(@RequestParam("bookingId") long bookingId, Model model) throws StripeException, MessagingException, IOException {
		
		Bookings booking = bookingService.cancelBooking(bookingId);

		paymentsService.refundPaymentIntent(booking.getPaymentId());
		PaymentIntent paymentIntent = paymentsService.getPaymentIntent(booking.getPaymentId());	
		
		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(booking.getUsers().getEmail());
		mail.setSubject("Booking Cancelled");
		mail.setTemplate("emails/usercancelbooking");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", booking.getUsers().getFirstName());
		prop.put("bookingId", booking.getBookingId());
		prop.put("refundAmount", booking.getAmount());
		prop.put("receipturl", paymentIntent.getCharges().getData().stream().findFirst().get().getReceiptUrl());
		mail.setProps(prop);
		emailService.sendEmail(mail);

		model.addAttribute("booking", booking);

		model.addAttribute("payment", paymentIntent);
		
		return "fragments :: bookingsInfo";
	}

}
