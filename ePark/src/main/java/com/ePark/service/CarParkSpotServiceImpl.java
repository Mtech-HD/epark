package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkRegistrationDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.repository.CarParkSpotRepository;

@Service
public class CarParkSpotServiceImpl implements CarParkSpotService {

	@Autowired
	private CarParkSpotRepository carParkSpotRepo;
	
	@Override
	public CarParkSpots save(CarParkRegistrationDto carParkRegistrationDto, CarParks carPark) {
		CarParkSpots carParkSpot;
		
		for (int i = 0; i < carParkRegistrationDto.getSpaces(); i++) {
			int disabled = carParkRegistrationDto.getIsDisabled();
			if (disabled > 0) {
				carParkSpot = new CarParkSpots(carPark, true);
				carParkSpotRepo.save(carParkSpot);
				disabled--;
			} else {
				carParkSpot = new CarParkSpots(carPark, false);
				carParkSpotRepo.save(carParkSpot);
				
			}
		}
		return null;

	}
	
	@Override
	public List<CarParkSpots> findByCarParks(CarParks carPark) {
		
		return carParkSpotRepo.findByCarParks(carPark);
	}

}
