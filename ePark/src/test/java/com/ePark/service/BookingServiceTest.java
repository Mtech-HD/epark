package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.model.Bookings;
import com.ePark.model.Bookings.BookingStatus;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.model.EarningsAndBookings;
import com.ePark.repository.BookingRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BookingServiceTest {

	@Mock
	private BookingRepository bookingRepo;

	@InjectMocks
	private BookingService bookingService;

	@Test
	void testSave() {
		Bookings booking = new Bookings(1000L);

		Mockito.when(bookingRepo.save(booking)).thenReturn(booking);

		assertThat(bookingService.save(booking)).isEqualTo(booking);
	}

	@Test
	void testFindByBookingId() {
		Bookings booking = new Bookings(1000L);

		Mockito.when(bookingRepo.findByBookingId(1001L)).thenReturn(booking);

		assertThat(bookingService.findByBookingId(1001L)).isEqualTo(booking);
	}

	@Test
	void testFindByCarParkSpotsAndStartDateAndBookingStatusNot() {

		Bookings booking = new Bookings(1000L);
		booking.setBookingStatus(BookingStatus.INPROGRESS);

		LocalDate date = LocalDate.of(2021, 10, 10);
		booking.setStartDate(date);

		CarParkSpots carParkSpot = new CarParkSpots(1000L);
		booking.setCarParkSpots(carParkSpot);

		List<Bookings> bookingList = new ArrayList<>();
		bookingList.add(booking);

		Mockito.when(bookingRepo.findByCarParkSpotsAndStartDateAndBookingStatusNot(carParkSpot, date,
				BookingStatus.INPROGRESS)).thenReturn(bookingList);

		assertThat(bookingService.findByCarParkSpotsAndStartDateAndBookingStatusNot(carParkSpot, date,
				BookingStatus.INPROGRESS)).isEqualTo(bookingList);

	}

	@Test
	void testFindByCarParksAndStartDate() {

		Bookings booking = new Bookings(1000L);

		LocalDate date = LocalDate.of(2021, 10, 10);
		booking.setStartDate(date);

		CarParks carParks = new CarParks(1000L);
		booking.setCarParks(carParks);

		List<Bookings> bookingList = new ArrayList<>();
		bookingList.add(booking);

		Mockito.when(bookingRepo.findByCarParksAndStartDate(carParks, date)).thenReturn(bookingList);

		assertThat(bookingService.findByCarParksAndStartDate(carParks, date)).isEqualTo(bookingList);
	}

	@Test
	void testGetBookingsForMonth() {

		Bookings booking = new Bookings(1000L);

		LocalDate date = LocalDate.of(2021, 03, 01);
		booking.setStartDate(date);

		CarParks carParks = new CarParks(1000L);
		booking.setCarParks(carParks);

		booking.setAmount(new BigDecimal(10.00));

		EarningsAndBookings earningsAndBookings = new EarningsAndBookings() {
			public BigDecimal getEarnings() {
				return booking.getAmount();
			}

			public long getBookings() {
				return 1;
			}
		};

		Mockito.when(
				bookingRepo.getBookingsForMonth(carParks.getCarParkId(), "202103", BookingStatus.ACTIVE.toString()))
				.thenReturn(earningsAndBookings);

		assertThat(bookingService.getBookingsForMonth(carParks.getCarParkId(), "202103")).isEqualTo(earningsAndBookings);
	}

	@Test
	void testDelete() {
		Bookings booking = new Bookings(1000L);
		
		Mockito.when(bookingRepo.findByBookingId(1001L)).thenReturn(booking);
		
		Mockito.when(bookingRepo.existsById(booking.getBookingId())).thenReturn(false);
		
		//bookingService.delete(1000L);
		// verify the mocks
		//verify(bookingRepo, times(1)).deleteBooking(1000L);
		
		assertFalse(bookingRepo.existsById(booking.getBookingId()));
	}

	@Test
	void testRevenueBasedPrice() {
		CarParks carPark = new CarParks(1000L);
		carPark.setTargetRevenue(new BigDecimal(100.00));
		carPark.setPrice(new BigDecimal(5.00));
		
		LocalDate date = LocalDate.of(2021, 01, 01);

		BigDecimal revenueThisWeek = new BigDecimal(56.00);

		Mockito.when(bookingRepo.revenueBetween(anyLong(), any(), any(), any())).thenReturn(revenueThisWeek);
		
		BigDecimal expected = new BigDecimal(4.78).setScale(2, RoundingMode.HALF_UP);
		
		assertThat(bookingService.revenueBasedPrice(carPark, BookingStatus.ACTIVE, date)).isEqualTo(expected);
	}

	@Test
	void testGetChangeAmount() {

		BigDecimal revenue = new BigDecimal(100.00);
		BigDecimal target = new BigDecimal(50.00);
		boolean targetMet = true;
		BigDecimal maxChangeAmount = new BigDecimal(0.5);
		
		BigDecimal expected = new BigDecimal(0.5).setScale(2, RoundingMode.HALF_UP);
		
		assertThat(bookingService.getChangeAmount(revenue, target, targetMet, maxChangeAmount)).isEqualTo(expected);

	}

	@Test
	void testGetPriceForDay() {
		CarParks carPark = new CarParks(1000L);
		carPark.setPrice(new BigDecimal(5.00));
		
		LocalDate date = LocalDate.of(2021, 01, 01);
		BigDecimal weekPrice = new BigDecimal(5.00);
		int available = 5;
		int allSpaces = 15;
		
		BigDecimal expected = new BigDecimal(5.45).setScale(2, RoundingMode.HALF_UP);
		
		assertThat(bookingService.getPriceForDay(carPark, date, weekPrice, available, allSpaces)).isEqualTo(expected);
	}

	@Test
	void testCancelBooking() {

		Bookings booking = new Bookings(1000L);

		booking.setBookingStatus(BookingStatus.CANCELLED);

		Mockito.when(bookingRepo.save(booking)).thenReturn(booking);

		Mockito.when(bookingRepo.findByBookingId(booking.getBookingId())).thenReturn(booking);

		bookingRepo.save(booking);

		assertThat(bookingService.cancelBooking(booking.getBookingId()).getBookingStatus())
				.isEqualTo(BookingStatus.CANCELLED);
	}

	@Test
	void testReassign() {

		Bookings booking = new Bookings(1000L);

		CarParkSpots carParkSpotOld = new CarParkSpots(1000L);
		booking.setCarParkSpots(carParkSpotOld);

		Mockito.when(bookingRepo.save(booking)).thenReturn(booking);

		Mockito.when(bookingRepo.findByBookingId(booking.getBookingId())).thenReturn(booking);

		bookingRepo.save(booking);

		CarParkSpots carParkSpotNew = new CarParkSpots(1001L);
		booking.setCarParkSpots(carParkSpotOld);

		assertThat(bookingService.reassign(booking.getBookingId(), carParkSpotNew).getCarParkSpots())
				.isEqualTo(carParkSpotNew);
	}

	@Test
	void testGetRevenueForMonth() {

		Bookings booking = new Bookings(1000L);

		CarParks carPark = new CarParks(1000L);
		booking.setCarParks(carPark);

		BigDecimal amount = new BigDecimal(10.00);
		booking.setAmount(amount);
		
		LocalDate nowDate = LocalDate.now();
		booking.setStartDate(nowDate);

		Mockito.when(bookingRepo.save(booking)).thenReturn(booking);

		bookingRepo.save(booking);
		
		
		LocalDate today = LocalDate.now();
		LocalDate startDate =  today.withDayOfMonth(1);
		LocalDate endDate =  today.withDayOfMonth(today.lengthOfMonth());
		
		Mockito.when(bookingRepo.revenueBetween(carPark.getCarParkId(), BookingStatus.ACTIVE.toString(), startDate, endDate)).thenReturn(amount);

		assertThat(bookingService.getRevenueForMonth(carPark)).isEqualTo(amount);
	}

}
