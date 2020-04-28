package depot;

public class Vehicle {
	private String make;
	private String model;
	private String weight;
	private String regNo;
	private String vehicleType;
	private String depot;
	
	public Vehicle(String make, String model, String weight, String regNo, String vehicleType, String depot) {
		this.make = make;
		this.model = model;
		this.weight = weight;
		this.regNo = regNo;
		this.vehicleType = vehicleType;
		this.depot = depot;

	}
	public String getMake() {
		return this.make;
	}
	public String getModel() {
		return this.model;
	}
	public String getWeight() {
		return this.weight;
	}
	public String getregNo() {
		return this.regNo;
	}
	public String getvehicleType() {
		return this.vehicleType;
	}
	public String getDepot() {
		return this.depot;
	}
}
