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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "carParkSpots")
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

	public CarParkSpots() {

	}

	public CarParkSpots(CarParks carParks, boolean isDisabled) {
		this.isDisabled = isDisabled;
		this.carParks = carParks;
	}
	
	public CarParkSpots(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public CarParkSpots(long carParkSpotId, boolean isDisabled, CarParks carParks) {
		super();
		this.carParkSpotId = carParkSpotId;
		this.isDisabled = isDisabled;
		this.carParks = carParks;
	}

	public long getCarParkSpotId() {
		return carParkSpotId;
	}

	public void setCarParkSpotId(long carParkSpotId) {
		this.carParkSpotId = carParkSpotId;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

}
