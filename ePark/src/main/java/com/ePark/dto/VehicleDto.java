package com.ePark.dto;

import com.ePark.model.Users;

public class VehicleDto {
	
	private String registration;
	
	private String make;
	
	private String colour;
	
	private Users user;


	public VehicleDto() {
		super();
	}
	
	public VehicleDto(String registration, String make, String colour, Users user) {
		super();
		this.registration = registration;
		this.make = make;
		this.colour = colour;
		this.user = user;
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

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	

}
