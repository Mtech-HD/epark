package com.ePark.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
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

	public List<CarParkSpots> getFreeSpaces(long carParkId, LocalDate bookingDate, LocalTime startTime,
			LocalTime endTime, boolean isDisabled, int length) {

		return carParkSpotRepo.getFreeSpaces(carParkId, bookingDate, startTime, endTime, isDisabled, length);
	}

	public void updateSpots(int newSpaceNumber, int newDisabledNumber, CarParks carPark) {

		int spaceRange;
		int disabledRange;

		if (newSpaceNumber > carPark.getCarParkSpots().size()) {
			spaceRange = newSpaceNumber - carPark.getCarParkSpots().size();
			System.out.println("add " + spaceRange);
			addSpots(carPark, spaceRange);
		} else if (newSpaceNumber < carPark.getCarParkSpots().size()) {
			spaceRange = carPark.getCarParkSpots().size() - newSpaceNumber;
			System.out.println("remove " + spaceRange);
			removeSpots(carPark, spaceRange);
		}

		int newTotalSpaces = carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark, true).size();

		if (newDisabledNumber > newTotalSpaces) {
			disabledRange = newDisabledNumber - newTotalSpaces;
			System.out.println("disableadd " + disabledRange);
			increaseDisabledSpots(carPark, disabledRange);
		} else if (newDisabledNumber < newTotalSpaces) {
			disabledRange = newTotalSpaces - newDisabledNumber;
			System.out.println("disableremove " + disabledRange);
			decreaseDisabledSpots(carPark, disabledRange);
		}
	}

	public void addSpots(CarParks carPark, int spaces) {

		int highestSpaceNumber = carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId());

		highestSpaceNumber++;

		for (int i = 0; i < spaces; i++) {
			CarParkSpots carParkSpot = new CarParkSpots(carPark, false, highestSpaceNumber);
			highestSpaceNumber++;

			carParkSpotRepo.save(carParkSpot);
		}
	}

	public void removeSpots(CarParks carPark, int spaces) {

		int highestSpaceNumber = carParkSpotRepo.findLargestSpaceNumber(carPark.getCarParkId());

		for (int i = 0; i < spaces; i++) {

			CarParkSpots carParkSpot = carParkSpotRepo.findByCarParksAndSpaceNumber(carPark, highestSpaceNumber);
			carParkSpotRepo.deleteByCarParkSpotId(carParkSpot.getCarParkSpotId());
			highestSpaceNumber--;
		}
	}

	public void increaseDisabledSpots(CarParks carPark, int disabledRange) {

		List<CarParkSpots> carParkSpots = carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark,
				false);

		int index = 0;

		for (int i = 0; i < disabledRange; i++) {

			CarParkSpots carParkSpot = carParkSpots.get(index);
			carParkSpot.setIsDisabled(true);
			carParkSpotRepo.save(carParkSpot);

			index++;
		}
	}

	public void decreaseDisabledSpots(CarParks carPark, int disabledRange) {

		List<CarParkSpots> carParkSpots = carParkSpotRepo.findByCarParksAndIsDisabledOrderBySpaceNumberAsc(carPark,
				true);
		int index = carParkSpots.size() - 1;

		for (int i = 0; i < disabledRange; i++) {

			CarParkSpots carParkSpot = carParkSpots.get(index);
			carParkSpot.setIsDisabled(false);
			carParkSpotRepo.save(carParkSpot);

			index--;
		}
	}
}
