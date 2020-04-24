import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import depot.Driver;
import depot.User;
import depot.Vehicle;

public class Sys {
	private static boolean loggedIn = false;
	private static User loggedInUser = null;
	private static ArrayList<User> users = new ArrayList<User>();
	private static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	private static ArrayList<Driver> drivers = new ArrayList<Driver>();
	private static boolean menuLoop = true;
	private static Scanner userInput = new Scanner(System.in);
	public static String curUser = " ";

	public static void main(String[] args) throws IOException {
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
		username = userInput.nextLine();
		System.out.println("Enter Password:");
		password = userInput.nextLine();

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

//the menu the driver sees
	private static void driverMenu() throws IOException {
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
				curUser = null;

				break;
			}
			default:
				System.out.println("Invalid choice entered, please try again.");
				break;
			}
		}
	}

//else
	// the menu the manager sees
	private static void managerMenu() throws IOException {
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
			menuLoop = false;
			loggedIn = false;
			loggedInUser = null;
			System.out.println("Goodbye " + curUser + ".\nYou are now logged out.");
			curUser = null;
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
		System.out.println("\n");
		while ((line = reader.readLine()) != null) {
			lines.add(line + "\n ");
		}

		int size = (lines.size());

		for (int x = 0; x < size; x++) {
			if (x == 0) {
				String output = lines.get(x);

				String formattedString = output.toString().replace(",", "\t|\t"); // remove the commas

				System.out.println(formattedString
						+ "\n________________________________________________________________________________________");
			} else {
				String output = lines.get(x);

				String formattedString = output.toString().replace(",", "\t|\t"); // remove the commas

				System.out.println(formattedString);
			}
		}
		reader.close();
	}

//THE SCHEDULE DRIVERS SEE OF ONLY THEIR JOBS
	private static void personalSchedule() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = null;
		int noOutput = 0;
		while ((line = reader.readLine()) != null) {
			lines.add(line + "\n ");
		}
		int size = (lines.size());

		for (int x = 0; x < size; x++) {// FIRST LINE
			if (x == 0) {
				String output = lines.get(x);

				String formattedString = output.toString().replace(",", "\t|\t"); // remove the commas

				System.out.println(formattedString
						+ "\n________________________________________________________________________________________");
			} else { // REST OF ARRAY
				String output = lines.get(x);
				{
					if (output.contains(curUser)) {
						String formattedString = output.toString().replace(",", "\t|\t"); // remove the commas
						noOutput++;

						System.out.println(formattedString);
					} else {
						// do nowt
					}
				}
			}
		}
		if (noOutput == 0) {
			System.out.println("NO SCHEDULED JOBS");

		}
		reader.close();

	}

	@SuppressWarnings("unlikely-arg-type")
	private static void arrangeSchedule() throws IOException {

		Scanner ss = new Scanner(new File("src/vehicle.csv"));
		ss.useDelimiter(",");
		while (ss.hasNextLine()) {
			String s = ss.nextLine();
			String[] split = s.split(",");
			Vehicle x = new Vehicle(split[0], split[1], split[2], split[3], "depot");
			vehicles.add(x);

			FileWriter csvWriter = new FileWriter("src/schedule.csv", true);
			Scanner in = new Scanner(System.in);

			System.out.println("Enter Client Name:");
			String client = in.next();
			System.out.println("Enter Start Date: (DD/MM/YYYY)");
			String start = in.next();
			System.out.println("Enter End Date: (DD/MM/YYYY)");
			String end = in.next();
			System.out.println("Enter Vehicle Reg:");
			String vehicleReg = in.next();
			System.out.println("Enter Driver ID:");
			String driverID = in.next();

			List<List<String>> rows = Arrays.asList(Arrays.asList(client, start, end, vehicleReg, driverID));

			for (Driver driver : drivers) {
				// System.out.println(user.getpassword());
				if ((driver.getdriverID().equals(driverID))) {
					System.out.println("Driver Found");

					break;
				} else {
					System.out.println("Driver Not Found");
					arrangeSchedule();
					break;
				}
			}

			for (Vehicle vehicle : vehicles) {
				// System.out.println(user.getpassword());
				if ((vehicle.getregNo().equals(vehicleReg))) {
					System.out.println("Vehicle Found");

					break;
				} else {
					System.out.println("Vehicle Not Found");
					arrangeSchedule();
					break;
				}
			}

			for (List<String> rowData : rows) {
				csvWriter.append(String.join(",", rowData));
				csvWriter.append("\n");
			}

			System.out.println("--Confirm new schedule details--");
			System.out.println("Client Name: " + client);
			System.out.println("Start Date: " + start);
			System.out.println("End Date: " + end);
			System.out.println("Vehicle ID: " + vehicleReg);
			System.out.println("Driver ID: " + driverID);
			System.out.println("Is this correct Y/N");
			String choice = in.next();

			if (choice.equalsIgnoreCase("Y")) {

				System.out.println("---Complete---");
				csvWriter.flush();
				csvWriter.close();
			} else if (choice.equalsIgnoreCase("N")) {
				System.out.println("--Schedule Cancelled--");
			}

			managerMenu();
		}

	}

	private static void moveVehicle() {
		Scanner in = new Scanner(System.in);

		System.out.println("--Move Vehicle--");
		System.out.println("Specify vehicle ID:");
		String vehicleID = in.next();

		// check vehicle id exists
		System.out.println("Invalid Vehicle Code");
		System.out.println("Specify move date:");
		String moveDate = in.next();

		// check date is valid
		System.out.println("Invalid date");
		// error messages
		System.out.println("Vehicle is in active state");
		System.out.println("Vehicle is in pending state");
		// if vehicle isnt active or pending
		System.out.println("Specify depot you wish to move vehicle to:");
		String newDepot = in.next();

		System.out.println(
				"Do you confirm you wish to move Vehicle ID: " + vehicleID + " from depot:" + " to depot:" + newDepot);
		System.out.println("Is this correct Y/N");
		String choice = in.next();
		System.out.println("---Vehicle Move Succesful---");
		System.out.println("---Vehicle Move Cancelled---");

	}

	private static void setupVehicle() throws IOException {
		Scanner scannerVehicle = new Scanner(new File("src/vehicle.csv"));
		scannerVehicle.useDelimiter(",");
		while (scannerVehicle.hasNextLine()) {
			String s = scannerVehicle.nextLine();
			String[] split = s.split(",");
			Vehicle x = new Vehicle(split[0], split[1], split[2], split[3], "depot");
			vehicles.add(x);

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
			System.out.println("Enter Vehicle Depot:");
			String depot = in.next();

			System.out.println("--Confirm new Vehicle details--");
			System.out.println("Vehicle Make: " + make);
			System.out.println("Vehicle Model: " + model);
			System.out.println("Vehicle Capacity: " + capacity);
			System.out.println("Vehicle Registration: " + registration);
			System.out.println("Vehicle Depot: " + depot);
			System.out.println("Is this correct Y/N");
			String choice = in.next();

			if (choice.equalsIgnoreCase("Y")) {

				List<List<String>> rows = Arrays.asList(Arrays.asList(make, model, capacity, registration, depot));

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

	private static void setupDriver() throws IOException {
		Scanner scannerDriver = new Scanner(new File("src/users.csv"));
		scannerDriver.useDelimiter(",");
		while (scannerDriver.hasNextLine()) {
			String s = scannerDriver.nextLine();
			String[] split = s.split(",");
			User x = new User(split[0], split[1], split[2], split[3], split[4]);
			users.add(x);

			FileWriter csvWriter = new FileWriter("src/users.csv", true);
			Scanner in = new Scanner(System.in);

			System.out.println("--Setup Driver--");

			System.out.println("Enter Username:");
			String username = in.next();
			System.out.println("Enter Password:");
			String password = in.next();
			String driver = "Driver";
			System.out.println("Enter Driver ID:");
			String ID = in.next();
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
			}

			managerMenu();
		}
	}
}