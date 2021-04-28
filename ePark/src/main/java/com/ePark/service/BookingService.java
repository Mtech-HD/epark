package com.ePark.service;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.Bookings;
import com.ePark.entity.Bookings.BookingStatus;
import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParks;
import com.ePark.entity.EarningsAndBookings;
import com.ePark.entity.Mail;
import com.ePark.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private StripeService paymentsService;
	
	@Autowired
	private EmailService emailService;
	

	private static final BigDecimal maxChangeRateWeek = new BigDecimal(0.1);

	private static final BigDecimal maxChangeRateDay = new BigDecimal(0.2);

	private static final BigDecimal minimumPrice = new BigDecimal(2.00);

	public Bookings save(Bookings booking) {
		return bookingRepo.save(booking);
	}

	public Bookings findByBookingId(long bookingId) {
		return bookingRepo.findByBookingId(bookingId);
	}

	public List<Bookings> findByCarParkSpotsAndStartDate(CarParkSpots carParkSpotId, LocalDate startDate) {
		return bookingRepo.findByCarParkSpotsAndStartDate(carParkSpotId, startDate);
	}

	public List<Bookings> findByCarParksAndStartDate(CarParks carPark, LocalDate startDate) {
		return bookingRepo.findByCarParksAndStartDate(carPark, startDate);
	}
	
	public EarningsAndBookings getBookingsForMonth(long carParkId, String lastMonth) {
 	
		return bookingRepo.getBookingsForMonth(carParkId, lastMonth, BookingStatus.ACTIVE.toString());
	}

	public void delete(long bookingId) {
		bookingRepo.deleteBooking(bookingId);
	}

	public BigDecimal revenueBasedPrice(CarParks carPark, BookingStatus bookingStatus, LocalDate date) {

		LocalDate monday = date.with(previousOrSame(MONDAY));
		LocalDate sunday = date.with(nextOrSame(SUNDAY));

		BigDecimal newPrice = carPark.getPrice();

		BigDecimal revenueThisWeek = bookingRepo.revenueBetween(carPark.getCarParkId(), bookingStatus.toString(),
				monday, sunday);

		BigDecimal changeAmount = new BigDecimal(0);

		BigDecimal maxChangeAmount = carPark.getPrice().multiply(maxChangeRateWeek);

		boolean targetMet = revenueThisWeek.compareTo(carPark.getTargetRevenue()) >= 0;

		if (targetMet) {

			changeAmount = getChangeAmount(revenueThisWeek, carPark.getTargetRevenue(), true, maxChangeAmount);

			newPrice = carPark.getPrice().add(changeAmount);

		} else {

			changeAmount = getChangeAmount(revenueThisWeek, carPark.getTargetRevenue(), false, maxChangeAmount);

			newPrice = carPark.getPrice().subtract(changeAmount);
		}

		if (newPrice.compareTo(minimumPrice) <= 0) {
			newPrice = minimumPrice;
		}

		return newPrice.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getChangeAmount(BigDecimal revenue, BigDecimal target, boolean targetMet,
			BigDecimal maxChangeAmount) {

		BigDecimal revenueDifferencePercent;

		BigDecimal revenueDifference = new BigDecimal(0);

		if (targetMet) {
			revenueDifference = revenue.subtract(target);
		} else {
			revenueDifference = target.subtract(revenue);
		}

		revenueDifferencePercent = revenueDifference.divide(target, 8, RoundingMode.HALF_UP);

		return revenueDifferencePercent.multiply(maxChangeAmount);
	}

	public BigDecimal getPriceForDay(CarParks carPark, LocalDate date, BigDecimal weekPrice, int available,
			int allSpaces) {

		if (allSpaces == 0) {
			return weekPrice;
		}

		BigDecimal finalPrice = weekPrice;

		BigDecimal maxChangeAmount = carPark.getPrice().multiply(maxChangeRateDay);

		BigDecimal spacesAvailable = new BigDecimal(available);

		BigDecimal totalspaces = new BigDecimal(allSpaces);

		BigDecimal percentageSpacesTaken = (totalspaces.subtract(spacesAvailable)).divide(totalspaces, 2,
				RoundingMode.HALF_UP);

		BigDecimal rangeForDecrease = new BigDecimal(0.40);

		BigDecimal rangeForIncrease = new BigDecimal(0.60);

		boolean increasePrice = percentageSpacesTaken.compareTo(rangeForDecrease) >= 0;

		BigDecimal percentageChange = new BigDecimal(0);

		if (increasePrice) {

			percentageChange = percentageSpacesTaken.subtract(rangeForDecrease).divide(rangeForIncrease, 2,
					RoundingMode.HALF_UP);

			finalPrice = finalPrice.add(maxChangeAmount.multiply(percentageChange));

		} else {

			percentageChange = rangeForDecrease.subtract(percentageSpacesTaken).divide(rangeForDecrease, 2,
					RoundingMode.HALF_UP);

			finalPrice = finalPrice.subtract(maxChangeAmount.multiply(percentageChange));
		}

		if (finalPrice.compareTo(minimumPrice) <= 0) {
			finalPrice = minimumPrice;
		}

		return finalPrice;
	}

	public void cancelBookingsAtDate(CarParks carPark, LocalDate date) throws StripeException, MessagingException, IOException {
		List<Bookings> bookings = bookingRepo.findByCarParksAndStartDate(carPark, date);

		if (bookings != null) {
			for (Bookings b : bookings) {
				paymentsService.refundPaymentIntent(b.getPaymentId());
				PaymentIntent paymentIntent = paymentsService.getPaymentIntent(b.getPaymentId());
				b.setBookingStatus(BookingStatus.CANCELLED);
				
				Mail mail = new Mail();
				mail.setFrom("ePark Admin <official-epark@outlook.com>");
				mail.setMailTo(b.getUsers().getEmail());
				mail.setSubject("Refund Confirmation");
				mail.setTemplate("emails/refundconfirmation");

				Map<String, Object> prop = new HashMap<String, Object>();
				prop.put("name", b.getUsers().getFirstName());
				prop.put("bookingId", b.getBookingId());
				prop.put("refundAmount", b.getAmount());
				prop.put("receipturl", paymentIntent.getCharges().getData().stream().findFirst().get().getReceiptUrl());
				mail.setProps(prop);
				emailService.sendEmail(mail);
			}
		}
	}
	
	public void cancelBooking(long bookingId) throws StripeException, MessagingException, IOException {
		
		Bookings booking = bookingRepo.findByBookingId(bookingId);
		
		paymentsService.refundPaymentIntent(booking.getPaymentId());
		PaymentIntent paymentIntent = paymentsService.getPaymentIntent(booking.getPaymentId());

		booking.setBookingStatus(BookingStatus.CANCELLED);

		bookingRepo.save(booking);

		Mail mail = new Mail();
		mail.setFrom("ePark Admin <official-epark@outlook.com>");
		mail.setMailTo(booking.getUsers().getEmail());
		mail.setSubject("Refund Confirmation");
		mail.setTemplate("emails/refundconfirmation");

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("name", booking.getUsers().getFirstName());
		prop.put("bookingId", booking.getBookingId());
		prop.put("refundAmount", booking.getAmount());
		prop.put("receipturl", paymentIntent.getCharges().getData().stream().findFirst().get().getReceiptUrl());
		mail.setProps(prop);
		emailService.sendEmail(mail);
	}
	
	public void reassign(long bookingId, CarParkSpots newCarParkSpot) throws MessagingException, IOException {
		
		Bookings booking = bookingRepo.findByBookingId(bookingId);

		booking.setCarParkSpots(newCarParkSpot);

		bookingRepo.save(booking);

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
	}
	
	public BigDecimal getRevenueForMonth(CarParks carPark) {
		
		LocalDate today = LocalDate.now();
		LocalDate startDate =  today.withDayOfMonth(1);
		LocalDate endDate =  today.withDayOfMonth(today.lengthOfMonth());
		
		return bookingRepo.revenueBetween(carPark.getCarParkId(), BookingStatus.ACTIVE.toString(), startDate, endDate);
	}

}
