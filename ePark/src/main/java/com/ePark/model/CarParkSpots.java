package com.ePark.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "carParkSpots")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carParkSpotId")
public class CarParkSpots {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkSpotId;

	@Column(name = "isDisabled")
	private boolean isDisabled;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	private int spaceNumber;

	@JsonIgnore
	@OneToOne(mappedBy = "carParkSpots")
	private Bookings bookings;

	public CarParkSpots() {

	}
	
	public CarParkSpots(long carParkSpotId) {
		this.carParkSpotId = carParkSpotId;
	}

	public CarParkSpots(CarParks carParks, boolean isDisabled, int spaceNumber) {
		super();
		this.isDisabled = isDisabled;
		this.carParks = carParks;
		this.spaceNumber = spaceNumber;
	}

	public long getCarParkSpotId() {
		return carParkSpotId;
	}

	public void setCarParkSpotId(long carParkSpotId) {
		this.carParkSpotId = carParkSpotId;
	}

	public boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

	public int getSpaceNumber() {
		return spaceNumber;
	}

	public void setSpaceNumber(int spaceNumber) {
		this.spaceNumber = spaceNumber;
	}

	public Bookings getBookings() {
		return bookings;
	}

	public void setBookings(Bookings bookings) {
		this.bookings = bookings;
	}

	public boolean isReserved() {
		return bookings != null;
	}

}
