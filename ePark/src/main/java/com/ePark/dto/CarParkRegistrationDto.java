package com.ePark.dto;

import java.math.BigDecimal;
import java.util.Set;

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkStatus;
import com.ePark.model.Users;

public class CarParkRegistrationDto {

	private String name;

	private String carParkAddress1;

	private String carParkAddress2;

	private String carParkCity;

	private String carParkPostcode;

	private int spaces;

	private int isDisabled;

	private BigDecimal price;

	private BigDecimal rate;

	private CarParkStatus carParkStatus;

	private Set<Users> users;

	private Set<CarParkSpots> carParkSpots;

	public CarParkRegistrationDto() {
	}

	public CarParkRegistrationDto(String name, String carParkAddress1, String carParkAddress2, String carParkCity,
			String carParkPostcode, int spaces, int isDisabled, BigDecimal price, BigDecimal rate,
			CarParkStatus carParkStatus, Set<Users> users, Set<CarParkSpots> carParkSpots) {
		super();
		this.name = name;
		this.carParkAddress1 = carParkAddress1;
		this.carParkAddress2 = carParkAddress2;
		this.carParkCity = carParkCity;
		this.carParkPostcode = carParkPostcode;
		this.spaces = spaces;
		this.isDisabled = isDisabled;
		this.price = price;
		this.rate = rate;
		this.carParkStatus = carParkStatus;
		this.users = users;
		this.carParkSpots = carParkSpots;
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

	public int getSpaces() {
		return spaces;
	}

	public void setSpaces(int spaces) {
		this.spaces = spaces;
	}

	public int getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(int isDisabled) {
		this.isDisabled = isDisabled;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public CarParkStatus getCarParkStatus() {
		return carParkStatus;
	}

	public void setCarParkStatus(CarParkStatus carParkStatus) {
		this.carParkStatus = carParkStatus;
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

}
