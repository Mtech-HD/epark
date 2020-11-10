package com.ePark.service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkStatus;
import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;
import com.ePark.model.ParkingRate;
import com.ePark.model.Users;
import com.ePark.model.Week;
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
	public void save(CarParkDto carParkDto) {

		ParkingRate rate = ParkingRate.HOUR;

		if (carParkDto.getRate() == new BigDecimal(0.5)) {
			rate = ParkingRate.HALFHOUR;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Set<Users> users = new HashSet<Users>();
		Users user = userRepo.findByUsername(auth.getName());
		users.add(user);

		CarParks carPark = new CarParks(carParkDto.getName(), carParkDto.getCarParkAddress1(),
				carParkDto.getCarParkAddress2(), carParkDto.getCarParkCity(), carParkDto.getCarParkPostcode(),
				carParkDto.getPrice(), carParkDto.getCarParkStatus(), new Date(), rate, users, "");

		Set<CarParkTimes> carParkTimes = new HashSet<CarParkTimes>();

			CarParkTimes mondayTime = new CarParkTimes();
			mondayTime.setDayOfWeek(Week.MONDAY);
			mondayTime.setOpenTime(carParkDto.getMondayFrom());
			mondayTime.setCloseTime(carParkDto.getMondayTo());
			mondayTime.setCarParks(carPark);
			carParkTimes.add(mondayTime);
			
			CarParkTimes tuesdayTime = new CarParkTimes();
			tuesdayTime.setDayOfWeek(Week.TUESDAY);
			tuesdayTime.setOpenTime(carParkDto.getTuesdayFrom());
			tuesdayTime.setCloseTime(carParkDto.getTuesdayTo());
			tuesdayTime.setCarParks(carPark);
			carParkTimes.add(tuesdayTime);
			
			CarParkTimes wednesdayTime = new CarParkTimes();
			wednesdayTime.setDayOfWeek(Week.WEDNESDAY);
			wednesdayTime.setOpenTime(carParkDto.getWednesdayFrom());
			wednesdayTime.setCloseTime(carParkDto.getWednesdayTo());
			wednesdayTime.setCarParks(carPark);
			carParkTimes.add(wednesdayTime);

			CarParkTimes thursdayTime = new CarParkTimes();
			thursdayTime.setDayOfWeek(Week.THURSDAY);
			thursdayTime.setOpenTime(carParkDto.getThursdayFrom());
			thursdayTime.setCloseTime(carParkDto.getThursdayTo());
			thursdayTime.setCarParks(carPark);
			carParkTimes.add(thursdayTime);

			CarParkTimes fridayTime = new CarParkTimes();
			fridayTime.setDayOfWeek(Week.FRIDAY);
			fridayTime.setOpenTime(carParkDto.getFridayFrom());
			fridayTime.setCloseTime(carParkDto.getFridayTo());
			fridayTime.setCarParks(carPark);
			carParkTimes.add(fridayTime);

			CarParkTimes saturdayTime = new CarParkTimes();
			saturdayTime.setDayOfWeek(Week.SATURDAY);
			saturdayTime.setOpenTime(carParkDto.getSaturdayFrom());
			saturdayTime.setCloseTime(carParkDto.getSaturdayTo());
			saturdayTime.setCarParks(carPark);
			carParkTimes.add(saturdayTime);

			CarParkTimes sundayTime = new CarParkTimes();
			sundayTime.setDayOfWeek(Week.SUNDAY);
			sundayTime.setOpenTime(carParkDto.getSundayFrom());
			sundayTime.setCloseTime(carParkDto.getSundayTo());
			sundayTime.setCarParks(carPark);
			carParkTimes.add(sundayTime);
		

		carPark.setCarParkTimes(carParkTimes);

		for (int i = 0; i < carParkDto.getSpaces(); i++) {
			int disabled = carParkDto.getIsDisabled();
			if (disabled > 0) {
				CarParkSpots carParkSpot = new CarParkSpots(carPark, true);
				carPark.getCarParkSpots().add(carParkSpot);
				disabled--;
			} else {
				CarParkSpots carParkSpot = new CarParkSpots(carPark, false);
				carPark.getCarParkSpots().add(carParkSpot);
			}
		}

		carParkRepo.save(carPark);
	}

	@Override
	public CarParks save(CarParks carPark) {

		return carParkRepo.save(carPark);
	}

	@Override
	public CarParks findByCarParkId(long carParkId) {

		return carParkRepo.findByCarParkId(carParkId);
	}

	@Override
	public List<CarParks> findByCarParkStatus(CarParkStatus status) {

		return carParkRepo.findByCarParkStatus(status);
	}

	@Override
	public List<CarParks> findByUsers(Users user) {

		return carParkRepo.findByUsers(user);
	}

}
