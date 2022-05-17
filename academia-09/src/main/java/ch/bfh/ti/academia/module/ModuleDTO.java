package ch.bfh.ti.academia.module;

/**
 * The class ModuleDTO represents an DTO Pattern for a module
 */
public class ModuleDTO {
	private String mid;
	private String description;

	public ModuleDTO(String mid, String description) {
		this.mid = mid;
		this.description = description;
	}

	public ModuleDTO() {

	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMid() {
		return mid;
	}

	public String getDescription() {
		return description;
	}
}
