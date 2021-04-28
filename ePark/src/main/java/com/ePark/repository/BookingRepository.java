package com.ePark.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ePark.entity.Bookings;
import com.ePark.entity.CarParkSpots;
import com.ePark.entity.CarParks;
import com.ePark.entity.EarningsAndBookings;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
	
	Bookings findByBookingId(long bookingId);
	
	List<Bookings> findByCarParkSpotsAndStartDate(CarParkSpots carParkSpotId, LocalDate startDate);
	
	List<Bookings> findByCarParksAndStartDate(CarParks carPark, LocalDate startDate);

	@Modifying
	@Query(value = "delete from bookings b where b.bookingId = :bookingId", nativeQuery = true)
	void deleteBooking(@Param("bookingId") long bookingId);
	
	@Query(value = "SELECT IFNULL(sum(amount), 0) as revenueThisWeek FROM bookings WHERE carParkId = :carParkId AND bookingStatus = :bookingStatus AND bookingCreated BETWEEN :start and :end", nativeQuery = true)
	public BigDecimal revenueBetween(long carParkId, String bookingStatus, LocalDate start, LocalDate end);
	
	@Query(value = "SELECT SUM(amount) as earnings, COUNT(*) as bookings FROM bookings WHERE carParkId = :carParkId AND bookingStatus = :bookingStatus AND EXTRACT(YEAR_MONTH FROM startDate) = :yearMonth", nativeQuery = true)
	public EarningsAndBookings getBookingsForMonth(long carParkId, String yearMonth, String bookingStatus);
};
