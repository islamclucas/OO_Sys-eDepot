package depot;

public class User {

	private String username, password, role, depot;

	public User(String username,String password,String role,String depot) {
	
		this.username = username;
		this.password = password;
		this.role = role;
		this.depot = depot;

	}
	public String getusername() {
		return this.username;
	}
	public String getpassword() {
		return this.password;
	}
	public String getrole() {
		return this.role;
	}
	public String getdepot() {
		return this.depot;
	}
}
