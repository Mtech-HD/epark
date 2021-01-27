package com.ePark.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkDto;
import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParkStatus;
import com.ePark.entity.CarParkTimes;
import com.ePark.entity.CarParks;
import com.ePark.entity.ParkingRate;
import com.ePark.entity.Users;
import com.ePark.entity.Week;
import com.ePark.repository.CarParkRepository;
import com.ePark.repository.UserRepository;

@Service
public class CarParkService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CarParkRepository carParkRepo;

	public List<CarParks> findAll() {
		return carParkRepo.findAll();
	}

	public void save(CarParkDto carParkDto) {

		ParkingRate rate = ParkingRate.HOUR;

		if (carParkDto.getRate() == new BigDecimal(0.5)) {
			rate = ParkingRate.HALFHOUR;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Set<Users> users = new HashSet<Users>();
		Users user = userRepo.findByUsername(auth.getName());
		users.add(user);

		CarParks carPark = new CarParks(carParkDto.getName(), carParkDto.getEmail(), carParkDto.getCarParkAddress1(),
				carParkDto.getCarParkAddress2(), carParkDto.getCarParkCity(), carParkDto.getCarParkPostcode(),
				carParkDto.getPrice(), carParkDto.getCarParkStatus(), new Date(), rate, users);

		List<Week> weekValues = Arrays.asList(Week.values());
		Set<CarParkTimes> carParkTimes = new HashSet<CarParkTimes>();

		for (Week weekValue : weekValues) {
			CarParkTimes carParkTime = new CarParkTimes();
			carParkTime.setDayOfWeek(weekValue);

			String day = weekValue.toString().substring(0, 1).toUpperCase()
					+ weekValue.toString().substring(1).toLowerCase();
			String getFrom = "get" + day + "From";
			String getTo = "get" + day + "To";
			Method getFromMethod;
			Method getToMethod;
			try {
				getFromMethod = carParkDto.getClass().getMethod(getFrom);
				getToMethod = CarParkDto.class.getMethod(getTo);
				carParkTime.setOpenTime((LocalTime) getFromMethod.invoke(carParkDto));
				carParkTime.setCloseTime((LocalTime) getToMethod.invoke(carParkDto));
			} catch (Exception e) {
				e.printStackTrace();
			}

			carParkTime.setCarParks(carPark);
			carParkTimes.add(carParkTime);
		}

		carPark.setCarParkTimes(carParkTimes);

		for (int i = 1; i <= carParkDto.getSpaces(); i++) {
			int disabled = carParkDto.getIsDisabled();
			if (disabled > 0) {
				CarParkSpots carParkSpot = new CarParkSpots(carPark, true, i);
				carPark.getCarParkSpots().add(carParkSpot);
				disabled--;
			} else {
				CarParkSpots carParkSpot = new CarParkSpots(carPark, false, i);
				carPark.getCarParkSpots().add(carParkSpot);
			}
		}

		carParkRepo.save(carPark);
	}

	public CarParks save(CarParks carPark) {

		return carParkRepo.save(carPark);
	}

	public CarParks findByCarParkId(long carParkId) {

		return carParkRepo.findByCarParkId(carParkId);
	}

	public List<CarParks> findByCarParkStatus(CarParkStatus status) {

		return carParkRepo.findByCarParkStatus(status);
	}

	public List<CarParks> findByUsers(Users user) {

		return carParkRepo.findByUsers(user);
	}

}
