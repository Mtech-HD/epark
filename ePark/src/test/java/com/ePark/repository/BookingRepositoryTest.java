package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ePark.model.Bookings;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.model.EarningsAndBookings;
import com.ePark.model.Users;
import com.ePark.model.Bookings.BookingStatus;

@DataJpaTest
class BookingRepositoryTest {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testFindByBookingId() {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);

		booking = bookingRepo.save(booking);

		assertThat(bookingRepo.findByBookingId(booking.getBookingId())).isEqualTo(booking);
	}

	@Test
	void testFindByCarParkSpotsAndStartDateAndBookingStatusNot() {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		LocalDate startDate = LocalDate.of(2021, 04, 01);

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);
		booking.setStart(startDate);
		booking.setBookingStatus(BookingStatus.ACTIVE);

		booking = bookingRepo.save(booking);

		List<Bookings> expectedBookings = new ArrayList<>();
		expectedBookings.add(booking);

		assertThat(bookingRepo.findByCarParkSpotsAndStartDateAndBookingStatusNot(carParkSpot, startDate,
				BookingStatus.CANCELLED)).isEqualTo(expectedBookings);
	}

	@Test
	void testFindByCarParksAndStartDate() {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		LocalDate startDate = LocalDate.of(2021, 04, 01);

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);
		booking.setStart(startDate);

		booking = bookingRepo.save(booking);

		List<Bookings> expectedBookings = new ArrayList<>();
		expectedBookings.add(booking);

		assertThat(bookingRepo.findByCarParksAndStartDate(carPark, startDate)).isEqualTo(expectedBookings);
	}

	@Test
	void testDeleteBooking() {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);

		booking = bookingRepo.save(booking);

		bookingRepo.deleteBooking(booking.getBookingId());

		assertThat(bookingRepo.findByBookingId(booking.getBookingId())).isNull();
	}

	@Test
	void testRevenueBetween() throws ParseException {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		BigDecimal amount = new BigDecimal(10.00).setScale(2);

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = "2021-04-02 00:00:00";
		Date createDate = dateformat.parse(date);

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);
		booking.setBookingCreated(createDate);
		booking.setAmount(amount);
		booking.setBookingStatus(BookingStatus.ACTIVE);

		booking = bookingRepo.save(booking);

		LocalDate monday = LocalDate.of(2021, 04, 01);
		LocalDate sunday = LocalDate.of(2021, 04, 07);

		assertThat(bookingRepo.revenueBetween(booking.getCarParks().getCarParkId(), BookingStatus.ACTIVE.toString(),
				monday, sunday)).isEqualTo(amount);
	}

	@Test
	void testGetBookingsForMonth() throws ParseException {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		BigDecimal amount = new BigDecimal(10.59).setScale(2, RoundingMode.HALF_UP);

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);
		booking.setStartDate(LocalDate.of(2021, 04, 15));
		booking.setBookingStatus(BookingStatus.ACTIVE);
		booking.setAmount(amount);

		booking = bookingRepo.save(booking);

		EarningsAndBookings earningsAndBookings = bookingRepo.getBookingsForMonth(booking.getCarParks().getCarParkId(),
				"202104", BookingStatus.ACTIVE.toString());

		assertThat(earningsAndBookings).isNotNull();
		assertThat(earningsAndBookings.getBookings()).isEqualTo(1);
		assertThat(earningsAndBookings.getEarnings()).isEqualTo(amount);
	}

	@Test
	void testRemoveInvalidBookings() throws ParseException {

		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		carParkSpot = entityManager.persist(carParkSpot);

		Users user = entityManager.persist(new Users());

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = "2021-04-02 00:00:00";
		Date createDate = dateformat.parse(date);

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot);
		booking.setUsers(user);
		booking.setBookingCreated(createDate);
		booking.setBookingStatus(BookingStatus.INPROGRESS);

		booking = bookingRepo.save(booking);

		bookingRepo.removeInvalidBookings();

		assertThat(bookingRepo.findByBookingId(booking.getBookingId())).isNull();
	}

}
