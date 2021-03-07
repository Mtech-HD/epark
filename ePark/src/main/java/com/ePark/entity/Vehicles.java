package com.ePark.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "vehicles")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "vehicleId")
public class Vehicles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long vehicleId;

	@Column(unique = true)
	private String registration;

	private String make;

	private String colour;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "userId", nullable = false)
	private Users users;

	private boolean isDefault;

	@OneToOne(mappedBy = "vehicles")
	private Bookings bookings;

	public Vehicles() {
		super();
	}

	public Vehicles(String registration, String make, String colour, Users users, boolean isDefault) {
		super();
		this.registration = registration;
		this.make = make;
		this.colour = colour;
		this.users = users;
		this.isDefault = isDefault;
	}

	public long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Bookings getBookings() {
		return bookings;
	}

	public void setBookings(Bookings bookings) {
		this.bookings = bookings;
	}

}