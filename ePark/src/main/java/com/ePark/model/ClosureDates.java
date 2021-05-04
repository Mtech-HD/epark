package com.ePark.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "closureDates")
public class ClosureDates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long closureDateId;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	public ClosureDates() {
		super();
	}

	public ClosureDates(LocalDate date, CarParks carParks) {
		this.date = date;
		this.carParks = carParks;
	}
	
	public long getClosureDateId() {
		return closureDateId;
	}

	public void setClosureDateId(long closureDateId) {
		this.closureDateId = closureDateId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

}
