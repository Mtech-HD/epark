package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.model.CarParkPayments;
import com.ePark.model.CarParks;
import com.ePark.repository.CarParkPaymentRepository;

@Service
public class CarParkPaymentService {

	@Autowired
	private CarParkPaymentRepository carParkPaymentRepo;

	public CarParkPayments save(CarParkPayments carParkPayment) {
		return carParkPaymentRepo.save(carParkPayment);
	}

	public List<CarParkPayments> findByCarParksAndPaid(CarParks carPark, boolean paid) {
		return carParkPaymentRepo.findByCarParksAndPaid(carPark, paid);
	}

	public void saveIfNotExists(CarParkPayments carParkPayment) {

		CarParkPayments payment = carParkPaymentRepo.findByCarParksAndYearMonth(carParkPayment.getCarParks(),
				carParkPayment.getYearMonth());

		if (payment == null) {
			carParkPaymentRepo.save(carParkPayment);
		}
	}
}
