import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import depot.Driver;
import depot.User;
import depot.Vehicle;
import depot.WorkSchedule;

public class Sys {
	private static boolean loggedIn = false;
	private static User loggedInUser = null;
	private static ArrayList<User> users = new ArrayList<User>();
	private static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	private static ArrayList<WorkSchedule> workschedules = new ArrayList<WorkSchedule>();

	private static ArrayList<Driver> drivers = new ArrayList<Driver>();
	private static boolean menuLoop = true;
	private static Scanner userInput = new Scanner(System.in);
	public static String curUser = " ";
	public static String availRegs = " ";

	public static void main(String[] args) throws IOException, ParseException {
		boolean running = true;

		// displaying the correct menus
		while (running) {
			if (!loggedIn) {
				loginMenu();
			} else {
				if (loggedInUser.getRole().equals("Driver")) {
					driverMenu();

				} else if (loggedInUser.getRole().equals("Manager")) {
					managerMenu();
				} else {
					System.out.println("Invalid job role.");
					loginMenu();
				}
			}
		}

	}

	public static void loginMenu() throws FileNotFoundException {

		Scanner su = new Scanner(new File("src/users.csv"));
		su.useDelimiter(",");
		while (su.hasNextLine()) {
			String s = su.nextLine();
			String[] split = s.split(",");
			User y = new User(split[0], split[1], split[2], split[3], "depot");
			users.add(y);

			// System.out.print(username + " " + password + " " + account);

		}

		su.close();

		String username, password;

		System.out.println("-- DEPOT SYSTEM--");
		System.out.println(" --LOGIN--");
		System.out.println("Enter Username:");
		username = userInput.next();
		System.out.println("Enter Password:");
		password = userInput.next();

		for (User user : users) {
			// System.out.println(user.getpassword());
			if ((user.getUsername().equals(username)) && (user.getPassword().equals(password))) {
				curUser = username;
				loggedIn = true;
				loggedInUser = user;

				System.out.println("welcome user : " + curUser);
				break;
			}
		}
		if (!loggedIn) {
			System.out.println("Incorrect login details.");
		}
	}

	// the menu the driver sees
	private static void driverMenu() throws IOException {
		menuLoop = true;
		while (menuLoop) {
			String choice = "";
			System.out.println("-- DEPOT SYSTEM--");
			System.out.println("1. View Personal Schedule");
			System.out.println("Q. Log Out");
			System.out.print("Pick : ");
			choice = (userInput.next().toUpperCase());

			switch (choice) {
			case "1": {
				personalSchedule();
				break;
			}
			case "Q": {
				menuLoop = false;
				loggedIn = false;
				loggedInUser = null;
				System.out.println("Goodbye " + curUser + ".\nYou are now logged out.");
				curUser = " ";
				break;
			}
			default:
				System.out.println("Invalid choice entered, please try again.");
				break;
			}
		}
	}

	// else
	// the menu the manager sees
	private static void managerMenu() throws IOException, ParseException {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View All Schedules");
		System.out.println("2. Setup Work Schedule");
		System.out.println("3. Move Vehicle");
		System.out.println("4. Set up Vehicle");
		System.out.println("5. Set up Driver");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");
		choice = (userInput.next().toUpperCase());
		// userInput.nextLine();
		// while (menuLoop) {
		switch (choice) {
		case "1": {
			viewSchedule();
			break;
		}
		case "2": {
			arrangeSchedule();
			break;
		}
		case "3": {
			moveVehicle();
			break;
		}
		case "4": {
			setupVehicle();
			break;
		}
		case "5": {
			setupDriver();
			break;
		}
		case "Q": {
			loggedIn = false;
			loggedInUser = null;
			System.out.println("Goodbye " + curUser + ".\nYou are now logged out.");
			curUser = " ";
			break;
		}
		default:

			System.out.println("Invalid choice entered, please try again.");
			break;
		}
	}
	// }

	// THE SCHEDULE MANAGERS SEE OF ALL DETAILS
	private static void viewSchedule() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = null;
		int noOutput = 0;
		System.out.println("\n");
		while ((line = reader.readLine()) != null) {
			lines.add(line + "\n ");
		}
		int size = (lines.size());
		if (size != 0) {

			for (int x = 0; x < size; x++) {
				String output = lines.get(x);

				String formattedString = output.toString().replace(",", "\t|\t");

				System.out.println(formattedString);
				noOutput++;

			}
			if (noOutput == 0) {
				System.out.println("NO JOBS");
				reader.close();
			}

		} else {
			System.out.println("NO JOBS ");
			reader.close();

		}

	}

	// THE SCHEDULE DRIVERS SEE OF ONLY THEIR JOBS
	private static void personalSchedule() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = null;
		int noOutput = 0;
		while ((line = reader.readLine()) != null) {
			lines.add(line + "\n ");
		}
		int size = (lines.size());
		if (size != 0) {
			for (int x = 0; x < size; x++) {
				String output = lines.get(x);
				{
					if (output.contains(curUser)) {
						String formattedString = output.toString().replace(",", "\t|\t"); // remove the commas
						noOutput++;

						System.out.println(formattedString);
					} else {
						// do nowt
					}
					if (noOutput == 0) {
						System.out.println("NO JOBS");
						reader.close();
					}

				}
			}
		} else {
			System.out.println("NO JOBS ");
			reader.close();

		}
	}

	/*
	 * if (noOutput == 0) { System.out.println("NO SCHEDULED JOBS");
	 * 
	 * }
	 */

	private static void arrangeSchedule() throws IOException, ParseException {
		// BufferedReader reader = new BufferedReader(new
		// FileReader("src/schedule.csv"));

		Scanner in = new Scanner(System.in);
		// Scanner sf = new Scanner(new File("src/vehicles.csv"));
		// sf.useDelimiter(",");
		// while (sf.hasNextLine()) {
		// String s = sf.nextLine();
		// String[] split = s.split(",");
		// Vehicle c = new Vehicle(split[0], split[1], split[2], split[3], split[4],
		// split[5]);
		// vehicles.add(c);
		// }
		//
		// sf.close();

		Scanner ss = new Scanner(new File("src/schedule.csv"));
		ss.useDelimiter(",");
		while (ss.hasNextLine()) {
			String s = ss.nextLine();
			String[] split = s.split(",");
			WorkSchedule y = new WorkSchedule(split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7]);
			workschedules.add(y);
		}

		ss.close();

		System.out.println("Enter Client Name:");
		String client = in.next();

		for (WorkSchedule workschedules : workschedules) {
			// if (client.equals(workschedules.getClient())) {
			// System.out.println("-- Client Already Found --");
			// arrangeSchedule();
			// } else {
			// System.out.println("-- Client Not Found --");

			//regAvailable();

			System.out.println("List Vehicle Type (Truck or Tanker):");
			String vehicleType = in.next();

			if (vehicleType.equals("Truck")) {
				truck();
			
			}

			System.out.println("Enter Vehicle Reg:");
			String vehicleReg = in.next();

			if (vehicleReg.equals(workschedules.getVehicleReg())) {
				System.out.println("-- Vehicle Exists --");
			} else {
				System.out.println("-- Vehicle Does Not Exist --");

				driversAvailable();
				System.out.println("Enter Driver ID:");
				String driverID = in.next();

				if (driverID.equals(workschedules.getDriverID())) {
					System.out.println("-- DriverID In Use --");
					arrangeSchedule();
				} else {
					System.out.println("-- DriverID Is Free To Use --");

					Date start = new Date();
					System.out.println("Specify Start Date:");
					String startDate = in.next();

					DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE, 2);
					start = c.getTime();

					if (new SimpleDateFormat("dd/MM/yyyy").parse(startDate).after(start)) {
						System.out.println("-- Valid Start Date --");

						Date end = new Date();

						System.out.println("Specify End Date:");
						String endDate = in.next();

						Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);

						Calendar cal = Calendar.getInstance();
						cal.setTime(date1);
						cal.add(Calendar.DATE, 3);

						Date endDate72 = c.getTime();

						// endDate + 72 hours

						Date dateEnd = new Date();

						if (new SimpleDateFormat("dd/MM/yyyy").parse(endDate).before(endDate72)) {
							System.out.println("-- End Date Must Be 72 Hours Before Start Date --");
							arrangeSchedule();
						} else {
							System.out.println("* VALID END *");

							System.out.println("Specify Start Time For Entered Start Date(HH:mm:ss):");
							String startTime = in.next();
							LocalTime lt = LocalTime.parse(startTime);

							System.out.println("Specify End Time For Entered End Date(HH:mm:ss):");
							String endTime = in.next();
							LocalTime lt2 = LocalTime.parse(endTime);

							System.out.println("Start Date: " + startDate);
							System.out.println("End Date: " + endDate);
							System.out.println("Start time: " + startTime);
							System.out.println("End time: " + endTime + "\nIs this correct? (Y/N)");
							String choice = in.next();
							if (choice.equalsIgnoreCase("Y")) {

								List<WorkSchedule> schedules = new ArrayList<WorkSchedule>();
								try {
									Scanner fileReader = new Scanner(new FileReader("src/schedule.csv"));
									while (fileReader.hasNextLine()) {
										String line = fileReader.nextLine();
										String[] splitLine = line.split(",");

										schedules.add(new WorkSchedule(client, vehicleType, startDate, endDate, startTime, endTime,
												vehicleReg, driverID));

										break;
									}
									fileReader.close();

								} catch (Exception e) {
									System.out.println(e);
								}
								System.out.println(schedules.size());
								FileWriter csvWriter = new FileWriter("src/schedule.csv", true);

								List<List<String>> rows = Arrays.asList(Arrays.asList(client, startDate, endDate,
										startTime, endTime, vehicleReg, driverID));

								for (List<String> rowData : rows) {
									csvWriter.append(String.join(",", rowData));
									csvWriter.append(",");
									csvWriter.append("\n");
								}

								csvWriter.flush();
								csvWriter.close();

								System.out.println("-- Schedule Added Successfully --");

							} else if (choice.equalsIgnoreCase("N")) {
								System.out.println("-- Schedule Cancelled --");
								arrangeSchedule();
							}

						}

					} else {
						System.out.println("-- Start Date Must Be 48 Hours From Today --");
						arrangeSchedule();
					}
				}

			}

		}
	}

	// }

	private static void driversAvailable() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = "";

		while ((line = reader.readLine()) != null) {
			String[] DriverID = line.trim().split(",");
			for (int w = 0; w < line.length(); w++) {
				if (!lines.contains(DriverID[6])) {
					lines.add(DriverID[6]);
				}
				w++;
			}
		}

		BufferedReader UserReader = new BufferedReader(new FileReader("src/users.csv"));
		List<String> Userlines = new ArrayList<>();
		List<String> UserDetails = new ArrayList<String>();
		String Uline = "";
		while ((Uline = UserReader.readLine()) != null) {
			String[] UserDriverID = Uline.trim().split(",");

			for (int e = 0; e < Uline.length(); e++) {
				if (!Userlines.contains(UserDriverID[2])) {
					Userlines.add(UserDriverID[2]);
					UserDetails.add(UserDriverID[e]);/// ??dont think need

				}
				e++;
			}
		}
		Collections.sort(lines);
		Collections.sort(Userlines);
		Userlines.removeAll(lines);

		// System.out.println(UserDetails);

		int userSize = UserDetails.size();// need??
		int size = Userlines.size();
		for (int r = 0; r < size; r++) {

			String curID = Userlines.get(r);
			String drivername = (UserDetails.get(r));
			System.out.println("Driver " + drivername + "  ID : " + curID);

			reader.close();
			UserReader.close();
		}
	}

	private static void regAvailable() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = "";

		while ((line = reader.readLine()) != null) {
			String[] reg = line.trim().split(",");
			for (int w = 0; w < line.length(); w++) {
				if (!lines.contains(reg[5])) {
					lines.add(reg[5]);
				}
				w++;
			}
		}

		BufferedReader UserReader = new BufferedReader(new FileReader("src/vehicle.csv"));
		List<String> Userlines = new ArrayList<>();
		List<String> UserDetails = new ArrayList<String>();
		String Uline = "";
		while ((Uline = UserReader.readLine()) != null) {
			String[] UserDriverID = Uline.trim().split(",");

			for (int e = 0; e < Uline.length(); e++) {
				if (!Userlines.contains(UserDriverID[3])) {
					Userlines.add(UserDriverID[3]);
					UserDetails.add(UserDriverID[e]);/// ??dont think need

				}
				e++;
			}
		}
		Collections.sort(lines);
		Collections.sort(Userlines);
		Userlines.removeAll(lines);

		int size = Userlines.size();
		if (size != 0) {
			for (int r = 0; r < size; r++) {

				String Reg = (Userlines.get(r));
				availRegs = availRegs + "\n" + Reg;
				reader.close();
				UserReader.close();

			}

			System.out.println("Registrations available:");
			System.out.println(availRegs);
		} else if (size == 0) {
			System.out.println("No available vehicles.");
		}
	}

	private LocalDateTime createDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return LocalDateTime.parse(date, formatter);

	}

	private static void truck() throws IOException, ParseException {
		BufferedReader scheduleReader = new BufferedReader(new FileReader("src/vehicle.csv"));
		Scanner scannerSchedule = new Scanner(new File("src/vehicle.csv"));
		List<String> types = new ArrayList<>();
		List<String> regs = new ArrayList<>();
		String line = "";

		while (scannerSchedule.hasNextLine()) {
			String s = scannerSchedule.nextLine();
			String[] split = s.split(",");
			Vehicle x = new Vehicle(split[0], split[1], split[2], split[3], split[4], split[5]);
			vehicles.add(x);

		}

		scannerSchedule.close();

		String truck = "Truck";

		boolean value = false;

		Vehicle vehicle = null;
		for (Vehicle v : vehicles) {
			if (v.getvehicleType().equalsIgnoreCase(truck)) {

				value = true;
				vehicle = v;
			}
		}

		if (value == true) {
			for (Vehicle v : vehicles) {
				if (v.getvehicleType().equalsIgnoreCase("Truck")) {
					while ((line = scheduleReader.readLine()) != null) {
						String[] type = line.trim().split(",");
						for (int w = 0; w < line.length(); w++) {
							if (!types.contains(type[4])) {
								types.add(type[4]);
							}
							if (!regs.contains(type[3])) {
								regs.add(type[3]);
							}
							w++;
							break;

						}
					}

					System.out.println(types + " - " + regs);
					break;

				} else {

					System.out.println("-- No Truck Data --");
					arrangeSchedule();
				}
			}
		}

	}

	private boolean checkDate(String start, String end) {
		String dateFormat = "[0-3][0-9]/[0-1][0-9]/20[0-9][0-9] [0-2][0-9]:[0-6][0-9]";

		if (start.matches(dateFormat) && (end.matches(dateFormat))) {
			LocalDateTime currentDate = LocalDateTime.now();
			LocalDateTime compDate = createDate(start);
			if (compDate.isAfter(currentDate)) {
				return true;
			} else {
				System.out.println("Invalid date entered, please try again.");
			}
			return false;
		}
		return false;
	}

	private void isAvailable(String client, String startDate, String endDate, String vehicleReg, String driverID)
			throws IOException, ParseException {
		Scanner su = new Scanner(new File("src/schedule.csv"));
		su.useDelimiter(",");
		while (su.hasNextLine()) {
			String s = su.nextLine();
			String[] split = s.split(",");
			WorkSchedule y = new WorkSchedule(split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7]);
			workschedules.add(y);
		}

		su.close();

		for (WorkSchedule workschedule : workschedules) {
			if ((workschedule.getDriverID().contains(startDate)) && (workschedule.getDriverID().contains(endDate))) {

				System.out.println("Driver is available on these dates");

				break;
			} else {
				System.out.println("This Driver isn't available on these dates.");
				arrangeSchedule();
				break;
			}
		}
	}

	private static boolean isVehicleAvailable(String vehicleReg, String startDatein, String endDatein)
			throws IOException, ParseException {

		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDatein);
		Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDatein);

		Scanner su = new Scanner(new File("src/schedule.csv"));
		su.useDelimiter(",");
		while (su.hasNextLine()) {
			String s = su.nextLine();
			String[] split = s.split(",");
			WorkSchedule y = new WorkSchedule(split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7]);
			workschedules.add(y);
		}

		su.close();

		for (WorkSchedule workschedule : workschedules) {

			if (workschedule.getVehicleReg().equalsIgnoreCase(vehicleReg)) {
				if ((workschedule.getStartDate().after(startDate) && (workschedule.getStartDate().before(endDate)))
						|| ((workschedule.getEndDate().after(startDate)
								&& (workschedule.getEndDate().before(endDate))))) {
					return false;
				}
			}
		}
		return true;
	}

	private static void moveVehicle() throws IOException, ParseException {
		Calendar cal = Calendar.getInstance();
		Scanner in = new Scanner(System.in);

		Scanner scannerMove = new Scanner(new File("src/vehicle.csv"));
		scannerMove.useDelimiter(",");
		while (scannerMove.hasNextLine()) {
			String s = scannerMove.nextLine();
			String[] split = s.split(",");
			Vehicle y = new Vehicle(split[0], split[1], split[2], split[3], split[4], split[5]);
			vehicles.add(y);

		}

		System.out.println("--Move Vehicle--");
		System.out.println("Specify Vehicle Registration:");
		String registrationValue = in.nextLine();

		boolean value = false;

		Vehicle vehicle = null;
		for (Vehicle v : vehicles) {
			if ((v.getregNo().equals(registrationValue))) {
				value = true;
				vehicle = v;
			}
		}
		if (value == true) {
			System.out.println("--Vehicle Found--");
			System.out.println(vehicle.getMake());

			System.out.println("Specify move date:");
			String moveDate = in.next();

			if (new SimpleDateFormat("dd/MM/yyyy").parse(moveDate).after(new Date())) {
				System.out.println("--Date in Future--");

				// error messages
				// System.out.println("Vehicle is in active state");
				// System.out.println("Vehicle is in pending state");
				if (isVehicleAvailable(registrationValue, moveDate, "01/01/3000")) {
					System.out.println("Specify depot you wish to move vehicle to:");
					String newDepot = in.next();

					System.out.println("Do you confirm you wish to move Vehicle Registration: " + registrationValue
							+ " from depot: " + vehicle.getDepot() + " to depot: " + newDepot);
					System.out.println("Is this correct Y/N");
					String choice = in.next();

					if (choice.equalsIgnoreCase("Y")) {

						List<Vehicle> vehicles = new ArrayList<Vehicle>();
						try {
							Scanner fileReader = new Scanner(new FileReader("src/vehicle.csv"));
							while (fileReader.hasNextLine()) {
								String line = fileReader.nextLine();
								String[] splitLine = line.split(",");

								if (line.contains(registrationValue)) {
									vehicles.add(new Vehicle(splitLine[0], splitLine[1], splitLine[2], splitLine[3],
											splitLine[4], newDepot));

								} else {
									vehicles.add(new Vehicle(splitLine[0], splitLine[1], splitLine[2], splitLine[3],
											splitLine[4], splitLine[5]));

								}

							}
							fileReader.close();

						} catch (Exception e) {
							System.out.println(e);
						}
						FileWriter csvWriter = new FileWriter("src/vehicle.csv");

						for (Vehicle v : vehicles) {
							csvWriter.append(v.getMake() + "," + v.getModel() + "," + v.getWeight() + "," + v.getregNo()
									+ "," + v.getDepot());
							csvWriter.append("\n");
						}

						csvWriter.flush();
						csvWriter.close();

						System.out.println("---Vehicle Move Succesful---");

					} else if (choice.equalsIgnoreCase("N")) {
						System.out.println("---Vehicle Move Cancelled---");
					}

				} else {
					System.out.println("Vehicle not available");
				}

				managerMenu();

			} else {
				System.out.println("--Date Invalid--");
			}

		} else {
			System.out.println("--Invalid Vehicle Registration--");
			moveVehicle();
		}
	}

	private static void setupVehicle() throws IOException, ParseException {
		Scanner scannerVehicle = new Scanner(new File("src/vehicle.csv"));
		scannerVehicle.useDelimiter(",");

		while (scannerVehicle.hasNextLine()) {
			String s = scannerVehicle.nextLine();
			String[] split = s.split(",");
			Vehicle x = new Vehicle(split[0], split[1], split[2], split[3], split[4], split[5]);
			vehicles.add(x);

		}

		scannerVehicle.close();

		FileWriter csvWriter = new FileWriter("src/vehicle.csv", true);
		Scanner in = new Scanner(System.in);

		System.out.println("--Setup Vehicle--");

		System.out.println("Enter Vehicle Make:");
		String make = in.next();
		System.out.println("Enter Vehicle Model:");
		String model = in.next();
		System.out.println("Enter Vehicle Capacity:");
		String capacity = in.next();

		System.out.println("Enter Vehicle Registration:");
		String registration = in.next();

		boolean value = false;

		Vehicle vehicle = null;
		for (Vehicle v : vehicles) {
			if (v.getregNo().equals(registration)) {

				value = true;
				vehicle = v;
			}

		}

		if (value == true) {
			System.out.println("-- Vehicle Already in System --");
			setupVehicle();

		} else {
			System.out.println("-- Vehicle Not In System --");

			System.out.println("Enter Vehicle Type (Truck or Tanker):");
			String vehicleType = in.next();

			System.out.println("Enter Vehicle Depot:");
			String depot = in.next();

			System.out.println("--Confirm new Vehicle details--");
			System.out.println("Vehicle Make: " + make);
			System.out.println("Vehicle Model: " + model);
			System.out.println("Vehicle Capacity: " + capacity);
			System.out.println("Vehicle Registration: " + registration);
			System.out.println("Vehicle Type: " + vehicleType);
			System.out.println("Vehicle Depot: " + depot);
			System.out.println("Is this correct Y/N");
			String choice = in.next();

			if (choice.equalsIgnoreCase("Y")) {

				List<List<String>> rows = Arrays
						.asList(Arrays.asList(make, model, capacity, registration, vehicleType, depot));

				for (List<String> rowData : rows) {
					csvWriter.append(String.join(",", rowData));
					csvWriter.append(",");
					csvWriter.append("\n");
				}

				csvWriter.flush();
				csvWriter.close();
			} else if (choice.equalsIgnoreCase("N")) {
				System.out.println("--Schedule Cancelled--");
			}

			managerMenu();
		}
	}

	private static void setupDriver() throws IOException, ParseException {
		Scanner scannerDriver = new Scanner(new File("src/users.csv"));
		scannerDriver.useDelimiter(",");

		while (scannerDriver.hasNextLine()) {
			String s = scannerDriver.nextLine();
			String[] split = s.split(",");
			User x = new User(split[0], split[1], split[2], split[3], split[4]);
			users.add(x);
		}

		scannerDriver.close();

		FileWriter csvWriter = new FileWriter("src/users.csv", true);
		Scanner in = new Scanner(System.in);

		System.out.println("--Setup Driver--");

		System.out.println("Enter Username:");
		String username = in.next();

		for (User users : users) {
			if (username.equals(users.getUsername())) {
				System.out.println("-- Username in use-- ");
				setupDriver();
			} else {
				System.out.println("-- Username Free -- ");

				System.out.println("Enter Password:");
				String password = in.next();

				String driver = "Driver";

				System.out.println("Enter Driver ID:");
				String ID = in.next();

				if (ID.equals(users.getdriverID())) {
					System.out.println("-- ID in use-- ");
					setupDriver();
				} else {
					System.out.println("-- ID Free -- ");

					System.out.println("Enter Vehicle Depot:");
					String depot = in.next();

					System.out.println("--Confirm new Driver details--");
					System.out.println("Username: " + username);
					System.out.println("Password: " + password);
					System.out.println("Driver ID: " + ID);
					System.out.println("Vehicle Depot: " + depot);
					System.out.println("Is this correct Y/N");
					String choice = in.next();

					if (choice.equalsIgnoreCase("Y")) {

						List<List<String>> rows = Arrays.asList(Arrays.asList(username, password, driver, ID, depot));

						for (List<String> rowData : rows) {
							csvWriter.append(String.join(",", rowData));
							csvWriter.append(",");
							csvWriter.append("\n");
						}

						csvWriter.flush();
						csvWriter.close();
					} else if (choice.equalsIgnoreCase("N")) {
						System.out.println("--Schedule Cancelled--");
						break;
					}

					managerMenu();
				}

			}

		}
	}
}
