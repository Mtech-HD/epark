package com.ePark.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.ePark.model.CarParkSpots;
import com.ePark.model.CarParkStatus;
import com.ePark.model.Users;

public class CarParkDto {

	private String name;
	
	private String email;

	private String carParkAddress1;

	private String carParkAddress2;

	private String carParkCity;

	private String carParkPostcode;

	private int spaces;

	private int isDisabled;

	private BigDecimal price;

	private BigDecimal rate;

	private CarParkStatus carParkStatus;

	private String comment;

	private Set<Users> users;

	private Set<CarParkSpots> carParkSpots;

	private String mondayFrom;
	private String mondayTo;
	private String tuesdayFrom;
	private String tuesdayTo;
	private String wednesdayFrom;
	private String wednesdayTo;
	private String thursdayFrom;
	private String thursdayTo;
	private String fridayFrom;
	private String fridayTo;
	private String saturdayFrom;
	private String saturdayTo;
	private String sundayFrom;
	private String sundayTo;

	public CarParkDto() {
	}

	public CarParkDto(String name, String email, String carParkAddress1, String carParkAddress2, String carParkCity,
			String carParkPostcode, int spaces, int isDisabled, BigDecimal price, BigDecimal rate,
			CarParkStatus carParkStatus, Set<Users> users, Set<CarParkSpots> carParkSpots, String comment, String mondayFrom,
			String mondayTo, String tuesdayFrom, String tuesdayTo, String wednesdayFrom, String wednesdayTo,
			String thursdayFrom, String thursdayTo, String fridayFrom, String fridayTo, String saturdayFrom,
			String saturdayTo, String sundayFrom, String sundayTo) {
		super();
		this.name = name;
		this.email = email;
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
		this.comment = comment;
		this.mondayFrom = mondayFrom;
		this.mondayTo = mondayTo;
		this.tuesdayFrom = tuesdayFrom;
		this.tuesdayTo = tuesdayTo;
		this.wednesdayFrom = wednesdayFrom;
		this.wednesdayTo = wednesdayTo;
		this.thursdayFrom = thursdayFrom;
		this.thursdayTo = thursdayTo;
		this.fridayFrom = fridayFrom;
		this.fridayTo = fridayTo;
		this.saturdayFrom = saturdayFrom;
		this.saturdayTo = saturdayTo;
		this.sundayFrom = sundayFrom;
		this.sundayTo = sundayTo;

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMondayFrom() {
		return mondayFrom;
	}

	public void setMondayFrom(String mondayFrom) {
		this.mondayFrom = mondayFrom;
	}

	public String getMondayTo() {
		return mondayTo;
	}

	public void setMondayTo(String mondayTo) {
		this.mondayTo = mondayTo;
	}

	public String getTuesdayFrom() {
		return tuesdayFrom;
	}

	public void setTuesdayFrom(String tuesdayFrom) {
		this.tuesdayFrom = tuesdayFrom;
	}

	public String getTuesdayTo() {
		return tuesdayTo;
	}

	public void setTuesdayTo(String tuesdayTo) {
		this.tuesdayTo = tuesdayTo;
	}

	public String getWednesdayFrom() {
		return wednesdayFrom;
	}

	public void setWednesdayFrom(String wednesdayFrom) {
		this.wednesdayFrom = wednesdayFrom;
	}

	public String getWednesdayTo() {
		return wednesdayTo;
	}

	public void setWednesdayTo(String wednesdayTo) {
		this.wednesdayTo = wednesdayTo;
	}

	public String getThursdayFrom() {
		return thursdayFrom;
	}

	public void setThursdayFrom(String thursdayFrom) {
		this.thursdayFrom = thursdayFrom;
	}

	public String getThursdayTo() {
		return thursdayTo;
	}

	public void setThursdayTo(String thursdayTo) {
		this.thursdayTo = thursdayTo;
	}

	public String getFridayFrom() {
		return fridayFrom;
	}

	public void setFridayFrom(String fridayFrom) {
		this.fridayFrom = fridayFrom;
	}

	public String getFridayTo() {
		return fridayTo;
	}

	public void setFridayTo(String fridayTo) {
		this.fridayTo = fridayTo;
	}

	public String getSaturdayFrom() {
		return saturdayFrom;
	}

	public void setSaturdayFrom(String saturdayFrom) {
		this.saturdayFrom = saturdayFrom;
	}

	public String getSaturdayTo() {
		return saturdayTo;
	}

	public void setSaturdayTo(String saturdayTo) {
		this.saturdayTo = saturdayTo;
	}

	public String getSundayFrom() {
		return sundayFrom;
	}

	public void setSundayFrom(String sundayFrom) {
		this.sundayFrom = sundayFrom;
	}

	public String getSundayTo() {
		return sundayTo;
	}

	public void setSundayTo(String sundayTo) {
		this.sundayTo = sundayTo;
	}

}
