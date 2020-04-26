package depot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkSchedule {
	private String client;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String vehicleReg;
	private String driverID;

	public WorkSchedule(String client, String startDate, String endDate, String vehicleReg, String driverID) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		this.client = client;
		this.startDate = LocalDateTime.parse(startDate, formatter);
		this.endDate = LocalDateTime.parse(endDate, formatter);
		this.vehicleReg = vehicleReg;
		this.driverID = driverID;

	}

	public String getClient() {
		return this.client;
	}

	public LocalDateTime getStartDate() {
		return this.startDate;
	}

	public LocalDateTime getEndDate() {
		return this.endDate;
	}

	public String getVehicleReg() {
		return this.vehicleReg;
	}

	public  String getDriverID() {
		return this.driverID;
	}
}

//private String depot code
// private Vehicle vehicle;
// private Driver driver;

