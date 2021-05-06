package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.model.CarParkPayments;
import com.ePark.model.CarParks;

@Repository
public interface CarParkPaymentRepository extends JpaRepository<CarParkPayments, Long> {

	public CarParkPayments findByCarParksAndYearMonth(CarParks carPark, String yearMonth);

	List<CarParkPayments> findByCarParksAndPaid(CarParks carPark, boolean paid);
}
