package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParks;
import com.ePark.repository.CarParkSpotRepository;

@Service
public class CarParkSpotServiceImpl implements CarParkSpotService {

	@Autowired
	private CarParkSpotRepository carParkSpotRepo;
	

	
	@Override
	public List<CarParkSpots> findByCarParks(CarParks carPark) {
		
		return carParkSpotRepo.findByCarParks(carPark);
	}

}
