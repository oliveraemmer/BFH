/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.modulerun;

import ch.bfh.ti.academia.enrollment.Enrollment;
import ch.bfh.ti.academia.person.PersonDTO;

import java.util.List;

/**
 * The class Modulerun represents an modulerun object
 */
public class Modulerun {
	private int mrid;
	private String mid;
	private int year;
	private String semester;
	private String name;
	private String description;
	private List<PersonDTO> teachers;
	private PersonDTO coordinator;
	// only student attributes
	private String enrolled;
	private String grade;
	// only teacher attributes
	private List<Enrollment> enrollments;

	public Modulerun() {
	}

	public Modulerun(int mrid, String mid, int year, String semester, String name, String description, List<PersonDTO> teachers, PersonDTO coordinator, String enrolled, String grade, List<Enrollment> enrollments) {
		this.mrid = mrid;
		this.mid = mid;
		this.year = year;
		this.semester = semester;
		this.name = name;
		this.description = description;
		this.teachers = teachers;
		this.coordinator = coordinator;
		this.enrolled = enrolled;
		this.grade = grade;
		this.enrollments = enrollments;
	}

	public PersonDTO getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(PersonDTO coordinator) {
		this.coordinator = coordinator;
	}

	public int getMrid() {
		return mrid;
	}

	public String getMid() {
		return mid;
	}

	public int getYear() {
		return year;
	}

	public String getSemester() {
		return semester;
	}

	public List<PersonDTO> getTeachers() {
		return teachers;
	}

	public String getEnrolled() {
		return enrolled;
	}

	public String getGrade() {
		return grade;
	}

	public List<Enrollment> getEnrollments() {
		return enrollments;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setMrid(int mrid) {
		this.mrid = mrid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public void setTeachers(List<PersonDTO> teachers) {
		this.teachers = teachers;
	}

	public void setEnrolled(String enrolled) {
		this.enrolled = enrolled;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
