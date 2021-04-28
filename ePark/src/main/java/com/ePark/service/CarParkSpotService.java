package com.ePark.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParks;
import com.ePark.repository.CarParkSpotRepository;

@Service
public class CarParkSpotService {

	@Autowired
	private CarParkSpotRepository carParkSpotRepo;
	
	public List<CarParkSpots> findByCarParks(CarParks carPark) {
		
		return carParkSpotRepo.findByCarParks(carPark);
	}
	
	public CarParkSpots findByCarParkSpotId(long carParkSpotId) {
		return carParkSpotRepo.findByCarParkSpotId(carParkSpotId);
	}
	
	/*
	 * public CarParkSpots checkAvailable(long carParkId, LocalDate bookingDate,
	 * LocalTime startTime, LocalTime endTime, boolean isDisabled, int length) {
	 * 
	 * return carParkSpotRepo.checkAvailable(carParkId, bookingDate, startTime,
	 * endTime, isDisabled, length); }
	 */
	
	public List<CarParkSpots> getFreeSpaces(long carParkId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, boolean isDisabled, int length) {
		
		return carParkSpotRepo.getFreeSpaces(carParkId, bookingDate, startTime, endTime, isDisabled, length);
	}
	
	public void updateSpots(int newSpaceNumber, int newDisabledNumber, CarParks carPark) {
		
		int spaceRange;
		int disabledRange;
		
		if (newSpaceNumber > carPark.getCarParkSpots().size()) {
			spaceRange = newSpaceNumber - carPark.getCarParkSpots().size();
			addSpots(carPark, spaceRange);
		} else if (newSpaceNumber < carPark.getCarParkSpots().size()) {
			spaceRange = carPark.getCarParkSpots().size() - newSpaceNumber;
			removeSpots(carPark, spaceRange);
		} 

		if (newDisabledNumber > carPark.getDisabledSpots().size()) {
			disabledRange = newDisabledNumber - carPark.getDisabledSpots().size();
			increaseDisabledSpots(carPark, disabledRange);
		} else if (newDisabledNumber < carPark.getDisabledSpots().size()) {
			disabledRange = carPark.getDisabledSpots().size() - newDisabledNumber;
			decreaseDisabledSpots(carPark, disabledRange);
		}
	}
	
	public void addSpots(CarParks carPark, int spaces) {
		
		int highestSpaceNumber = carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId());
		
		for (int i = 0 ; i < spaces; i++) {
			CarParkSpots carParkSpot = new CarParkSpots(carPark, false, highestSpaceNumber);
			highestSpaceNumber++;
			
			carParkSpotRepo.save(carParkSpot);
		}
	}
	
	public void removeSpots(CarParks carPark, int spaces) {
		
		int highestSpaceNumber = carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId());
		
		for (int i = 0 ; i < spaces; i++) {
			
			CarParkSpots carParkSpot = carParkSpotRepo.findByCarParksAndSpaceNumber(carPark, highestSpaceNumber);
			carParkSpotRepo.deleteByCarParkSpotId(carParkSpot.getCarParkSpotId());
			highestSpaceNumber--;
		}	
	}
	
	public void increaseDisabledSpots(CarParks carPark, int disabledRange) {
		
		List<CarParkSpots> carParkSpots = carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark, false);
		System.out.println(carParkSpots.size());
		int index = 0;
		
		for (int i = 0 ; i < disabledRange; i++) {
			
			CarParkSpots carParkSpot = carParkSpots.get(index);
			carParkSpot.setIsDisabled(true);
			index++;
			
			carParkSpotRepo.save(carParkSpot);
		}	
	}
	
	public void decreaseDisabledSpots(CarParks carPark, int disabledRange) {
		
		List<CarParkSpots> carParkSpots = carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark, true);
		int index = carParkSpots.size() - 1;
		
		for (int i = 0 ; i < disabledRange; i++) {
			
			CarParkSpots carParkSpot = carParkSpots.get(index);
			carParkSpot.setIsDisabled(false);
			index--;
			
			carParkSpotRepo.save(carParkSpot);
		}	
	}
}
