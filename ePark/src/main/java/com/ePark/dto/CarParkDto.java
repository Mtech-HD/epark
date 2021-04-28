package com.ePark.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.ePark.entity.CarParks;
import com.ePark.entity.CarParks.AccessControlTypes;
import com.ePark.entity.CarParks.CarParkStatus;
import com.ePark.entity.CarParks.ParkingRate;
import com.ePark.entity.Users;

public class CarParkDto {

	private long carParkId;

	private String name;

	private String serialNumber;

	private String email;

	private String carParkAddress1;

	private String carParkAddress2;

	private String carParkCity;

	private String carParkPostcode;

	private int spaces;

	private int isDisabled;

	private BigDecimal price;

	private ParkingRate rate;

	private int enableFutureWeeks;

	private boolean dynamicPricing;

	private BigDecimal targetRevenue;

	private String description;

	private Integer heightRestriction;

	private AccessControlTypes accessControl;

	private CarParkStatus carParkStatus;

	private String comment;

	private String stripeId;

	private String stripeUrl;

	private Set<Users> users;

	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime mondayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime mondayTo;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime tuesdayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime tuesdayTo;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime wednesdayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime wednesdayTo;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime thursdayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime thursdayTo;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime fridayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime fridayTo;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime saturdayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime saturdayTo;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime sundayFrom;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime sundayTo;

	public CarParkDto() {
	}

	public CarParkDto(String name, String serialNumber, String email, String carParkAddress1, String carParkAddress2,
			String carParkCity, String carParkPostcode, int spaces, int isDisabled, BigDecimal price, ParkingRate rate,
			CarParkStatus carParkStatus, Set<Users> users, String comment, int enableFutureWeeks,
			boolean dynamicPricing, BigDecimal targetRevenue, LocalTime mondayFrom, LocalTime mondayTo,
			LocalTime tuesdayFrom, LocalTime tuesdayTo, LocalTime wednesdayFrom, LocalTime wednesdayTo,
			LocalTime thursdayFrom, LocalTime thursdayTo, LocalTime fridayFrom, LocalTime fridayTo,
			LocalTime saturdayFrom, LocalTime saturdayTo, LocalTime sundayFrom, LocalTime sundayTo) {
		super();
		this.name = name;
		this.serialNumber = serialNumber;
		this.email = email;
		this.carParkAddress1 = carParkAddress1;
		this.carParkAddress2 = carParkAddress2;
		this.carParkCity = carParkCity;
		this.carParkPostcode = carParkPostcode;
		this.spaces = spaces;
		this.isDisabled = isDisabled;
		this.price = price;
		this.rate = rate;
		this.enableFutureWeeks = enableFutureWeeks;
		this.dynamicPricing = dynamicPricing;
		this.targetRevenue = targetRevenue;
		this.carParkStatus = carParkStatus;
		this.users = users;
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

	public CarParkDto(CarParks carPark) {
		this.carParkId = carPark.getCarParkId();
		this.name = carPark.getName();
		this.email = carPark.getEmail();
		this.carParkAddress1 = carPark.getCarParkAddress1();
		this.carParkAddress2 = carPark.getCarParkAddress2();
		this.carParkCity = carPark.getCarParkCity();
		this.carParkPostcode = carPark.getCarParkPostcode();
		this.spaces = carPark.getCarParkSpots().size();
		this.isDisabled = carPark.getDisabledSpots().size();
		this.price = carPark.getPrice();
		this.rate = carPark.getParkingRate();
		this.carParkStatus = carPark.getCarParkStatus();
		this.description = carPark.getDescription();
		this.heightRestriction = carPark.getHeightRestriction();
		this.accessControl = carPark.getAccessControl();

		this.mondayFrom = carPark.orderTimes().get(0).getOpenTime();
		this.mondayTo = carPark.orderTimes().get(0).getCloseTime();
		this.tuesdayFrom = carPark.orderTimes().get(1).getOpenTime();
		this.tuesdayTo = carPark.orderTimes().get(1).getCloseTime();
		this.wednesdayFrom = carPark.orderTimes().get(2).getOpenTime();
		this.wednesdayTo = carPark.orderTimes().get(2).getCloseTime();
		this.thursdayFrom = carPark.orderTimes().get(3).getOpenTime();
		this.thursdayTo = carPark.orderTimes().get(3).getCloseTime();
		this.fridayFrom = carPark.orderTimes().get(4).getOpenTime();
		this.fridayTo = carPark.orderTimes().get(4).getCloseTime();
		this.saturdayFrom = carPark.orderTimes().get(5).getOpenTime();
		this.saturdayTo = carPark.orderTimes().get(5).getCloseTime();
		this.sundayFrom = carPark.orderTimes().get(6).getOpenTime();
		this.sundayTo = carPark.orderTimes().get(6).getCloseTime();
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

	public ParkingRate getRate() {
		return rate;
	}

	public void setRate(ParkingRate rate) {
		this.rate = rate;
	}

	public CarParkStatus getCarParkStatus() {
		return carParkStatus;
	}

	public void setCarParkStatus(CarParkStatus carParkStatus) {
		this.carParkStatus = carParkStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getHeightRestriction() {
		return heightRestriction;
	}

	public void setHeightRestriction(Integer heightRestriction) {
		this.heightRestriction = heightRestriction;
	}

	public AccessControlTypes getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(AccessControlTypes accessControl) {
		this.accessControl = accessControl;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getEnableFutureWeeks() {
		return enableFutureWeeks;
	}

	public void setEnableFutureWeeks(int enableFutureWeeks) {
		this.enableFutureWeeks = enableFutureWeeks;
	}

	public boolean getDynamicPricing() {
		return dynamicPricing;
	}

	public void setDynamicPricing(boolean dynamicPricing) {
		this.dynamicPricing = dynamicPricing;
	}

	public BigDecimal getTargetRevenue() {
		return targetRevenue;
	}

	public void setTargetRevenue(BigDecimal targetRevenue) {
		this.targetRevenue = targetRevenue;
	}

	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalTime getMondayFrom() {
		return mondayFrom;
	}

	public void setMondayFrom(LocalTime mondayFrom) {
		this.mondayFrom = mondayFrom;
	}

	public LocalTime getMondayTo() {
		return mondayTo;
	}

	public void setMondayTo(LocalTime mondayTo) {
		this.mondayTo = mondayTo;
	}

	public LocalTime getTuesdayFrom() {
		return tuesdayFrom;
	}

	public void setTuesdayFrom(LocalTime tuesdayFrom) {
		this.tuesdayFrom = tuesdayFrom;
	}

	public LocalTime getTuesdayTo() {
		return tuesdayTo;
	}

	public void setTuesdayTo(LocalTime tuesdayTo) {
		this.tuesdayTo = tuesdayTo;
	}

	public LocalTime getWednesdayFrom() {
		return wednesdayFrom;
	}

	public void setWednesdayFrom(LocalTime wednesdayFrom) {
		this.wednesdayFrom = wednesdayFrom;
	}

	public LocalTime getWednesdayTo() {
		return wednesdayTo;
	}

	public void setWednesdayTo(LocalTime wednesdayTo) {
		this.wednesdayTo = wednesdayTo;
	}

	public LocalTime getThursdayFrom() {
		return thursdayFrom;
	}

	public void setThursdayFrom(LocalTime thursdayFrom) {
		this.thursdayFrom = thursdayFrom;
	}

	public LocalTime getThursdayTo() {
		return thursdayTo;
	}

	public void setThursdayTo(LocalTime thursdayTo) {
		this.thursdayTo = thursdayTo;
	}

	public LocalTime getFridayFrom() {
		return fridayFrom;
	}

	public void setFridayFrom(LocalTime fridayFrom) {
		this.fridayFrom = fridayFrom;
	}

	public LocalTime getFridayTo() {
		return fridayTo;
	}

	public void setFridayTo(LocalTime fridayTo) {
		this.fridayTo = fridayTo;
	}

	public LocalTime getSaturdayFrom() {
		return saturdayFrom;
	}

	public void setSaturdayFrom(LocalTime saturdayFrom) {
		this.saturdayFrom = saturdayFrom;
	}

	public LocalTime getSaturdayTo() {
		return saturdayTo;
	}

	public void setSaturdayTo(LocalTime saturdayTo) {
		this.saturdayTo = saturdayTo;
	}

	public LocalTime getSundayFrom() {
		return sundayFrom;
	}

	public void setSundayFrom(LocalTime sundayFrom) {
		this.sundayFrom = sundayFrom;
	}

	public LocalTime getSundayTo() {
		return sundayTo;
	}

	public void setSundayTo(LocalTime sundayTo) {
		this.sundayTo = sundayTo;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	public String getStripeUrl() {
		return stripeUrl;
	}

	public void setStripeUrl(String stripeUrl) {
		this.stripeUrl = stripeUrl;
	}

}
