package depot;

public class Truck extends Vehicle {
	private int cargoCapacity;

	public Truck(String make, String model, int weight, String regNo, int cargoCapacity) {
		
		super(make, model, weight, regNo);
		
		this.cargoCapacity = cargoCapacity;

	}
	
	@Override
	public String toString() {
		return "Truck Details :" + make + ", " + model + ", " + weight + ", " + regNo + " - Truck Specifics :" + cargoCapacity;
	}
}
