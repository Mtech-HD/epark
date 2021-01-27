package com.ePark.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ePark.entity.Bookings;
import com.ePark.entity.CarParkSpots;
import com.ePark.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepo;
	
	public Bookings save(Bookings booking) {
		return bookingRepo.save(booking);
	}
	
	public Bookings findByBookingId(long bookingId) {
		return bookingRepo.findByBookingId(bookingId);
	}
	
	public List<Bookings> findByCarParkSpotsAndStartDate(CarParkSpots carParkSpotId, LocalDate startDate) {
		return bookingRepo.findByCarParkSpotsAndStartDate(carParkSpotId, startDate);
	}

}
