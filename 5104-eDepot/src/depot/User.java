package depot;

public class User {

	private String username, password, role, depot;

	public User(String username,String password,String role,String depot) {
	
		this.username = username;
		this.password = password;
		this.role = role;
		this.depot = depot;

	}
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public String getRole() {
		return this.role;
	}
	public String getDepot() {
		return this.depot;
	}
}
