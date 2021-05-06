package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;

@DataJpaTest
class CarParkTimeRepositoryTest {

	@Autowired
	private CarParkTimeRepository carParkTimePaymentRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testFindByCarParks() {

		CarParks carPark = new CarParks();

		carPark = entityManager.persist(carPark);

		CarParkTimes carParkTime = new CarParkTimes();
		carParkTime.setCarParks(carPark);

		carParkTimePaymentRepo.save(carParkTime);

		List<CarParkTimes> expected = new ArrayList<>();
		expected.add(carParkTime);

		assertThat(carParkTimePaymentRepo.findByCarParks(carPark)).isEqualTo(expected);
	}

	@Test
	void testGetCarParkTimes() {

		CarParks carPark = new CarParks();

		carPark = entityManager.persist(carPark);

		CarParkTimes carParkTime1 = new CarParkTimes();
		carParkTime1.setCarParks(carPark);

		CarParkTimes carParkTime2 = new CarParkTimes();
		carParkTime2.setCarParks(carPark);

		carParkTimePaymentRepo.save(carParkTime1);
		carParkTimePaymentRepo.save(carParkTime2);

		List<CarParkTimes> actual = carParkTimePaymentRepo.getCarParkTimes(carPark.getCarParkId());
		
		assertThat(actual).isNotNull();
		assertThat(actual.get(0)).isEqualTo(carParkTime1);
		assertThat(actual.get(1)).isEqualTo(carParkTime2);
	}

	@Test
	void testFindByCarParkTimeId() {

		CarParks carPark = new CarParks();

		carPark = entityManager.persist(carPark);

		CarParkTimes carParkTime = new CarParkTimes();
		carParkTime.setCarParks(carPark);

		carParkTime = carParkTimePaymentRepo.save(carParkTime);

		assertThat(carParkTimePaymentRepo.findByCarParkTimeId(carParkTime.getCarParkTimeId())).isEqualTo(carParkTime);
	}

}
