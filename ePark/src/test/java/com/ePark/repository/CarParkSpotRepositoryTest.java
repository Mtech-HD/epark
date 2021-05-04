package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import com.ePark.model.Users;
import com.ePark.model.Bookings.BookingStatus;

@DataJpaTest
class CarParkSpotRepositoryTest {

	@Autowired
	private CarParkSpotRepository carParkSpotRepo;

	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void testGetFreeSpaces() {
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot1 = new CarParkSpots(carPark, false, 1);
		CarParkSpots carParkSpot2 = new CarParkSpots(carPark, false, 2);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot1);
		carParkSpots.add(carParkSpot2);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);

		Users user = entityManager.persist(new Users());
		
		carParkSpotRepo.save(carParkSpot1);
		carParkSpotRepo.save(carParkSpot2);
		
		LocalDate date = LocalDate.of(2021, 01, 01);
		
		LocalTime startTime = LocalTime.of(10, 00);
		LocalTime endTime = LocalTime.of(11, 00);

		Bookings booking = new Bookings();
		booking.setCarParks(carPark);
		booking.setCarParkSpots(carParkSpot1);
		booking.setUsers(user);
		booking.setStartDate(date);
		booking.setEndDate(date);
		booking.setStartTime(startTime);
		booking.setEndTime(endTime);
		booking.setBookingStatus(BookingStatus.ACTIVE);
		
		booking = entityManager.persist(booking);

		List<CarParkSpots> expected = new ArrayList<>();
		expected.add(carParkSpot2);
		
		assertThat(carParkSpotRepo.getFreeSpaces(carPark.getCarParkId(), date, startTime, endTime, false, 1000)).isEqualTo(expected);
	}
	
	@Test
	void testFindLargestSpaceNumber() {
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot1 = new CarParkSpots(carPark, false, 1);
		CarParkSpots carParkSpot2 = new CarParkSpots(carPark, false, 2);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot1);
		carParkSpots.add(carParkSpot2);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);
	
		carParkSpotRepo.save(carParkSpot1);
		carParkSpotRepo.save(carParkSpot2);
		
		assertThat(carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId())).isEqualTo(carParkSpot2.getSpaceNumber());
	}


	@Test
	void testDeleteByCarParkSpotId() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots(carPark, false, 1);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = entityManager.persist(carPark);
	
		carParkSpot = carParkSpotRepo.save(carParkSpot);
		
		carParkSpotRepo.deleteByCarParkSpotId(carParkSpot.getCarParkSpotId());
		
		assertThat(carParkSpotRepo.findByCarParkSpotId(carParkSpot.getCarParkSpotId())).isNull();
		
	}

}
