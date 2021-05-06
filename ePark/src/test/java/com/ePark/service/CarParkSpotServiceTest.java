package com.ePark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
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

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.repository.BookingRepository;
import com.ePark.repository.CarParkRepository;
import com.ePark.repository.CarParkSpotRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CarParkSpotServiceTest {
	
	@Mock
	private CarParkSpotRepository carParkSpotRepo;

	@InjectMocks
	private CarParkSpotService carParkSpotService;
	
	@Mock
	private CarParkRepository carParkRepo;

	@Mock
	private BookingRepository bookingRepo;
	
	@Test
	void testFindByCarParks() {
		
		CarParkSpots carParkSpot = new CarParkSpots(1000L);

		CarParks carParks = new CarParks(1000L);
		carParkSpot.setCarParks(carParks);

		List<CarParkSpots> carParkSpotList = new ArrayList<>();
		carParkSpotList.add(carParkSpot);

		Mockito.when(carParkSpotRepo.findByCarParks(carParks)).thenReturn(carParkSpotList);

		assertThat(carParkSpotService.findByCarParks(carParks)).isEqualTo(carParkSpotList);
	}

	@Test
	void testFindByCarParkSpotId() {
		CarParkSpots carParkSpot = new CarParkSpots(1000L);
		
		Mockito.when(carParkSpotRepo.findByCarParkSpotId(carParkSpot.getCarParkSpotId())).thenReturn(carParkSpot);

		assertThat(carParkSpotService.findByCarParkSpotId(carParkSpot.getCarParkSpotId())).isEqualTo(carParkSpot);
	}

	@Test
	void testGetFreeSpaces() {

		CarParkSpots carParkSpot1 = new CarParkSpots(1000L);

		CarParks carParks = new CarParks(1000L);
		
		LocalDate date = LocalDate.of(2021, 01, 01);
		LocalTime startTime = LocalTime.of(10, 0, 0);
		LocalTime endTime = LocalTime.of(11, 0, 0);
		
		List<CarParkSpots> availableSpots = new ArrayList<>();
		availableSpots.add(carParkSpot1);

		
		Mockito.when(carParkSpotRepo.getFreeSpaces(carParks.getCarParkId(), date, startTime, endTime, false, 1000)).thenReturn(availableSpots);
		
		assertThat(carParkSpotService.getFreeSpaces(carParks.getCarParkId(), date, startTime, endTime, false, 1000)).isEqualTo(availableSpots);
			
	}

	@Test
	void testAddSpots() {
		
		CarParks carPark = new CarParks(1000L);
		
		CarParkSpots carParkSpot1 = new CarParkSpots(carPark, false, 1);
		CarParkSpots carParkSpot2 = new CarParkSpots(carPark, false, 2);
		List<CarParkSpots> carParkSpots = new ArrayList<>();
		carParkSpots.add(carParkSpot1);
		carParkSpots.add(carParkSpot2);
		
		Mockito.when(carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId())).thenReturn(carParkSpot2.getSpaceNumber());
		
		carParkSpotService.addSpots(carPark, 1);
		
		verify(carParkSpotRepo, times(1)).save(any(CarParkSpots.class));
	}

	@Test
	void testRemoveSpots() {
		
		CarParks carPark = new CarParks(1000L);
		
		CarParkSpots carParkSpot2 = new CarParkSpots(carPark, false, 2);
		
		Mockito.when(carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId())).thenReturn(carParkSpot2.getSpaceNumber());
		
		Mockito.when(carParkSpotRepo.findByCarParksAndSpaceNumber(carPark, carParkSpot2.getSpaceNumber())).thenReturn(carParkSpot2);
		
		carParkSpotService.removeSpots(carPark, 1);
		
		verify(carParkSpotRepo, times(1)).deleteByCarParkSpotId(carParkSpot2.getCarParkSpotId());
	}

	@Test
	void testIncreaseDisabledSpots() {
		
		CarParks carPark = new CarParks();
		
		CarParkSpots carParkSpot1 = new CarParkSpots(carPark, false, 1);
		CarParkSpots carParkSpot2 = new CarParkSpots(carPark, false, 2);
		List<CarParkSpots> carParkSpots = new ArrayList<>();
		carParkSpots.add(carParkSpot1);
		carParkSpots.add(carParkSpot2);
		
		Mockito.when(carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark, false)).thenReturn(carParkSpots);
		
		carParkSpotService.increaseDisabledSpots(carPark, 2);
		
		verify(carParkSpotRepo, times(2)).save(any(CarParkSpots.class));
	}

	@Test
	void testDecreaseDisabledSpots() {
		
		CarParks carPark = new CarParks();
		
		CarParkSpots carParkSpot1 = new CarParkSpots(carPark, true, 1);
		CarParkSpots carParkSpot2 = new CarParkSpots(carPark, true, 2);
		List<CarParkSpots> carParkSpots = new ArrayList<>();
		carParkSpots.add(carParkSpot1);
		carParkSpots.add(carParkSpot2);
		
		Mockito.when(carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark, true)).thenReturn(carParkSpots);
		
		carParkSpotService.decreaseDisabledSpots(carPark, 2);
		
		verify(carParkSpotRepo, times(2)).save(any(CarParkSpots.class));
	}

}
