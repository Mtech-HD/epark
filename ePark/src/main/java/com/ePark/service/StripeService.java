package com.ePark.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ePark.AppSecurityConfig;
import com.ePark.model.ChargeRequest;
import com.ePark.model.Users;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Balance;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.LoginLink;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Payout;
import com.stripe.model.Refund;
import com.stripe.model.SetupIntent;
import com.stripe.model.Transfer;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountCreateParams.BusinessType;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.AccountLinkCreateParams.Type;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.LoginLinkCreateOnAccountParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.SetupIntentCreateParams;

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

	public Customer createCustomer(String name, String email) throws StripeException {
		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("name", name);
		customerParams.put("email", email);
		return Customer.create(customerParams);
	}

	public Card createCard(String sourceId, String customerId) throws StripeException {

		Map<String, Object> retrieveParams = new HashMap<>();
		List<String> expandList = new ArrayList<>();
		expandList.add("sources");
		retrieveParams.put("expand", expandList);
		Customer customer = Customer.retrieve(customerId, retrieveParams, null);

		Map<String, Object> params = new HashMap<>();
		params.put("source", sourceId);

		return (Card) customer.getSources().create(params);
	}

	public void removeCard(String cardId, String customerId) throws StripeException {

		Map<String, Object> retrieveParams = new HashMap<>();
		List<String> expandList = new ArrayList<>();
		expandList.add("sources");
		retrieveParams.put("expand", expandList);
		Customer customer = Customer.retrieve(customerId, retrieveParams, null);

		Card card = (Card) customer.getSources().retrieve(cardId);

		card.delete();
	}

	public PaymentIntent paymentIntent(ChargeRequest chargeRequest) throws StripeException {

		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setCurrency(chargeRequest.getCurrency().toString()).setAmount(chargeRequest.getAmount())
				.setPaymentMethod(chargeRequest.getPaymentMethodId()).setCustomer(chargeRequest.getCustomerId())
				.setConfirm(true).setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL).build();

		return PaymentIntent.create(params);
	}

	public PaymentSourceCollection getCards(String customerId) throws StripeException {

		List<String> expandList = new ArrayList<>();
		expandList.add("sources");

		Map<String, Object> retrieveParams = new HashMap<>();
		retrieveParams.put("expand", expandList);

		Customer customer = Customer.retrieve(customerId, retrieveParams, null);

		Map<String, Object> params = new HashMap<>();
		params.put("object", "card");
		params.put("limit", 3);

		return customer.getSources().list(params);
	}

	public PaymentIntent getPaymentIntent(String paymentIntentId) throws StripeException {

		return PaymentIntent.retrieve(paymentIntentId);
	}

	public Customer getCustomer(String customerId) throws StripeException {

		return Customer.retrieve(customerId);
	}

	public Customer setDefaultCard(String cardId, String customerId) throws StripeException {

		Customer customer = Customer.retrieve(customerId);

		CustomerUpdateParams params = CustomerUpdateParams.builder().setDefaultSource(cardId).build();

		return customer.update(params);
	}

	public void refundPaymentIntent(String paymentIntentId) throws StripeException {

		Refund.create(RefundCreateParams.builder().setPaymentIntent(paymentIntentId).build());
	}

	public String createSepaSetupIntent() throws StripeException {

		Users user = appSecurity.getCurrentUser();

		Customer customer = Customer.retrieve(user.getStripeId());

		SetupIntentCreateParams params = SetupIntentCreateParams.builder().addPaymentMethodType("sepa_debit")
				.setCustomer(customer.getId()).build();

		SetupIntent setupIntent = SetupIntent.create(params);

		return setupIntent.getClientSecret();
	}

	public PaymentMethodCollection getBankAccounts(String customerId) throws StripeException {

		Map<String, Object> params = new HashMap<>();
		params.put("customer", customerId);
		params.put("type", "sepa_debit");

		return PaymentMethod.list(params);
	}

	public Payout createPayout(String bankId, BigDecimal amount) throws StripeException {

		if (bankId == null) {
			return null;
		}

		Map<String, Object> params = new HashMap<>();

		params.put("amount", amount.multiply(new BigDecimal(100)).intValue());
		params.put("currency", "gbp");
		params.put("source_type", "bank_account");
		params.put("destination", bankId);

		return Payout.create(params);
	}

	public Transfer createTransfer(String accountId, BigDecimal amount, String decscription) throws StripeException {

		if (accountId == null) {
			return null;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("amount", amount.multiply(new BigDecimal(100)).intValue());
		params.put("currency", "gbp");
		params.put("destination", accountId);
		params.put("source_type", "card");
		params.put("description", decscription);

		return Transfer.create(params);

	}

	public Account createConnectedAccount(String email) throws StripeException {

		AccountCreateParams params = AccountCreateParams.builder().setCountry("GB")
				.setType(AccountCreateParams.Type.EXPRESS).setBusinessType(BusinessType.INDIVIDUAL)
				.setCapabilities(AccountCreateParams.Capabilities.builder()
						.setCardPayments(
								AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
						.setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
						.build())
				.build();

		return Account.create(params);
	}

	public Account getAccount(String stripeId) throws StripeException {

		return Account.retrieve(stripeId);
	}

	public AccountLink createAccountLink(String accountId, long carParkId) throws StripeException {

		AccountLinkCreateParams params = AccountLinkCreateParams.builder().setAccount(accountId)
				.setRefreshUrl("https://localhost:8443/viewcarparkdetails/" + carParkId + "#/carparkdetails")
				.setReturnUrl("https://localhost:8443/viewcarparkdetails/" + carParkId + "#/carparkdetails")
				.setType(Type.ACCOUNT_ONBOARDING).build();

		return AccountLink.create(params);
	}

	public LoginLink getLoginLink(String accountId) throws StripeException {

		LoginLinkCreateOnAccountParams params = LoginLinkCreateOnAccountParams.builder()
				.setRedirectUrl("https://localhost:8443/home").build();

		return LoginLink.createOnAccount(accountId, params, (RequestOptions) null);
	}

	public String getStripeAccountLink(String accountId, long carParkId) throws StripeException {

		if (accountId == null) {
			return "";
		}

		Account account = Account.retrieve(accountId);

		if (account.getDetailsSubmitted()) {
			return getLoginLink(accountId).getUrl();
		} else {
			return createAccountLink(accountId, carParkId).getUrl();
		}
	}

	public Balance getAccountBalance(String accountId) throws StripeException {

		RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(accountId).build();

		return Balance.retrieve(requestOptions);
	}

	public String accountChecks(String accountId) throws StripeException {

		if (accountId == null) {
			return "No account present";
		}

		Account account = Account.retrieve(accountId);

		if (account.getRequirements().getDisabledReason() != null) {
			return "Account is not fully setup for payouts";
		} else if (!account.getChargesEnabled()) {
			return "Account charges is not enabled";
		} else if (!account.getDetailsSubmitted()) {
			return "Stripe account not setup";
		} else if (!account.getCapabilities().getTransfers().equalsIgnoreCase("active")) {
			return "Account transfers not enabled";
		} else if (account.getExternalAccounts().getData().size() == 0) {
			return "No bank account present";
		}

		return null;
	}

}
