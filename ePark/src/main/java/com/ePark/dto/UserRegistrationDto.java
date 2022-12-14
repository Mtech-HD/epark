package com.ePark.dto;

import com.ePark.model.Roles;

public class UserRegistrationDto {

	private String username;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private String customerId;

	private Roles roles;

	private long roleId;

	private long carParkId;

	private String returnPage;

	private Boolean adminCreated;

	public UserRegistrationDto() {
	}

	public UserRegistrationDto(String username, String password, String firstName, String lastName, String email,
			String customerId, Roles roles) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.customerId = customerId;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public long getCarParkId() {
		return carParkId;
	}

	public void setCarParkId(long carParkId) {
		this.carParkId = carParkId;
	}

	public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

	public Boolean getAdminCreated() {
		return adminCreated;
	}

	public void setAdminCreated(Boolean adminCreated) {
		this.adminCreated = adminCreated;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

}
