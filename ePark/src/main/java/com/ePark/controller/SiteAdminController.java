package com.ePark.controller;

import java.io.IOException;
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
import com.ePark.entity.CarParks;
import com.ePark.entity.ChargeRequest;
import com.ePark.entity.Mail;
import com.ePark.entity.Users;
import com.ePark.entity.ChargeRequest.Currency;
import com.ePark.service.CarParkService;
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
	private UserService userService;

	@RequestMapping("/siteadminbookings/{carParkId}")
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
	
	@GetMapping(value="/chargeCustomer")
	public ResponseEntity<String> chargeCustomerFine(@RequestParam("stripeId") String stripeId,
			ChargeRequest chargeRequest, Model model) throws StripeException, MessagingException, IOException {
		
		Users user = userService.findByStripeId(stripeId);
		
		String paymentMethodId = paymentsService.getCustomer(user.getStripeId()).getInvoiceSettings().getDefaultPaymentMethod();
		
		chargeRequest.setDescription("Late Departure Fee");
		chargeRequest.setCurrency(Currency.GBP);
		chargeRequest.setAmount(1000);
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
        prop.put("amount", "10.00");
        mail.setProps(prop);
        emailService.sendEmail(mail);
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
}
