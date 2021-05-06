package com.ePark.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Roles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long roleId;

	private String name;

	public Roles() {

	}

	public Roles(long roleId) {
		this.roleId = roleId;
	}

	public Roles(String name) {
		super();
		this.name = name;
	}

	public Roles(long roleId, String name) {
		this.roleId = roleId;
		this.name = name;
	}

	public Roles(Roles role) {
		this.roleId = role.roleId;
		this.name = role.name;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
