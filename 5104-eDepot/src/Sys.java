import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
		System.out.println("2. New Schedule");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");
		choice = userInput.nextLine();

		switch (choice) {
		case "1": {
			viewSchedule();
			break;
		}
		case "2": {
			arrangeSchedule();
			break;
		}
		case "Q":
			menuLoop = false;
			loggedIn = false;
			loggedInUser = null;
			break;
		default:
			System.out.println("Invalid choice entered, please try again.");
		}
	}

//else
	// the menu the manager sees
	private static void managerMenu() throws IOException {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View Schedule");
		System.out.println("2. Setup Work Schedule");
		System.out.println("3. Check Availability");
		System.out.println("4. Move Vehicle");
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
				checkAvailability();
				break;

			case "4":
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
		System.out.println("Enter Client ID:");
		String clientID = in.next();

		List<List<String>> rows = Arrays.asList(Arrays.asList(client, start, end, clientID));

		for (List<String> rowData : rows) {
			csvWriter.append(String.join(",", rowData));
			csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();

		System.out.println("---Complete---");

	}

	private static void checkAvailability() {
	}

	private static void moveVehicle() {
		System.out.println("--Move Vehicle--");
		System.out.println("Specify vehicle:");
		// error message
		System.out.println("Invalid Vehicle Code");
		System.out.println("Specify move date:");
		// error message
		System.out.println("Invalid date");
		// error messages
		System.out.println("Vehicle is in active state");
		System.out.println("Vehicle is in pending state");
		// if vehicle isnt active or pending
		System.out.println("Specify depot:");
		System.out.println("Vehicle Move Succesful");

	}
}
