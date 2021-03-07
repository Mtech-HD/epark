package com.ePark.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ePark.AppSecurityConfig;
import com.ePark.entity.Bookings;
import com.ePark.entity.Users;
import com.ePark.service.BookingFlow;
import com.ePark.service.BookingService;
import com.ePark.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

@Controller
public class AccountDetails {

	@Autowired
	private StripeService paymentsService;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private AppSecurityConfig appSecurity;

	@GetMapping("/accountdetails")
	public ModelAndView showAccountDetails() throws StripeException {

		Users user = appSecurity.getCurrentUser();

		ModelAndView mv = new ModelAndView();
		mv.setViewName("accountdetails");
		mv.addObject("cards", paymentsService.getCards());
		mv.addObject("defaultCard", paymentsService.getCustomer(user.getStripeId()).getInvoiceSettings().getDefaultPaymentMethod());
		mv.addObject("user", user);
		mv.addObject("paymentsService", paymentsService);
		return mv;
	}

	@GetMapping(value = "/removeCard", params = "paymentMethodId")
	public String removeCardFromCustomer(@RequestParam("paymentMethodId") String paymentMethodId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow) throws StripeException {
		
		paymentsService.removeCard(paymentMethodId);
		
		model.addAttribute("cards", paymentsService.getCards());

		return "bookingfragment :: cardsTable";
	}
	
	@GetMapping(value = "/addcard", params = "paymentMethodId")
	public String addCardToCustomer(@RequestParam("paymentMethodId") String paymentMethodId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow)
			throws StripeException {

		paymentsService.createCard(paymentMethodId);

		model.addAttribute("cards", paymentsService.getCards());

		return "bookingfragment :: cardsTable";
	}
	
	@GetMapping(value = "/setdefaultcard", params = "paymentMethodId")
	public String setDefaultCard(@RequestParam("paymentMethodId") String paymentMethodId, Model model,
			@ModelAttribute("bookingFlow") Optional<BookingFlow> bookingFlow)
			throws StripeException {
		
		Users user = appSecurity.getCurrentUser();
		
		Customer customer = paymentsService.getCustomer(user.getStripeId());

		paymentsService.setDefaultCard(paymentMethodId, customer);

		model.addAttribute("cards", paymentsService.getCards());
		
		model.addAttribute("defaultCard", paymentsService.getCustomer(user.getStripeId()).getInvoiceSettings().getDefaultPaymentMethod());
		
		return "bookingfragment :: cardsTable";
	}
	
	@GetMapping(value = "/getBooking")
	public String getBooking(@RequestParam("bookingId") long bookingId, Model model) throws StripeException {
		
		Bookings booking = bookingService.findByBookingId(bookingId);

		PaymentIntent payment = paymentsService.getPaymentIntent(booking.getPaymentId());

		model.addAttribute("booking", booking);
		
		model.addAttribute("payment", payment);

		return "bookingfragment :: bookingsInfo";
	}

	
}
