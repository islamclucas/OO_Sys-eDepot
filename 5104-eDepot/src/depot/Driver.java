package depot;

public class Driver {
	private String username;
	private String password;
	private String driverID;
	private String role;
	private String depot;
	
	public Driver(String username, String password, String driverID, String role, String depot) {
		this.username = username;
		this.password = password;
		this.driverID = driverID;
		this.username = role;
		this.password = depot;
	}
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public String getdriverID() {
		return this.driverID;
	}
	public String getRole() {
		return this.role;
	}
	public String getDepot() {
		return this.depot;
	}
}
