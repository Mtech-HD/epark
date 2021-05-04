package com.ePark.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "carParkPayments")
@DynamicUpdate
public class CarParkPayments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkPaymentId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	private String yearMonth;

	private BigDecimal amount;

	private long bookings;

	private boolean paid;

	private String paymentId;

	private String failedReason;

	public CarParkPayments() {
		super();
	}
	
	public CarParkPayments(long carParkPaymentId) {
		this.carParkPaymentId = carParkPaymentId;
	}

	public CarParkPayments(CarParks carParks, String yearMonth, BigDecimal amount, long bookings, boolean paid,
			String paymentId, String failedReason) {
		super();
		this.carParks = carParks;
		this.yearMonth = yearMonth;
		this.amount = amount;
		this.bookings = bookings;
		this.paid = paid;
		this.failedReason = failedReason;

	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public long getBookings() {
		return bookings;
	}

	public void setBookings(long bookings) {
		this.bookings = bookings;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

}
