package com.ePark.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "carParks")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carParkId")
public class CarParks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkId;

	private String name;

	private String email;

	private String carParkAddress1;

	private String carParkAddress2;

	private String carParkCity;

	private String carParkPostcode;

	private BigDecimal price;

	private CarParkStatus carParkStatus;

	private Date dateModified;

	private ParkingRate parkingRate;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "carParkOwners", joinColumns = @JoinColumn(name = "carParkId", referencedColumnName = "carParkId"), inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"))
	private Set<Users> users;

	@OneToMany(mappedBy = "carParks", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CarParkSpots> carParkSpots = new HashSet<CarParkSpots>();

	@JsonIgnore
	@OneToMany(mappedBy = "carParks", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CarParkComments> carParkComments;

	@OneToMany(mappedBy = "carParks", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CarParkTimes> carParkTimes;

	@JsonIgnore
	@OneToMany(mappedBy = "carParks", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Bookings> bookings;

	public CarParks() {

	}

	public CarParks(String name, String email, String carParkAddress1, String carParkAddress2, String carParkCity,
			String carParkPostcode, BigDecimal price, CarParkStatus carParkStatus, Date dateModified,
			ParkingRate parkingRate, Set<Users> users) {
		super();
		this.name = name;
		this.email = email;
		this.carParkAddress1 = carParkAddress1;
		this.carParkAddress2 = carParkAddress2;
		this.carParkCity = carParkCity;
		this.carParkPostcode = carParkPostcode;
		this.price = price;
		this.carParkStatus = carParkStatus;
		this.dateModified = dateModified;
		this.parkingRate = parkingRate;
		this.users = users;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Set<CarParkTimes> getCarParkTimes() {
		return carParkTimes;
	}

	public void setCarParkTimes(Set<CarParkTimes> carParkTimes) {
		this.carParkTimes = carParkTimes;
	}

	public Set<CarParkComments> getCarParkComments() {
		return carParkComments;
	}

	public void setCarParkComments(Set<CarParkComments> carParkComments) {
		this.carParkComments = carParkComments;
	}

	public Set<Bookings> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Bookings> bookings) {
		this.bookings = bookings;
	}

	public Set<CarParkSpots> getDisabledSpots() {
		Set<CarParkSpots> disabledSpots = new HashSet<CarParkSpots>();
		for (CarParkSpots carParkSpot : carParkSpots) {
			if (carParkSpot.getIsDisabled() == true) {
				disabledSpots.add(carParkSpot);
			}
		}

		return disabledSpots;
	}

	public List<CarParkTimes> orderTimes() {
		List<CarParkTimes> orderedTimes = new ArrayList<CarParkTimes>();

		List<Week> weekValues = Arrays.asList(Week.values());
		for (Week weekValue : weekValues) {
			for (CarParkTimes carParkTime : carParkTimes) {

				if (carParkTime.getDayOfWeek() == weekValue) {
					orderedTimes.add(carParkTime);
				}
			}
		}
		return orderedTimes;
	}

}
