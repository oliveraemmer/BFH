package ch.bfh.ti.academia.authentication;

/**
 * The class AuthResponse represent the response of /api/auth (GET-Method)
 */
public class AuthResponse {
	private int pid;
	private String role;

	public AuthResponse(int pid, String role) {
		this.pid = pid;
		this.role = role;
	}

	public AuthResponse() {

	}

	public int getPid() {
		return pid;
	}

	public String getRole() {
		return role;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
