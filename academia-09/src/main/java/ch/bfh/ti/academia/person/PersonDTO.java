/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

/**
 * The class PersonDTO represents an DTO Pattern for a person
 */
public class PersonDTO {
	private int pid;
	private String firstname;
	private String lastname;

	public PersonDTO(int pid, String firstname, String lastname) {
		this.pid = pid;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public PersonDTO() {

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

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
