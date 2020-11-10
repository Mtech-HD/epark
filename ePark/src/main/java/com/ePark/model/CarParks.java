package com.ePark.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

@Entity
@Table(name = "carParks")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carParkId")
public class CarParks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkId;

	private String name;

	private String carParkAddress1;

	private String carParkAddress2;

	private String carParkCity;

	private String carParkPostcode;

	private BigDecimal price;

	private CarParkStatus carParkStatus;

	private Date dateModified;

	private ParkingRate parkingRate;

	@NotNull
	private String carParkComment;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "carParkOwners", joinColumns = @JoinColumn(name = "carParkId", referencedColumnName = "carParkId"), inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"))
	private Set<Users> users;

	@OneToMany(mappedBy = "carParks", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CarParkSpots> carParkSpots = new HashSet<CarParkSpots>();

	@OneToMany(mappedBy = "carParks", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CarParkTimes> carParkTimes;

	public CarParks() {

	}

	public CarParks(String name, String carParkAddress1, String carParkAddress2, String carParkCity,
			String carParkPostcode, BigDecimal price, CarParkStatus carParkStatus, Date dateModified,
			ParkingRate parkingRate, Set<Users> users, String carParkComment) {
		super();
		this.name = name;
		this.carParkAddress1 = carParkAddress1;
		this.carParkAddress2 = carParkAddress2;
		this.carParkCity = carParkCity;
		this.carParkPostcode = carParkPostcode;
		this.price = price;
		this.carParkStatus = carParkStatus;
		this.dateModified = dateModified;
		this.parkingRate = parkingRate;
		this.users = users;
		this.carParkComment = carParkComment;
	}

	public long getCarParkId() {
		return carParkId;
	}

	public void setCarParkId(long carParkId) {
		this.carParkId = carParkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarParkAddress1() {
		return carParkAddress1;
	}

	public void setCarParkAddress1(String carParkAddress1) {
		this.carParkAddress1 = carParkAddress1;
	}

	public String getCarParkAddress2() {
		return carParkAddress2;
	}

	public void setCarParkAddress2(String carParkAddress2) {
		this.carParkAddress2 = carParkAddress2;
	}

	public String getCarParkCity() {
		return carParkCity;
	}

	public void setCarParkCity(String carParkCity) {
		this.carParkCity = carParkCity;
	}

	public String getCarParkPostcode() {
		return carParkPostcode;
	}

	public void setCarParkPostcode(String carParkPostcode) {
		this.carParkPostcode = carParkPostcode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public CarParkStatus getCarParkStatus() {
		return carParkStatus;
	}

	public void setCarParkStatus(CarParkStatus carParkStatus) {
		this.carParkStatus = carParkStatus;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public ParkingRate getParkingRate() {
		return parkingRate;
	}

	public void setParkingRate(ParkingRate parkingRate) {
		this.parkingRate = parkingRate;
	}

	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	public Set<CarParkSpots> getCarParkSpots() {
		return carParkSpots;
	}

	public void setCarParkSpots(Set<CarParkSpots> carParkSpots) {
		this.carParkSpots = carParkSpots;
	}

	public String getCarParkComment() {
		return carParkComment;
	}

	public void setCarParkComment(String carParkComment) {
		this.carParkComment = carParkComment;
	}

	public Set<CarParkTimes> getCarParkTimes() {
		return carParkTimes;
	}

	public void setCarParkTimes(Set<CarParkTimes> carParkTimes) {
		this.carParkTimes = carParkTimes;
	}

}
