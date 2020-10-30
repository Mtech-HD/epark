package com.ePark.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkRegistrationDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParks;
import com.ePark.model.ParkingRate;
import com.ePark.model.Users;
import com.ePark.repository.CarParkRepository;
import com.ePark.repository.CarParkSpotRepository;
import com.ePark.repository.UserRepository;

@Service
public class CarParkServiceImpl implements CarParkService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CarParkRepository carParkRepo;

	@Autowired
	private CarParkSpotRepository carParkSpotRepo;

	
	@Override
	public List<CarParks> findAll() {
		return carParkRepo.findAll();
	}
	
	
	@Override
	public void save(CarParkRegistrationDto carParkRegistrationDto) {

		ParkingRate rate = ParkingRate.HOUR;

		if (carParkRegistrationDto.getRate() == new BigDecimal(0.5)) {
			rate = ParkingRate.HALFHOUR;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Set<Users> users = new HashSet<Users>();
		Users user = userRepo.findByUsername(auth.getName());
		users.add(user);

		CarParks carPark = new CarParks(carParkRegistrationDto.getName(), carParkRegistrationDto.getCarParkAddress1(),
				carParkRegistrationDto.getCarParkAddress2(), carParkRegistrationDto.getCarParkCity(),
				carParkRegistrationDto.getCarParkPostcode(), carParkRegistrationDto.getPrice(),
				carParkRegistrationDto.getCarParkStatus(), new Date(), rate, users);

		carParkRepo.save(carPark);

		for (int i = 0; i < carParkRegistrationDto.getSpaces(); i++) {
			int disabled = carParkRegistrationDto.getIsDisabled();
			if (disabled > 0) {
				CarParkSpots carParkSpot = new CarParkSpots(carPark, true);
				carParkSpotRepo.save(carParkSpot);
				disabled--;
			} else {
				CarParkSpots carParkSpot = new CarParkSpots(carPark, false);
				carParkSpotRepo.save(carParkSpot);
			}
		}

	}
	
	@Override
	public CarParks findByCarParkId(long carParkId) {
		
		return carParkRepo.findByCarParkId(carParkId);
	}
	
	@Override
	public List<CarParks> findByCarParkStatus(CarParkStatus status) {
		
		return carParkRepo.findByCarParkStatus(status);
	}

}
