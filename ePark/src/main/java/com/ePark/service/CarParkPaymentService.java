package com.ePark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.CarParkPayments;
import com.ePark.repository.CarParkPaymentRepository;

@Service
public class CarParkPaymentService {

	@Autowired
	private CarParkPaymentRepository carParkPaymentRepo;
	
	
	public CarParkPayments save(CarParkPayments carParkPayment) {
		return carParkPaymentRepo.save(carParkPayment);
	}
	
	public List<CarParkPayments> findByCarParksAndyearMonth(long carParkId, String yearMonth, boolean paid) {
		return carParkPaymentRepo.findByCarParksAndyearMonth(carParkId, yearMonth, paid);
	}
	
	public List<CarParkPayments> findByCarParksAndPaid(long carParkId, boolean paid) {
		return carParkPaymentRepo.findByCarParksAndPaid(carParkId, paid);
	}
	
	public void saveIfNotExists(CarParkPayments carParkPayment) {
		
		CarParkPayments payment = carParkPaymentRepo.findByCarParksAndYearMonth(carParkPayment.getCarParks(), carParkPayment.getYearMonth());
		
		if (payment != null) {
			carParkPaymentRepo.save(payment);
		}
	}
}
