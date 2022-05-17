/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.authentication;

/**
 * The class AuthLogin represent the request body of /api/auth (POST-Method)
 */
public class AuthLogin {

	private String username;
	private String password;

	public AuthLogin() {}

	public AuthLogin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
