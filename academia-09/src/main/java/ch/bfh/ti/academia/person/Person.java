/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;

/**
 * The class Person represents a person object
 */
public class Person {

	private int pid;
	private String firstname;
	private String lastname;
	private String address;
	private String sex;
	private Date birthdate;
	private String role;
	private String username;
	@JsonIgnore
	private String password;

	public Person () {
	}

	public Person(int pid, String firstname, String lastname, String address, String sex, Date birthdate, String role, String username, String password) {
		this.pid = pid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.sex = sex;
		this.birthdate = birthdate;
		this.role = role;
		this.username = username;
		this.password = password;
	}

	public int getPid() {
		return pid;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getAddress() {
		return address;
	}

	public String getSex() {
		return sex;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public String getRole() {
		return role;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
