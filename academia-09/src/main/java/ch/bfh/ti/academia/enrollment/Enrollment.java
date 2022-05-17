/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enrollment;

/**
 * The class Enrollment represents an enrollment object
 */
public class Enrollment {
	private int pid;
	private String grade;
	private String firstname;
	private String lastname;

	public Enrollment(int pid, String grade, String firstname, String lastname) {
		this.pid = pid;
		this.grade = grade;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public Enrollment() {
	}

	public int getPid() {
		return pid;
	}

	public String getGrade() {
		return grade;
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

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
