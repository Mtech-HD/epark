package com.ePark.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "carParkComments")
public class CarParkComments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long carParkCommentId;

	private String comment;

	private Date dateCreated;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId", nullable = false)
	private Users users;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "carParkId", nullable = false)
	private CarParks carParks;

	public CarParkComments() {

	}
	
	public CarParkComments(String comment, Date dateCreated, Users users, CarParks carParks) {
		super();
		this.comment = comment;
		this.dateCreated = dateCreated;
		this.users = users;
		this.carParks = carParks;
	}

	public long getCarParkCommentId() {
		return carParkCommentId;
	}

	public void setCarParkCommentId(long carParkCommentId) {
		this.carParkCommentId = carParkCommentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public CarParks getCarParks() {
		return carParks;
	}

	public void setCarParks(CarParks carParks) {
		this.carParks = carParks;
	}

}