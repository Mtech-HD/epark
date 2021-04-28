package com.ePark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ePark.entity.CarParkPayments;
import com.ePark.entity.CarParks;

@Repository
public interface CarParkPaymentRepository extends JpaRepository<CarParkPayments, Long> {

	@Query(value = "SELECT * FROM carParkPayments WHERE carParkId = :carParkId AND yearMonth = :yearMonth AND paid = :paid ORDER BY CarParkPaymentsId ASC", nativeQuery = true)
	public List<CarParkPayments> findByCarParksAndyearMonth(long carParkId, String yearMonth, boolean paid);
	
	public CarParkPayments findByCarParksAndYearMonth(CarParks carPark, String yearMonth);
	
	List<CarParkPayments> findByCarParksAndPaid(long carParkId, boolean paid);
}
