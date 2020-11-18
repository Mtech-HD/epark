package com.ePark.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "carParkTimes")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carParkTimeId")
public class CarParkTimes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkTimeId;

	private Week dayOfWeek;
	private String openTime;
	private String closeTime;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	public CarParkTimes() {

	}

	public CarParkTimes(Week dayOfWeek, String openTime, String closeTime, CarParks carParks) {
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

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}




}
