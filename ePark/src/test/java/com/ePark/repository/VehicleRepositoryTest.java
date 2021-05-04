package com.ePark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ePark.model.Users;
import com.ePark.model.Vehicles;

@DataJpaTest
class VehicleRepositoryTest {

	@Autowired
	private VehicleRepository vehicleRepo;

	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test
	void testSave() {
		
		Users user = entityManager.persist(new Users());

		Vehicles vehicle = new Vehicles("TT1 TT1", "ford", "blue", user, true);

		vehicle = vehicleRepo.save(vehicle);

		assertThat(vehicle).isNotNull();
	}

	@Test
	void testFindByUsersAndIsDefault() {

		Users user = entityManager.persist(new Users());

		Vehicles vehicle = new Vehicles("TT1 TT1", "ford", "blue", user, true);

		vehicle = vehicleRepo.save(vehicle);

		assertThat(vehicleRepo.findByUsersAndIsDefault(user.getUserId(), true)).isEqualTo(vehicle);

	}
	
	@Test
	void testFindByUsers() {
		
		Users user = entityManager.persist(new Users());

		Vehicles vehicle = new Vehicles("TT1 TT1", "ford", "blue", user, true);

		vehicle = vehicleRepo.save(vehicle);
		
		List<Vehicles> expectList = new ArrayList<>();
		expectList.add(vehicle);

		assertThat(vehicleRepo.findByUsers(user)).isEqualTo(expectList);
	}
	

	@Test
	void testFindByVehicleId() {
		
		Users user = entityManager.persist(new Users());

		Vehicles vehicle = new Vehicles("TT1 TT1", "ford", "blue", user, true);

		vehicle = vehicleRepo.save(vehicle);

		assertThat(vehicleRepo.findByVehicleId(vehicle.getVehicleId())).isEqualTo(vehicle);
	}

}
