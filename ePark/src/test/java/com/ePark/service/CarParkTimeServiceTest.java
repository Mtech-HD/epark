package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;
import com.ePark.model.Week;
import com.ePark.repository.CarParkTimeRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CarParkTimeServiceTest {

	@Mock
	private CarParkTimeRepository carParkTimeRepo;

	@InjectMocks
	private CarParkTimeService carParkTimeService;
	
	@Test
	void testFindByCarParks() {
		
		CarParkTimes carParkTime = new CarParkTimes();
		CarParks carPark = new CarParks(1000L);
		carParkTime.setCarParks(carPark);

		List<CarParkTimes> carParkTimeList = new ArrayList<>();
		carParkTimeList.add(carParkTime);

		Mockito.when(carParkTimeRepo.findByCarParks(carPark)).thenReturn(carParkTimeList);

		assertThat(carParkTimeService.findByCarParks(carPark)).isEqualTo(carParkTimeList);
	}

	@Test
	void testGetCarParkTimes() {
		
		CarParkTimes carParkTime1 = new CarParkTimes();
		CarParks carPark = new CarParks(1000L);
		carParkTime1.setCarParks(carPark);
		
		CarParkTimes carParkTime2 = new CarParkTimes();
		carParkTime2.setCarParks(carPark);

		List<CarParkTimes> carParkTimeList = new ArrayList<>();
		carParkTimeList.add(carParkTime1);
		carParkTimeList.add(carParkTime2);

		Mockito.when(carParkTimeRepo.getCarParkTimes(carPark.getCarParkId())).thenReturn(carParkTimeList);

		assertThat(carParkTimeService.getCarParkTimes(carPark.getCarParkId())).isEqualTo(carParkTimeList);
	}

	/*
	 * @Test void testFindByCarParkTimeId() { CarParkTimes carParkTime = new
	 * CarParkTimes(1000L);
	 * 
	 * Mockito.when(carParkTimeRepo.findByCarParks(carPark)).thenReturn(
	 * carParkTimeList);
	 * 
	 * assertThat(carParkTimeService.findByCarParks(carPark)).isEqualTo(
	 * carParkTimeList); }
	 */

	@Test
	void testUpdateTimes() {
		
		CarParkDto carParkDto = new CarParkDto();
		CarParks carPark = new CarParks(1000L);
		
		carParkDto.setCarParkId(carPark.getCarParkId());
		
		LocalTime time = LocalTime.of(10, 0, 0, 0);
		
		carParkDto.setMondayFrom(time);
		
		CarParkTimes carParkTime = new CarParkTimes();
		carParkTime.setDayOfWeek(Week.MONDAY);
		
		List<CarParkTimes> carParkTimeList = new ArrayList<>();
		carParkTimeList.add(carParkTime);
		
		Mockito.when(carParkTimeService.updateTimes(carPark, carParkDto)).thenReturn(carParkTimeList);

		assertThat(carParkTimeService.updateTimes(carPark, carParkDto).get(0).getOpenTime()).isEqualTo(time);

	}

}
