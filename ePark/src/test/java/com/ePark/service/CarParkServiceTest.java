package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.AppSecurityConfig;
import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParks;
import com.ePark.model.Users;
import com.ePark.model.CarParks.CarParkStatus;
import com.ePark.repository.CarParkRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CarParkServiceTest {
	
	@Mock
	private CarParkRepository carParkRepo;
	
	@Mock
	private AppSecurityConfig appSecurity;

	@InjectMocks
	private CarParkService carParkService;

	@Test
	void testFindAll() {
		
		CarParks carPark1 = new CarParks(1000L);
		
		CarParks carPark2 = new CarParks(1001L);
		
		List<CarParks> carParkList = new ArrayList<>();
		carParkList.add(carPark1);
		carParkList.add(carPark2);

		Mockito.when(carParkRepo.findAll()).thenReturn(carParkList);

		assertThat(carParkService.findAll()).isEqualTo(carParkList);
	}

	@Test
	void testSaveCarParkDto() {
		
		CarParks carPark = new CarParks();
		
		CarParkDto carParkDto = new CarParkDto();
		
		Mockito.when(carParkRepo.save(any(CarParks.class))).thenReturn(carPark);
		
		assertThat(carParkService.save(carParkDto)).isEqualTo(carPark);
	}

	@Test
	void testUpdate() {
		
		CarParks carPark = new CarParks(1000L);
		carPark.setCarParkCity("London");
		
		CarParks carParkUpdated = new CarParks(1000L);
		carParkUpdated.setCarParkCity("Leicester");
				
		CarParkDto carParkDto = new CarParkDto();
		carParkDto.setCarParkId(1000L);
		carParkDto.setCarParkCity("London");

		Mockito.when(carParkRepo.findByCarParkId(carPark.getCarParkId())).thenReturn(carPark);
		
		Mockito.when(carParkRepo.save(carPark)).thenReturn(carParkUpdated);
		
		assertThat(carParkService.update(carParkDto).getCarParkCity()).isEqualTo("Leicester");	
	}

	@Test
	void testSaveCarParks() {
		CarParks carPark = new CarParks(1000L);

		Mockito.when(carParkRepo.save(carPark)).thenReturn(carPark);

		assertThat(carParkService.save(carPark)).isEqualTo(carPark);
	}

	@Test
	void testFindByCarParkId() {
		CarParks carPark = new CarParks(1000L);

		Mockito.when(carParkRepo.findByCarParkId(carPark.getCarParkId())).thenReturn(carPark);

		assertThat(carParkService.findByCarParkId(carPark.getCarParkId())).isEqualTo(carPark);
	}

	@Test
	void testFindByCarParkStatus() {
		CarParks carPark = new CarParks(1000L);
		carPark.setCarParkStatus(CarParkStatus.APPROVED);
		
		List<CarParks> carParkList = new ArrayList<>();
		carParkList.add(carPark);

		Mockito.when(carParkRepo.findByCarParkStatus(CarParkStatus.APPROVED)).thenReturn(carParkList);

		assertThat(carParkService.findByCarParkStatus(carPark.getCarParkStatus()).get(0).getCarParkStatus()).isEqualTo(CarParkStatus.APPROVED);
	}

	@Test
	void testFindByUsers() {
		CarParks carPark = new CarParks(1000L);
		
		Users user = new Users();
		Set<Users> userList = new HashSet<>();
		userList.add(user);
		
		carPark.setUsers(userList);
		
		List<CarParks> carParkList = new ArrayList<>();
		carParkList.add(carPark);

		Mockito.when(carParkRepo.findByUsers(user)).thenReturn(carParkList);

		assertThat(carParkService.findByUsers(user).get(0)).isEqualTo(carPark);
	}

	@Test
	void testAddUserToCarPark() {
		CarParks carPark = new CarParks(1000L);
		
		Set<Users> userList =  new HashSet<>();
		
		Users user1 = new Users();
		
		Users user2 = new Users();
		
		userList.add(user1);
		
		carPark.setUsers(userList);
		
		Mockito.when(carParkRepo.findByCarParkId(carPark.getCarParkId())).thenReturn(carPark);
		
		Mockito.when(carParkRepo.save(carPark)).thenReturn(carPark);
		
		assertThat(carParkService.addUserToCarPark(carPark.getCarParkId(), user2).getUsers().size()).isEqualTo(2);
	}


}
