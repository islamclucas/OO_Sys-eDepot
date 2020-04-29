package depot;

public class Vehicle {
	protected String make;
	protected String model;
	protected String weight;
	protected String regNo;
	protected String vehicleType;
	protected String depot;
	
	public Vehicle(String make, String model, String weight, String regNo, String vehicleType, String depot) {
		this.make = make;
		this.model = model;
		this.weight = weight;
		this.regNo = regNo;
		this.vehicleType = vehicleType;
		this.depot = depot;

	}
	public Vehicle(String make2, String model2, int weight2, String regNo2) {
		// TODO Auto-generated constructor stub
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
