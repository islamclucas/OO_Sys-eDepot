import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import depot.User;

public class Sys {
	private static boolean loggedIn = false;
	private static User loggedInUser = null;
	private static ArrayList<User> users = new ArrayList<User>();
	private static boolean menuLoop = true;
	private static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		boolean running = true;

		// displaying the correct menus
		while (running) {
			if (!loggedIn) {
				loginMenu();
			} else {
				if (loggedInUser.getrole().equals("Driver")) {
					driverMenu();

				} else if (loggedInUser.getrole().equals("Manager")) {
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
			User y = new User(split[0], split[1], split[2], "depot");
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
			if ((user.getusername().equals(username)) && (user.getpassword().equals(password))) {

				loggedIn = true;
				loggedInUser = user;
			}
		}
	}

//the menu the driver sees
	private static void driverMenu() throws IOException {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View Schedule");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");
		choice = userInput.nextLine();

		switch (choice) {
		case "1": {
			viewSchedule();
			break;
		}
		case "Q": {
			menuLoop = false;
			loggedIn = false;
			loggedInUser = null;
			break;
		}
		default:
			System.out.println("Invalid choice entered, please try again.");
			break;
		}
	}

//else
	// the menu the manager sees
	private static void managerMenu() throws IOException {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View Schedule");
		System.out.println("2. Setup Work Schedule");
		System.out.println("3. Move Vehicle");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");
		choice = userInput.nextLine();
		while (menuLoop) {
			switch (choice) {
			case "1":
				viewSchedule();
				break;

			case "2":
				arrangeSchedule();
				break;

			case "3":
				moveVehicle();
				break;

			case "Q":
				menuLoop = false;
				loggedIn = false;
				loggedInUser = null;
				break;
			default:

				System.out.println("Invalid choice entered, please try again.");
			}
		}
	}

	private static void viewSchedule() {

		System.out.println("--View Schedule--");
		String commaDelimiter = ",";
		Scanner scanner = new Scanner(System.in);
		scanner = null;

		try {
			scanner = new Scanner(new File("src/schedule.csv"));
			scanner.useDelimiter(commaDelimiter);

			while (scanner.hasNext()) {
				System.out.println(scanner.next() + " ");
			}
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	private static void arrangeSchedule() throws IOException {
		FileWriter csvWriter = new FileWriter("src/new.csv");
		Scanner in = new Scanner(System.in);

		System.out.println("Enter Client Name:");
		String client = in.next();
		System.out.println("Enter Start Date: (DD/MM/YYYY)");
		String start = in.next();
		System.out.println("Enter End Date: (DD/MM/YYYY)");
		String end = in.next();
		System.out.println("Enter Vehicle ID:");
		String vehicleID = in.next();
		System.out.println("Enter Driver ID:");
		String driverID = in.next();

		List<List<String>> rows = Arrays.asList(Arrays.asList(client, start, end, vehicleID, driverID));

		for (List<String> rowData : rows) {
			csvWriter.append(String.join(",", rowData));
			csvWriter.append("\n");
		}

		System.out.println("--Confirm new schedule details--");
		System.out.println("Client Name: " + client);
		System.out.println("Start Date: " + start);
		System.out.println("End Date: " + end);
		System.out.println("Vehicle ID: " + vehicleID);
		System.out.println("Driver ID: " + driverID);
		System.out.println("Is this correct Y/N");
		String choice = in.next();

		if (choice.contentEquals("Y")) {
			
			System.out.println("---Complete---");
			csvWriter.flush();
			csvWriter.close();	
		}
		else if (choice.contentEquals("N")) {
			System.out.println("--Schedule Cancelled--");
		}

		managerMenu();

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
}
