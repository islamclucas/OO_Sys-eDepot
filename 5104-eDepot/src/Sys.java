import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import depot.User;

public class Sys {
	private static boolean loggedIn = false;
	private static User loggedInUser = null;
	private static ArrayList<User> users = new ArrayList<User>();
	private static boolean menuLoop = true;
	private static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		boolean running = true;

		//displaying the correct menus
		while (running) {
			if (!loggedIn) {
				loginMenu();
			} else {
				if (loggedInUser.getrole().equals("Driver")) {
					driverMenu();

				}
				else if (loggedInUser.getrole().equals("Manager")) {
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
			 //System.out.println(user.getpassword());
			if ((user.getusername().equals(username)) && (user.getpassword().equals(password))) {


				loggedIn = true;
				loggedInUser = user;
			}
		}
	}

//the menu the driver sees
	private static void driverMenu() {
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
	private static void managerMenu() {
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

	private static void arrangeSchedule() {
		System.out.println("Specify client name:");
		String clientName, startDate, endDate, vehicleCode, driverCode;
		clientName = userInput.nextLine();
		System.out.println("Specify start date:");
		startDate = userInput.nextLine();
		System.out.println("Specify end date:");
		endDate = userInput.nextLine();

		System.out.println("Specify vehicle code:");
		vehicleCode = userInput.nextLine();

		// error message
		System.out.println("Invalid Vehicle Code");

		System.out.println("Specify driver code:");
		driverCode = userInput.nextLine();
		// error message
		System.out.println("Invalid Driver Code");

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
