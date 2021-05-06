package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.model.Users;
import com.ePark.model.CarParks.CarParkStatus;

@DataJpaTest
class CarParkRepositoryTest {
	
	@Autowired
	private CarParkRepository carParkRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testFindAll() {
		
		CarParks carPark1 = new CarParks();

		CarParkSpots carParkSpot1 = new CarParkSpots();
		carParkSpot1.setCarParks(carPark1);

		Set<CarParkSpots> carParkSpots1 = new HashSet<>();
		carParkSpots1.add(carParkSpot1);
		
		CarParks carPark2 = new CarParks();

		CarParkSpots carParkSpot2 = new CarParkSpots();
		carParkSpot2.setCarParks(carPark2);

		Set<CarParkSpots> carParkSpots2 = new HashSet<>();
		carParkSpots2.add(carParkSpot2);
		
		carParkSpot1 = entityManager.persist(carParkSpot1);
		carParkSpot2 = entityManager.persist(carParkSpot2);
		
		carPark1.setCarParkSpots(carParkSpots1);
		carPark2.setCarParkSpots(carParkSpots2);		

		carParkRepo.save(carPark1);
		carParkRepo.save(carPark2);
		
		assertThat(carParkRepo.findAll()).isNotNull();
	}

	@Test
	void testFindByCarParkId() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carParkSpot = entityManager.persist(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);

		carPark = carParkRepo.save(carPark);
		
		assertThat(carParkRepo.findByCarParkId(carPark.getCarParkId())).isEqualTo(carPark);
	}

	@Test
	void testFindByCarParkStatus() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carParkSpot = entityManager.persist(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);

		carPark = carParkRepo.save(carPark);
		
		List<CarParks> actual = carParkRepo.findByCarParkStatus(CarParkStatus.APPROVED);
		
		assertThat(actual).isNotNull();
		
		actual.forEach(c -> assertThat(c.getCarParkStatus()).isEqualTo(CarParkStatus.APPROVED));
	}

	@Test
	void testFindByUsers() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carParkSpot = entityManager.persist(carParkSpot);
		
		Users user = entityManager.persist(new Users());

		carPark.setCarParkSpots(carParkSpots);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);
		
		Set<Users> users = new HashSet<>();
		users.add(user);
		carPark.setUsers(users);

		carPark = carParkRepo.save(carPark);
		
		List<CarParks> expected = new ArrayList<>();
		expected.add(carPark);
		
		assertThat(carParkRepo.findByUsers(user)).isEqualTo(expected);
	}

	@Test
	void testFindByDynamicPricing() {
		
		CarParks carPark = new CarParks();

		CarParkSpots carParkSpot = new CarParkSpots();
		carParkSpot.setCarParks(carPark);

		Set<CarParkSpots> carParkSpots = new HashSet<>();
		carParkSpots.add(carParkSpot);

		carParkSpot = entityManager.persist(carParkSpot);

		carPark.setCarParkSpots(carParkSpots);
		carPark.setDynamicPricing(true);

		carPark = carParkRepo.save(carPark);
		
		List<CarParks> actual = carParkRepo.findByDynamicPricing(true);
		
		assertThat(actual).isNotNull();
		
		actual.forEach(c -> assertThat(c.getDynamicPricing()).isEqualTo(true));
	}

}
