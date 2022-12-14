package com.ePark.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.ePark.model.CarParks.ParkingRate;

@Entity
@DynamicUpdate
@Table(name = "bookings")
public class Bookings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookingId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userId", nullable = false)
	private Users users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicleId", referencedColumnName = "vehicleId")
	private Vehicles vehicles;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carParkSpotId", referencedColumnName = "carParkSpotId")
	private CarParkSpots carParkSpots;

	private String paymentId;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate endDate;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;

	private boolean isDisabled;

	private BigDecimal unitPrice;

	private BigDecimal amount;

	private Date bookingCreated;

	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;

	public Bookings() {
		super();
	}
	
	public Bookings(long bookingId) {
		this.bookingId = bookingId;
	}

	public Bookings(Users users, Vehicles vehicles, CarParks carParks, CarParkSpots carParkSpots, String paymentId,
			LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, BookingStatus bookingStatus) {
		super();
		this.users = users;
		this.vehicles = vehicles;
		this.carParks = carParks;
		this.carParkSpots = carParkSpots;
		this.paymentId = paymentId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.bookingStatus = bookingStatus;
	}

	public enum BookingStatus {
		ACTIVE, CANCELLED, INPROGRESS
	}

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Vehicles getVehicles() {
		return vehicles;
	}

	public void setVehicles(Vehicles vehicles) {
		this.vehicles = vehicles;
	}

	public CarParkSpots getCarParkSpots() {
		return carParkSpots;
	}

	public void setCarParkSpots(CarParkSpots carParkSpots) {
		this.carParkSpots = carParkSpots;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public LocalDate getStart() {
		return startDate;
	}

	public void setStart(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEnd() {
		return endDate;
	}

	public void setEnd(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

	public boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getBookingCreated() {
		return bookingCreated;
	}

	public void setBookingCreated(Date bookingCreated) {
		this.bookingCreated = bookingCreated;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getFullAddress() {
		return carParks.getCarParkAddress1() + "," + carParks.getCarParkAddress2() + "," + carParks.getCarParkCity()
				+ "," + carParks.getCarParkPostcode();
	}

	public long calculateDuration() {
		long unitsCount;
		long duration;
		int hourUnit = 60;
		int halfHourUnit = 30;

		if (startTime == null || endTime == null) {
			return 0;
		} else {
			duration = Duration.between(startTime, endTime).toMinutes();
		}

		if (carParks.getParkingRate() == ParkingRate.HOUR) {
			unitsCount = Math.floorDiv(duration, hourUnit);
		} else {
			unitsCount = Math.floorDiv(duration, halfHourUnit);
		}

		return unitsCount;
	}

	public BigDecimal calculatePrice() {

		return unitPrice.multiply(BigDecimal.valueOf(calculateDuration()));
	}

}
