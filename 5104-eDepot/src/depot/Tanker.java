package depot;

public class Tanker extends Vehicle {
	private int liquidCapacity;
	private String liquidType;
	
	public Tanker(String make, String model, int weight, String regNo, int liquidCapacity, String liquidType) {
		
		super(make, model, weight, regNo);
		
		this.liquidCapacity = liquidCapacity;
		this.liquidType = liquidType;
		
	}
	
	@Override
	public String toString() {
		return "Tanker Details :" + make + ", " + model + ", " + weight + ", " + regNo + " - Tanker Specifics :" + liquidCapacity + ", " + liquidType;
	}
}
