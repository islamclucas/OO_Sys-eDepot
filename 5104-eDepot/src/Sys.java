//Import relevant frameworks and external files
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import depot.User;
import depot.Vehicle;
import depot.WorkSchedule;

public class Sys {
	private static boolean loggedIn = false; //A variable created to track whether or not a user is signed into the system - initially false
	private static User loggedInUser = null; //A variable used to store the current user - initially null
	private static ArrayList<User> users = new ArrayList<User>(); //Array to store users
	private static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(); //Array to store vehicles
	private static ArrayList<WorkSchedule> workschedules = new ArrayList<WorkSchedule>(); //Array to store workschedules

	private static boolean menuLoop = true;
	private static Scanner userInput = new Scanner(System.in);
	public static String curUser = " ";
	public static String availRegs = " ";
	public static String timeWorks = null;

	// Main Method which stores if a user is logged in

	public static void main(String[] args) throws IOException, ParseException {
		boolean running = true;
		//A while loop that checks if a user is signed in and if so what role they have, taking them to relevant menu
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

	// login menu every non logged in user sees

	public static void loginMenu() throws FileNotFoundException {

		Scanner su = new Scanner(new File("src/users.csv"));
		su.useDelimiter(",");
		//Scan the users CSV file, split the data and then add it to an array
		while (su.hasNextLine()) {
			String s = su.nextLine();
			String[] split = s.split(",");
			User y = new User(split[0], split[1], split[2], split[3], "depot");
			users.add(y);

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
			//Check if username is correct and if so change values of variables to match user data
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

	// Method which displays driver menu if user is a driver
	
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
			//Once user logs out the values are reset
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

	// Method which displays manager menu if user is a manager
	//ASSUMPTION- Managers can view all schedules, and their personal schedule.
	private static void managerMenu() throws IOException, ParseException {
		String choice = "";
		System.out.println("-- DEPOT SYSTEM--");
		System.out.println("1. View All Schedules");
		System.out.println("2. View Personal Schedule");
		System.out.println("3. Setup Work Schedule");
		System.out.println("4. Move Vehicle");
		System.out.println("5. Set up Vehicle");
		System.out.println("6. Set up Driver");
		System.out.println("Q. Log Out");
		System.out.print("Pick : ");
		choice = (userInput.next().toUpperCase());
		switch (choice) {
		case "1": {
			viewSchedule();
			break;
		}
		case "2": {
			personalSchedule();
			break;
		}
		case "3": {
			arrangeSchedule();
			break;
		}
		case "4": {
			moveVehicle();
			break;
		}
		case "5": {
			setupVehicle();
			break;
		}
		case "6": {
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

	// Method which shows a manager all stored schedules

	private static void viewSchedule() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = null;
		int noOutput = 0;
		System.out.println("\n");
		//Schedule CSV is added to an array and all lines are added so that a manager can view all schedules
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

	// Method which outputs a users personal schedule

	private static void personalSchedule() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = null;
		int noOutput = 0;
		//Schedule CSV is added to an array and lines are added that the user is active on a schedule
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
						noOutput++;
					}

				}
			}

		}
		if (noOutput == 0) {
			System.out.println("No Schedule For Current User ");
			reader.close();
		}
	}

	/*
	 * Method which allows a manager to assign a schedule
	 * 
	 * @return boolean true if vehicle is found in vehicle csv, false if not
	 *
	 */
	private static void arrangeSchedule() throws IOException, ParseException {
		Scanner in = new Scanner(System.in);

		Scanner vehicleScanner = new Scanner(new File("src/vehicle.csv"));
		vehicleScanner.useDelimiter(",");
		while (vehicleScanner.hasNextLine()) {
			String s = vehicleScanner.nextLine();
			String[] split = s.split(",");
			Vehicle y = new Vehicle(split[0], split[1], split[2], split[3], split[4], split[5]);
			vehicles.add(y);

		}

		Scanner ss = new Scanner(new File("src/schedule.csv"));
		ss.useDelimiter(",");
		while (ss.hasNextLine()) {
			String s = ss.nextLine();
			String[] split = s.split(",");
			WorkSchedule y = new WorkSchedule(split[0], split[1], split[2], split[3], split[4], split[5], split[6],
					split[7]);
			workschedules.add(y);
		}

		ss.close();
		
		//Vehcile and Schedule CSV files are loaded into appropriate arrays to be referenced later in the code

		System.out.println("Enter Client Name:");
		String client = in.next();

		Date start = new Date();
		System.out.println("Specify Start Date:");
		String startDate = in.next();

		//Add 2 calender days to the start date inputted to ensure start date is 48 hours ahead
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 2);
		start = c.getTime();
		
		//A specified format is set so that the users entered value can be referenced against it
		String dateFormat = "[0-3][0-9]/[0-1][0-9]/20[0-9][0-9]";

		if ((startDate).matches(dateFormat)) {

			//Make sure that the inputted start date is after 48 hours from the current date
			if (new SimpleDateFormat("dd/MM/yyyy").parse(startDate).after(start)) {
				System.out.println("-- Valid Start Date --");

				Date end = new Date();

				System.out.println("Specify End Date:");
				String endDate = in.next();

				if ((endDate).matches(dateFormat)) {
					System.out.println("Valid date");

					Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);

					//72 hours added to valid start date to reference
					Calendar cal = Calendar.getInstance();
					cal.setTime(date1);
					cal.add(Calendar.DATE, 3);

					Date endDate72 = c.getTime();

					Date dateEnd = new Date();
					
					//If the end date is after a period of 72 hours from the start it will not allow the user to continue
					if (new SimpleDateFormat("dd/MM/yyyy").parse(endDate).before(endDate72)) {
						System.out.println("-- End Date Must Be 72 Hours Before Start Date --");
						arrangeSchedule();
					} else {

						System.out.println("Specify Start Time For Entered Start Date(HH:mm):");
						String startTime = in.next();

						//The time entered by the user is checked to see if it matches the required format
						try {
							checkTime(startTime);

						} catch (Exception e) {
							System.out.println(e);
						}
						//If start time is in the correct format
						if (timeWorks != null) {
							System.out.println("Specify End Time For Entered End Date(HH:mm):");
							String endTime = in.next();
							try {
								checkTime(endTime);

							} catch (Exception e) {
								System.out.println(e);
							}
							//If end time is in the correct format
							if (timeWorks != null) {
								for (WorkSchedule workschedules : workschedules) {
									BufferedReader reader = new BufferedReader(new FileReader("src/vehicle.csv"));
									List<String> lines = new ArrayList<>();
									String line1 = null;
									int noOutput = 0;
									System.out.println("\n");
									while ((line1 = reader.readLine()) != null) {
										lines.add(line1 + "\n ");
									}
									
									//All of the vehcile data from vehciles CSV file is displayed on console to allow user to pick a vehicle
									int size = (lines.size());
									if (size != 0) {
										System.out.println("All Vehicles In System:");
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

									System.out.println("Enter Vehicle Type (Truck or Tanker):");
									String vehicleType = in.next();

									//Make sure the user only inputs Truck or Tanker in order for the program to continue
									if (vehicleType.equalsIgnoreCase("Truck")
											|| vehicleType.equalsIgnoreCase("Tanker")) {
										System.out.println("Enter Vehicle Reg:");
										String vehicleReg = in.next();

										boolean value = false;

										Vehicle vehicle = null;
										for (Vehicle v : vehicles) {
											if ((v.getregNo().equals(vehicleReg))) {
												value = true;
												vehicle = v;
											}
										}
										//Check to make sure that the registration entered by the user is for a vehicle that exists
										if (value == true) {
											System.out.println("--Vehicle Found--");

											driversAvailable();

											System.out.println("Enter Driver ID:");
											String driverID = in.next();

											boolean value2 = false;

											User user = null;
											//Make sure that the driver ID is valid before program will continue
											for (User users : users) {
												if ((users.getdriverID().equals(driverID))) {
													value2 = true;
													user = users;
													if (value2 == true) {
														System.out.println("Driver found.");

														System.out.println("Start Date: " + startDate);
														System.out.println("End Date: " + endDate);
														System.out.println("Start time: " + startTime);
														System.out.println(
																"End time: " + endTime + "\nIs this correct? (Y/N)");
														String choice = in.next();
														
														//Allows user to confirm the details
														if (choice.equalsIgnoreCase("Y")) {

															List<WorkSchedule> schedules = new ArrayList<WorkSchedule>();
															try {
																Scanner fileReader = new Scanner(
																		new FileReader("src/schedule.csv"));
																while (fileReader.hasNextLine()) {
																	String line = fileReader.nextLine();
																	String[] splitLine = line.split(",");

																	schedules.add(new WorkSchedule(client, vehicleType,
																			startDate, endDate, startTime, endTime,
																			vehicleReg, driverID));

																	break;
																}
																fileReader.close();

															} catch (Exception e) {
																System.out.println(e);
															}
															//The new schedule is added to the CSV file
															FileWriter csvWriter = new FileWriter("src/schedule.csv",
																	true);

															List<List<String>> rows = Arrays.asList(Arrays.asList(
																	client, vehicleType, startDate, endDate, startTime,
																	endTime, vehicleReg, driverID));

															for (List<String> rowData : rows) {
																csvWriter.append(String.join(",", rowData));
																csvWriter.append(",");
																csvWriter.append("\n");
															}

															csvWriter.flush();
															csvWriter.close();

															System.out.println("-- Schedule Added Successfully --\n");
															managerMenu();
															break;

														} else if (choice.equalsIgnoreCase("N")) {
															System.out.println("-- Schedule Cancelled --");
															arrangeSchedule();
														}

													} 
												}else {
														System.out.println("-- Driver Does Not Exist --");
														arrangeSchedule();

													}

												
											}

										} else {
											System.out.println("-- Vehicle Does Not Exist --");
											arrangeSchedule();

										}

									} else {
										System.out.println("-- Invalid Vehicle Type-- ");
										arrangeSchedule();
									}

								}
							} else if (timeWorks == null) {
								System.out.println("Time Invalid");
								arrangeSchedule();
							}
						} else if (timeWorks == null) {
							System.out.println("Time Invalid");
							arrangeSchedule();
						}
					}
				} else {
					System.out.println("-- Invalid Date --\n");
					arrangeSchedule();
				}

			} else {
				System.out.println("-- Start Date Must Be 48 Hours From Today --");
				arrangeSchedule();
			}
		} else {
			System.out.println("-- Invalid Start Date --\n");
			arrangeSchedule();
		}

	}

	/*
	 * Method checks time is of correct format
	 * 
	 * @return boolean true if time format is correct, false if format is incorrect
	 *
	 */
	private static boolean checkTime(String choiceTime) {
		//A time format created to reference
		String TimeFormat = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
		//Make sure user input matches specified format
		if (choiceTime.matches(TimeFormat)) {
			timeWorks = "works";
			return true;
		}
		timeWorks = null;
		return false;

	}
	// Method outputs driver ID when assigning schedule

	private static void driversAvailable() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader("src/schedule.csv"));
		List<String> lines = new ArrayList<>();
		String line = "";
		//Adds vehcile reg into lines to be outputted
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
					UserDetails.add(UserDriverID[e]);

				}
				e++;
			}
		}
		Collections.sort(lines);
		Collections.sort(Userlines);
		Userlines.removeAll(lines);

		int userSize = UserDetails.size();
		int size = Userlines.size();
		for (int r = 0; r < size; r++) {
			
			//Driver names and IDs are displayed to the user to allow them to choose a correct values
			String curID = Userlines.get(r);
			String drivername = (UserDetails.get(r));
			System.out.println("ID _" + drivername);

			reader.close();
			UserReader.close();
		}
	}

	// Method checks which vehicle registrations aren't already in the schedule

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
					UserDetails.add(UserDriverID[e]);

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

	// private static void truck() throws IOException, ParseException {
	// BufferedReader scheduleReader = new BufferedReader(new
	// FileReader("src/vehicle.csv"));
	// Scanner scannerSchedule = new Scanner(new File("src/vehicle.csv"));
	// List<String> types = new ArrayList<>();
	// List<String> regs = new ArrayList<>();
	// String line = "";
	//
	// while (scannerSchedule.hasNextLine()) {
	// String s = scannerSchedule.nextLine();
	// String[] split = s.split(",");
	// Vehicle x = new Vehicle(split[0], split[1], split[2], split[3], split[4],
	// split[5]);
	// vehicles.add(x);
	//
	// }
	//
	// scannerSchedule.close();
	//
	// String truck = "Truck";
	//
	// boolean value = false;
	//
	// Vehicle vehicle = null;
	// for (Vehicle v : vehicles) {
	// if (v.getvehicleType().equalsIgnoreCase(truck)) {
	//
	// value = true;
	// vehicle = v;
	// }
	// }
	//
	// if (value == true) {
	// for (Vehicle v : vehicles) {
	// if (v.getvehicleType().equalsIgnoreCase("Truck")) {
	// while ((line = scheduleReader.readLine()) != null) {
	// String[] type = line.trim().split(",");
	// for (int w = 0; w < line.length(); w++) {
	// if (!types.contains(type[4])) {
	// types.add(type[4]);
	// }
	// if (!regs.contains(type[3])) {
	// regs.add(type[3]);
	// }
	// w++;
	// break;
	//
	// }
	// }
	//
	// System.out.println("Trucks:");
	// System.out.println(types + " - " + regs);
	// break;
	//
	// } else {
	//
	// System.out.println("-- No Truck Data --");
	// arrangeSchedule();
	// }
	// }
	// }
	//
	// }

	/*
	 * Method which validates if a vehicle is available to move depot
	 * 
	 * @return boolean false if vehicle isn't available on the date, true if
	 * otherwise
	 */
	private static boolean isVehicleAvailable(String vehicleReg, String startDatein, String endDatein)
			throws IOException, ParseException {

		//Inputted dates are changed to date format to allow them to be compared against one another
		Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDatein);
		Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDatein);

		Scanner su = new Scanner(new File("src/schedule.csv"));
		su.useDelimiter(",");
		while (su.hasNextLine()) {
			String s = su.nextLine();
			String[] split = s.split(",");
			WorkSchedule y = new WorkSchedule(split[0], split[1], split[2], split[3], split[4], split[5], split[6],
					split[7]);
			workschedules.add(y);
		}

		su.close();

		for (WorkSchedule workschedule : workschedules) {

			//Checks if the vehicle is being used in entered dates
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

	// method that would be used to check if a driver is available for specific
	// dates

	// private static boolean isDriverAvailable(String driverID, String startDatein,
	// String endDatein)
	// throws IOException, ParseException {
	//
	// Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDatein);
	// Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDatein);
	//
	// Scanner su = new Scanner(new File("src/schedule.csv"));
	// su.useDelimiter(",");
	// while (su.hasNextLine()) {
	// String s = su.nextLine();
	// String[] split = s.split(",");
	// WorkSchedule y = new WorkSchedule(split[0], split[1], split[2], split[3],
	// split[4], split[5], split[6],
	// split[7]);
	// workschedules.add(y);
	// }
	//
	// su.close();
	//
	// for (WorkSchedule workschedule : workschedules) {
	//
	// if (workschedule.getDriverID().equalsIgnoreCase(driverID)) {
	// if ((workschedule.getStartDate().after(startDate) &&
	// (workschedule.getStartDate().before(endDate)))
	// || ((workschedule.getEndDate().after(startDate)
	// && (workschedule.getEndDate().before(endDate))))) {
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	/*
	 * Method which moves a vehicle to another depot
	 * 
	 * @return boolean true if vehicle exists, false if not
	 *
	 */
	
	//ASSUMPTION- vehicle can't be moved on its start date, but can be moved after its end date
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

		//Goes through each line of the CSV fike and adds the registration to an array
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
			
			//Makes sure that the date is in the future
			if (new SimpleDateFormat("dd/MM/yyyy").parse(moveDate).after(new Date())) {

				if (isVehicleAvailable(registrationValue, moveDate, "01/01/3000")) {
					System.out.println("Specify depot you wish to move vehicle to:");
					String newDepot = in.next();
					//Checks that all the entered values are as they want
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

						//Updates the file with new depot
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
					System.out.println("-- Vehicle Currently on Job --");
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

	/*
	 * Method which is used to write a new vehicle to file
	 * 
	 * @return boolean false if vehicle is already in the system, true otherwise
	 */
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

			//Allows user to check if the data they entered is correct
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

				//Writes correct data into the file vehicle CSV
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

	/*
	 * Method which writes a new driver to the file
	 * 
	 * checks if userID is already taken, doesn't allow input if it is
	 */
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
