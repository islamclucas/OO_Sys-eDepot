package depot;

public abstract class Vehicle {
	protected String make;
	protected String model;
	protected int weight;
	protected String regNo;
	
	public Vehicle(String make, String model, int weight, String regNo) {
		this.make = make;
		this.model = model;
		this.weight = weight;
		this.regNo = regNo;

	}

}
