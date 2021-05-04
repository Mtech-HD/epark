package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ePark.model.CarParkPayments;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;

@DataJpaTest
class CarParkPaymentRepositoryTest {
	
	@Autowired
	private CarParkPaymentRepository carParkPaymentRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testFindByCarParksAndYearMonth() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		CarParkPayments carParkPayment = new CarParkPayments();
		
		carPark = entityManager.persist(carPark);
		
		carParkPayment.setCarParks(carPark);
		carParkPayment.setYearMonth("202101");
		
		carParkPaymentRepo.save(carParkPayment);
		
		assertThat(carParkPaymentRepo.findByCarParksAndYearMonth(carParkPayment.getCarParks(), carParkPayment.getYearMonth())).isEqualTo(carParkPayment);
	}

	@Test
	void testFindByCarParksAndPaid() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		CarParkPayments carParkPayment = new CarParkPayments();
		
		carPark = entityManager.persist(carPark);
		
		carParkPayment.setCarParks(carPark);
		carParkPayment.setYearMonth("202101");
		carParkPayment.setPaid(false);
		
		carParkPaymentRepo.save(carParkPayment);
		
		List<CarParkPayments> expectedCarParkPayments = new ArrayList<>();
		expectedCarParkPayments.add(carParkPayment);
		
		assertThat(carParkPaymentRepo.findByCarParksAndPaid(carParkPayment.getCarParks(), false)).isEqualTo(expectedCarParkPayments);
	}

}
