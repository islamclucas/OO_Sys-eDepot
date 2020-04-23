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

import depot.User;

public class Sys {
	private static boolean loggedIn = false;
	private static User loggedInUser = null;
	private static ArrayList<User> users = new ArrayList<User>();
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
		while(menuLoop) {
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

	//THE SCHEDULE MANAGERS SEE OF ALL DETAILS
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
						//do nowt
					}
				}
			}
		}
			if(noOutput==0) {
				System.out.println("NO SCHEDULED JOBS");
			
		}
		reader.close();

	}

	private static void arrangeSchedule() throws IOException {
		
		//BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		
		//List<String> lines = new ArrayList<>();
		//String line = null;
		
		//while ((line = reader.readLine()) != null) {
		//    lines.add(line);
		//}

		//System.out.println(lines.get(0));
		
		
		
		
		
		FileWriter csvWriter = new FileWriter("src/schedule.csv",true);
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

		if (choice.equalsIgnoreCase("Y")) {

			System.out.println("---Complete---");
			csvWriter.flush();
			csvWriter.close();
		} else if (choice.equalsIgnoreCase("N")) {
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
