/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;
import ch.bfh.ti.academia.person.PersonDTO;

import java.util.Objects;

/**
 * The class Module represents a module object
 */
public class Module {
	private String mid;
	private String name;
	private String description;
	private PersonDTO coordinator;

	public Module() {
	}

	public Module(String mid, String name, String description, PersonDTO coordinator) {
		this.mid = mid;
		this.name = name;
		this.description = description;
		this.coordinator = coordinator;
	}

	public PersonDTO getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(PersonDTO coordinator) {
		this.coordinator = coordinator;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		return Objects.equals(mid, ((Module) other).mid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mid);
	}

	@Override
	public String toString() {
		return "Module{number=" + mid + ", name='" + name + "', description='" + description + "'}";
	}
}
