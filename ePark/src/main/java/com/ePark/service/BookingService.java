package com.ePark.service;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.Bookings;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.model.EarningsAndBookings;
import com.ePark.model.Bookings.BookingStatus;
import com.ePark.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepo;
	
	private static final BigDecimal maxChangeRateWeek = new BigDecimal(0.1);

	private static final BigDecimal maxChangeRateDay = new BigDecimal(0.2);

	private static final BigDecimal minimumPrice = new BigDecimal(2.00);

	public Bookings save(Bookings booking) {
		return bookingRepo.save(booking);
	}

	public Bookings findByBookingId(long bookingId) {
		return bookingRepo.findByBookingId(bookingId);
	}

	public List<Bookings> findByCarParkSpotsAndStartDateAndBookingStatusNot(CarParkSpots carParkSpot, LocalDate startDate, BookingStatus bookingStatus) {
		return bookingRepo.findByCarParkSpotsAndStartDateAndBookingStatusNot(carParkSpot, startDate, bookingStatus);
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

		revenueDifferencePercent = revenueDifference.divide(target, 2, RoundingMode.HALF_UP);

		return revenueDifferencePercent.multiply(maxChangeAmount).setScale(2, RoundingMode.HALF_UP);
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

			finalPrice = finalPrice.add(maxChangeAmount.multiply(percentageChange)).setScale(2, RoundingMode.HALF_UP);;

		} else {

			percentageChange = rangeForDecrease.subtract(percentageSpacesTaken).divide(rangeForDecrease, 2,
					RoundingMode.HALF_UP);

			finalPrice = finalPrice.subtract(maxChangeAmount.multiply(percentageChange)).setScale(2, RoundingMode.HALF_UP);;
		}

		if (finalPrice.compareTo(minimumPrice) <= 0) {
			finalPrice = minimumPrice;
		}

		return finalPrice;
	}
	
	public Bookings cancelBooking(long bookingId) {
		
		Bookings booking = bookingRepo.findByBookingId(bookingId);

		booking.setBookingStatus(BookingStatus.CANCELLED);

		return bookingRepo.save(booking);
	}

	
	public Bookings reassign(long bookingId, CarParkSpots newCarParkSpot) {
		
		Bookings booking = bookingRepo.findByBookingId(bookingId);

		booking.setCarParkSpots(newCarParkSpot);

		return bookingRepo.save(booking);
	}
	
	public BigDecimal getRevenueForMonth(CarParks carPark) {
		
		LocalDate today = LocalDate.now();
		LocalDate startDate =  today.withDayOfMonth(1);
		LocalDate endDate =  today.withDayOfMonth(today.lengthOfMonth());
		
		return bookingRepo.revenueBetween(carPark.getCarParkId(), BookingStatus.ACTIVE.toString(), startDate, endDate);
	}
	
	
	public void removeInvalidBookings() {
		bookingRepo.removeInvalidBookings();
	}

}
