package com.ePark.model;

import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "carParkTimes")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carParkTimeId")
public class CarParkTimes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkTimeId;

	@Enumerated(EnumType.STRING)
	private Week dayOfWeek;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime openTime;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime closeTime;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	public CarParkTimes() {

	}

	public CarParkTimes(Week dayOfWeek, LocalTime openTime, LocalTime closeTime, CarParks carParks) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.carParks = carParks;
	}

	public long getCarParkTimeId() {
		return carParkTimeId;
	}

	public void setCarParkTimeId(long carParkTimeId) {
		this.carParkTimeId = carParkTimeId;
	}

	public Week getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Week dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

}
