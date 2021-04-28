package com.ePark.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ePark.entity.CarParkPayments;
import com.ePark.entity.CarParks;
import com.ePark.entity.CarParks.CarParkStatus;
import com.ePark.entity.EarningsAndBookings;
import com.ePark.entity.Mail;
import com.ePark.service.BookingService;
import com.ePark.service.CarParkPaymentService;
import com.ePark.service.CarParkService;
import com.ePark.service.EmailService;
import com.ePark.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Transfer;

@Component
public class ScheduledProcess {

	@Autowired
	private CarParkService carParkService;

	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private CarParkPaymentService carParkPaymentService;

	@Autowired
	private StripeService paymentService;
	
	//Runs first day of every month
	@Scheduled(cron="0 0 0 1 1/1 *")
	public void carParkPaymentForPreviousMonth() {
		
		List<CarParks> carParks = carParkService.findByCarParkStatus(CarParkStatus.APPROVED);

		YearMonth thisMonth = YearMonth.now();
		YearMonth lastMonth = thisMonth.minusMonths(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		String yearMonth = formatter.format(lastMonth);

		for (CarParks carPark : carParks) {

			EarningsAndBookings earningsAndBookings = bookingService.getBookingsForMonth(carPark.getCarParkId(),
					yearMonth);

			CarParkPayments carParkPayment = new CarParkPayments(carPark, yearMonth, earningsAndBookings.getEarnings(),
					earningsAndBookings.getBookings(), false, null, null);

			carParkPaymentService.saveIfNotExists(carParkPayment);
		}
	}
	

	//Runs midnight everyday
	@Scheduled(cron = "0 0 0 * * * ")
	//@Scheduled(cron = "0 04 08 21 * ?")
	public void payoutOwners() throws StripeException, MessagingException, IOException {

		List<CarParks> carParks = carParkService.findByCarParkStatus(CarParkStatus.APPROVED);

		for (CarParks carPark : carParks) {

			List<CarParkPayments> carParkPayments = carParkPaymentService.findByCarParksAndPaid(carPark.getCarParkId(), false);

			for (CarParkPayments payment : carParkPayments) {

				if (payment.getAmount().compareTo(new BigDecimal(0)) > 0) {
					
					String accountChecks = paymentService.accountChecks(carPark.getStripeId());
					

					if (carPark.getStripeId() != null && accountChecks == null) {
						
						String description = "Car Park ID: " + carPark.getCarParkId() + ", yearMonth: " + payment.getYearMonth() + ", Bookings: " + payment.getBookings();

						Transfer transfer = paymentService.createTransfer(
								carPark.getStripeId(), payment.getAmount(), description);

						if (transfer != null) {
								payment.setPaid(true);
								payment.setPaymentId(transfer.getId());
								payment = carParkPaymentService.save(payment);
								
								Mail mail = new Mail();
						        mail.setFrom("ePark Admin <official-epark@outlook.com>");
						        mail.setMailTo(carPark.getEmail());
						        mail.setSubject("Monthly Payment Notification");
						        mail.setTemplate("emails/monthlypayment");
						        
						        Map<String, Object> prop = new HashMap<String, Object>();
						        prop.put("name", carPark.getName());
						        prop.put("payment", payment);
						       
						        mail.setProps(prop);
						        emailService.sendEmail(mail);
								
						}

					} else {
						payment.setFailedReason(accountChecks);
						carParkPaymentService.save(payment);
					}

				}

			}

		}

	}

}
