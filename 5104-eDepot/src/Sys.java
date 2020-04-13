import java.util.Scanner;

public class Sys {
	private static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) {

		boolean running = true;
		boolean loggedIn = false;

		while (running) {
			if (loggedIn) {
				loginMenu();
			} else {
			}
		}

	}

	//menu seen straight away
	public static void loginMenu() {
		String username, password;
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println(" --LOGIN--");
		System.out.println("Enter Username:");
		username = userInput.nextLine();
		System.out.println("Enter Password:");
		password = userInput.nextLine();
	}


//the menu the driver sees
	private void driverMenu() {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View Schedule");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");
		choice = S.nextLine().toUpperCase();

		switch (choice) {
		case "1": {
			viewSchedule();
			break;
		}
		case "Q": {
			// logout
			break;

		}
		}
		while (!choice.equals("Q"))
			;
	}

	// the menu the manager sees
	private void managerMenu() {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View Schedule");
		System.out.println("2. Setup Work Schedule");
		System.out.println("3. Check Availability");
		System.out.println("4. Move Vehicle");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");

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
			checkAvailability();
			break;
		}
		case "4": {
			moveVehicle();
			break;
		}
		}
		while (!choice.equals("Q"))
			;
	}

	//setting the methods
	private void viewSchedule() {
	}

	private void arrangeSchedule() {
	}

	private void checkAvailability() {
	}

	private void moveVehicle() {
	}
}
