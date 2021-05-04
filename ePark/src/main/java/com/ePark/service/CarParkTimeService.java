package com.ePark.service;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.dto.CarParkDto;
import com.ePark.model.CarParkTimes;
import com.ePark.model.CarParks;
import com.ePark.repository.CarParkTimeRepository;

@Service
public class CarParkTimeService {

	@Autowired
	private CarParkTimeRepository carParkTimeRepo;

	public List<CarParkTimes> findByCarParks(CarParks carPark) {

		return carParkTimeRepo.findByCarParks(carPark);
	}

	public List<CarParkTimes> getCarParkTimes(long carParkId) {

		return carParkTimeRepo.getCarParkTimes(carParkId);
	}

	public List<CarParkTimes> updateTimes(CarParks carParks, CarParkDto carParkDto) {

		List<CarParkTimes> existingTimes = findByCarParks(carParks);

		for (CarParkTimes carParkTime : existingTimes) {

			String day = carParkTime.getDayOfWeek().toString().substring(0, 1).toUpperCase()
					+ carParkTime.getDayOfWeek().toString().substring(1).toLowerCase();

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

			carParkTimeRepo.save(carParkTime);
		}

		return findByCarParks(carParks);
	}

}
