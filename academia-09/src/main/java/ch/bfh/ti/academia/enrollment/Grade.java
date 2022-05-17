/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enrollment;

/**
 * The class Grade represent the body of /api/enrollments/{eid} (PUT-Method)
 */
public class Grade {

	private String grade;

	public Grade() {
	}

	public Grade(String grade) {
		this.grade = grade;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}