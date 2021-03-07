package com.ePark.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ePark.AppSecurityConfig;
import com.ePark.dto.UserRegistrationDto;
import com.ePark.entity.ChargeRequest;
import com.ePark.entity.Users;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.RefundCreateParams;

@Service
public class StripeService {

	@Autowired
	private AppSecurityConfig appSecurity;

	@Value("${stripe.secret.key}")
	private String secretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = secretKey;
	}

	public Customer createCustomer(UserRegistrationDto registrationDto) throws StripeException {
		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("name", registrationDto.getFirstName() + " " + registrationDto.getLastName());
		customerParams.put("email", registrationDto.getEmail());
		return Customer.create(customerParams);
	}

	public PaymentMethod createCard(String paymentMethodId) throws StripeException {

		Users user = appSecurity.getCurrentUser();

		PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

		PaymentMethodAttachParams params = PaymentMethodAttachParams.builder().setCustomer(user.getStripeId()).build();

		return paymentMethod.attach(params);
	}

	public void removeCard(String paymentMethodId) throws StripeException {

		PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

		paymentMethod.detach();
	}

	public PaymentIntent paymentIntent(ChargeRequest chargeRequest) throws StripeException {

		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setCurrency(chargeRequest.getCurrency().toString()).setAmount(chargeRequest.getAmount())
				.setPaymentMethod(chargeRequest.getPaymentMethodId()).setCustomer(chargeRequest.getCustomerId())
				.setConfirm(true).setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL).build();

		return PaymentIntent.create(params);

	}

	public PaymentMethodCollection getCards() throws StripeException {

		Users user = appSecurity.getCurrentUser();

		Map<String, Object> params = new HashMap<>();
		params.put("customer", user.getStripeId());
		params.put("type", "card");

		return PaymentMethod.list(params);
	}

	public PaymentIntent getPaymentIntent(String paymentIntentId) throws StripeException {

		return PaymentIntent.retrieve(paymentIntentId);
	}

	public Customer getCustomer(String customerId) throws StripeException {

		return Customer.retrieve(customerId);
	}

	public Customer setDefaultCard(String paymentMethodId, Customer customer) throws StripeException {

		Map<String, Object> default_payment_method = new HashMap<>();
		default_payment_method.put("default_payment_method", paymentMethodId);

		Map<String, Object> invoice_settings = new HashMap<>();
		invoice_settings.put("invoice_settings", default_payment_method);

		return customer.update(invoice_settings);
	}

	public void refundPaymentIntent(String paymentIntentId) throws StripeException {

		Refund.create(RefundCreateParams.builder().setPaymentIntent(paymentIntentId).build());
	}
}
