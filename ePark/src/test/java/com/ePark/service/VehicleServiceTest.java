package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.model.Users;
import com.ePark.model.Vehicles;
import com.ePark.repository.VehicleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class VehicleServiceTest {
	
	@Mock
	private VehicleRepository vehicleRepo;

	@InjectMocks
	private VehicleService vehicleService;

	@Test
	void testFindAll() {
		
		Users user = new Users(1000L);
		
		Vehicles vehicle1 = new Vehicles("TT1 TT1", "Ford", "blue", user, false);
		
		Vehicles vehicle2 = new Vehicles("TT2 TT2", "Ford", "red", user, false);
		
		List<Vehicles> vehicleList = new ArrayList<>();
		vehicleList.add(vehicle1);
		vehicleList.add(vehicle2);

		Mockito.when(vehicleRepo.findAll()).thenReturn(vehicleList);

		assertThat(vehicleService.findAll()).isEqualTo(vehicleList);
	}

	@Test
	void testSave() {
		Users user = new Users(1000L);
		
		Vehicles vehicle = new Vehicles("TT1 TT1", "Ford", "blue", user, false);

		Mockito.when(vehicleRepo.save(vehicle)).thenReturn(vehicle);

		assertThat(vehicleService.save(vehicle)).isEqualTo(vehicle);
	}

	@Test
	void testFindByUsers() {
		
		Users user = new Users(1000L);
		
		Vehicles vehicle1 = new Vehicles("TT1 TT1", "Ford", "blue", user, false);
		
		Vehicles vehicle2 = new Vehicles("TT2 TT2", "Ford", "red", user, false);
		
		List<Vehicles> vehicleList = new ArrayList<>();
		vehicleList.add(vehicle1);
		vehicleList.add(vehicle2);

		Mockito.when(vehicleRepo.findByUsers(user)).thenReturn(vehicleList);

		assertThat(vehicleService.findByUsers(user)).isEqualTo(vehicleList);
	}

	@Test
	void testRemoveVehicleFromUser() {
		
		Users user = new Users(1000L);
		
		Vehicles vehicle = new Vehicles(1000L);
		
		Mockito.when(vehicleRepo.findByVehicleId(vehicle.getVehicleId())).thenReturn(vehicle);
		
		vehicleService.removeVehicleFromUser(vehicle.getVehicleId(), user);

		verify(vehicleRepo, times(1)).save(vehicle);
	}

	@Test
	void testSetDefault() {
		
		Users user = new Users(1000L);
		
		Vehicles vehicle = new Vehicles("TT1 TT1", "Ford", "blue", user, false);

		Mockito.when(vehicleRepo.findByVehicleId(vehicle.getVehicleId())).thenReturn(vehicle);
		
		vehicle.setIsDefault(true);
		
		Mockito.when(vehicleRepo.save(vehicle)).thenReturn(vehicle);
		
		assertThat(vehicleService.setDefault(vehicle.getVehicleId(), true).getIsDefault()).isEqualTo(true);	
	}

	@Test
	void testFindByVehicleId() {
		
		Users user = new Users(1000L);
		
		Vehicles vehicle = new Vehicles(1000L);

		Mockito.when(vehicleRepo.findByVehicleId(vehicle.getVehicleId())).thenReturn(vehicle);

		assertThat(vehicleService.findByVehicleId(vehicle.getVehicleId())).isEqualTo(vehicle);
	}

	@Test
	void testFindByUsersAndIsDefault() {
		
		Users user = new Users(1000L);
		
		Vehicles vehicle = new Vehicles("TT1 TT1", "Ford", "blue", user, true);

		Mockito.when(vehicleRepo.findByUsersAndIsDefault(user.getUserId(), true)).thenReturn(vehicle);

		assertThat(vehicleService.findByUsersAndIsDefault(user.getUserId())).isEqualTo(vehicle);
	}

}
