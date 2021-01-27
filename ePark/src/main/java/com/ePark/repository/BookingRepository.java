package com.ePark.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ePark.entity.Bookings;
import com.ePark.entity.CarParkSpots;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {

	Bookings findByBookingId(long bookingId);
	
	List<Bookings> findByCarParkSpotsAndStartDate(CarParkSpots carParkSpotId, LocalDate startDate);
}
