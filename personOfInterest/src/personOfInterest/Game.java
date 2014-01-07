package personOfInterest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Main game program
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-10 UTC
 */
public class Game {
	/* general tools and variables */
	/** scanner that reads from keyboard */
	static Scanner input = new Scanner(System.in);
	/** saved path for saving game */
	static String savedPath = null;
	/** saved name for saving game */
	static String savedName = null;
	/** game calendar  */
	static Calendar gameCal = Calendar.getInstance();
	static int[] locArea = new int[8];
	static int[] poiArea = new int[8];
	/** if the player wants to start a new mission */
	static boolean newMission = false;

	/*
	 * methods for updating the game - start
	 * 
	 * Modified from:
	 * http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	 */
	/**
	 * Update location information from the csv file
	 */
	public static void locs() throws NumberFormatException, IOException, InterruptedException {
		String file = "res/locs/locs.csv";
		BufferedReader br = null;
		String line = "";
		String delimiter = "=";
		br = new BufferedReader(new FileReader(file));
		int index = 1;
		int currentArea = 0;
		while ((line = br.readLine()) != null) {
			String[] locInfo = line.split(delimiter);
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/locs/" + index + ".loc"));
			Location loc = new Location(locInfo[0],
					locInfo[1],
					locInfo[2],
					Integer.parseInt(locInfo[3]),
					addLinebreaks(locInfo[4]));
			output.writeObject(loc);
			output.close();

			if (loc.area != currentArea) {
				currentArea = loc.area;
				locArea[currentArea] = index;
			}

			index++;
		}
		locArea[7] = index;
		br.close();

		System.out.println("Locations Updated");
	}

	/**
	 * Update location information from the csv file
	 */
	public static void pois() throws NumberFormatException, IOException, InterruptedException {
		String file = "res/pois/pois.csv";
		BufferedReader br = null;
		String line = "";
		String delimiter = "=";
		br = new BufferedReader(new FileReader(file));
		int index = 1;
		int currentArea = 0;
		while ((line = br.readLine()) != null) {
			String[] poiInfo = line.split(delimiter);
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/pois/" + index + ".poi"));
			POI poi = new POI(poiInfo[0], toBoolean(Integer.parseInt(poiInfo[2])));
			poi.area = Integer.parseInt(poiInfo[1]);
			output.writeObject(poi);
			output.close();

			if (poi.area != currentArea) {
				currentArea = poi.area;
				poiArea[currentArea] = index;
			}

			index++;
		}
		poiArea[7] = index;
		br.close();

		System.out.println("POIs Updated");
	}

	/**
	 * Update location information from the csv file
	 */
	public static void msts() throws NumberFormatException, IOException, InterruptedException {
		String file = "res/msts/msts.csv";
		BufferedReader br = null;
		String line = "";
		String delimiter = "=";
		br = new BufferedReader(new FileReader(file));
		int index = 0;

		while ((line = br.readLine()) != null)
			index++;
		br.close();

		br = new BufferedReader(new FileReader(file));
		String[] msts = new String[index];
		index = 0;
		while ((line = br.readLine()) != null) {
			String[] mstInfo = line.split(delimiter);
			// System.out.println(mstInfo[0]);
			msts[index] = mstInfo[0];
			index++;
		}
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/msts/msts.mst"));
		output.writeObject(msts);
		output.close();

		br.close();

		System.out.println("Missions Updated");
	}

	/**
	 * Update problems
	 */
	public static void prbs() throws IOException {
		for (int rank = 1; rank < 7; rank++) {
			String file = "res/prbs/" + rank + ".csv";
			BufferedReader br = null;
			String line = "";
			String delimiter = "=";
			br = new BufferedReader(new FileReader(file));
			int index = 1;
			while ((line = br.readLine()) != null) {
				String[] prbInfo = line.split(delimiter);
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("res/prbs/" + rank + "/"
						+ index + ".prb"));
				// System.out.println(poiInfo[0]);
				Problem prb = null;
				switch (Integer.parseInt(prbInfo[2])) {
				case 0: // int
					prb = new Problem(prbInfo[0],
							prbInfo[1],
							Integer.parseInt(prbInfo[2]),
							Integer.parseInt(prbInfo[3]),
							Integer.parseInt(prbInfo[4]),
							Integer.parseInt(prbInfo[5]));
					break;
				case 1: // double
					prb = new Problem(prbInfo[0],
							prbInfo[1],
							Integer.parseInt(prbInfo[2]),
							Double.parseDouble(prbInfo[3]),
							Integer.parseInt(prbInfo[4]),
							Integer.parseInt(prbInfo[5]));
					break;
				case 2: // boolean
					if (prbInfo[3].equals("true"))
						prb = new Problem(prbInfo[0],
								prbInfo[1],
								Integer.parseInt(prbInfo[2]),
								true,
								Integer.parseInt(prbInfo[4]),
								Integer.parseInt(prbInfo[5]));
					else if (prbInfo[3].equals("false"))
						prb = new Problem(prbInfo[0],
								prbInfo[1],
								Integer.parseInt(prbInfo[2]),
								false,
								Integer.parseInt(prbInfo[4]),
								Integer.parseInt(prbInfo[5]));
					break;
				case 3: // string
					prb = new Problem(prbInfo[0],
							prbInfo[1],
							Integer.parseInt(prbInfo[2]),
							prbInfo[3],
							Integer.parseInt(prbInfo[4]),
							Integer.parseInt(prbInfo[5]));
					break;
				}
				prb.goodMessage = prbInfo[6];
				prb.punishment = prbInfo[7];
				prb.days = Integer.parseInt(prbInfo[8]);
				output.writeObject(prb);
				output.close();
				index++;
			}
			br.close();
		}

		System.out.println("Problems Updated");
	}

	/* methods for updating the game - end */

	/**
	 * Cast int to boolean
	 * 
	 * @param num
	 *            the int ready to be casted
	 * @return false if the int is 0; true otherwise
	 */
	public static boolean toBoolean(int num) {
		if (num == 0)
			return false;
		return true;
	}

	/**
	 * Determine if the game is over
	 * 
	 * @return true if the player is alive and not completed the game; false
	 *         otherwise
	 */
	public static boolean isGameOver(Player player) {
		if (!player.ifAlive)
			return true;
		if (player.rank >= 7)
			return true;

		return false;
	}

	/**
	 * Generate a 9-digits id number for agent id, mission id, etc.
	 * 
	 * @return the generated 9-digits number
	 */
	public static int randomID() {
		return Math.abs((int) (Math.random() * 1000000000));
	}

	/**
	 * Generate a random number in a certain range
	 * 
	 * @param min
	 *            the minimum for the number, inclusive
	 * @param max
	 *            the maximum for the number, inclusive
	 * @return the generated number
	 */
	public static int randomNum(int min, int max) {
		// ensure the max and min are correct
		if (max < min) {
			int temp = min;
			min = max;
			max = temp;
		}

		return (int) (Math.random() * (max - min + 1) + min);
	}

	/**
	 * Save the game
	 * 
	 * @param player
	 *            the game's player
	 * @return true if the saving is a success; false otherwise
	 */
	public static boolean saveGame(Player player) throws FileNotFoundException, IOException {
		boolean success = true;
		String path = null; // file directory path
		String name = null; // file name
		do {
			success = true;
			try {
				Saved saved = new Saved(player); // saved file as an object
				// get file info
				path = inputLn("Enter the path that you want to save your game to (press enter if the same): ");
				if (path.equals("")) {
					path = savedPath;
					name = savedName;
				} else {
					name = inputLn("Enter the name that you want to save your game to (overwirtten if exists): ");
				}

				// create the directory if not exists
				// ref
				// http://www.roseindia.net/java/beginners/java-create-directory.shtml
				new File(path).mkdir();

				// save the path
				if (savedPath == null || !savedPath.equalsIgnoreCase(path)) {
					savedPath = path;
					savedName = name;
				}

				// output stream that writes the object into a file
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(savedPath + "/" + savedName
						+ ".game"));
				output.writeObject(saved); // write the file
				output.close(); // close the output
			} catch (Exception e) {
				System.err.println("Error. Permission may be denied; path may not be saved before.");
				success = false;
			}
		} while (!success);
		System.out.println("The game is saved successfully to\n"
				+ new File(path + "/" + name + ".game").getCanonicalPath());

		return true;
	}

	/**
	 * 
	 * Process the user's input
	 * 
	 * @param uinput
	 *            the user's input
	 * @param player
	 *            the player
	 */
	public static void processUserInput(String uInput, Player player) throws FileNotFoundException,
			IOException,
			ClassNotFoundException,
			InterruptedException {
		uInput = uInput.toLowerCase();
		if (uInput.equals("s") || uInput.equals("save"))
			saveGame(player);
		else if (uInput.equals("saveas")) {
			// reset saved info
			savedName = null;
			savedPath = null;
			saveGame(player);
		} else if (uInput.equals("q") || uInput.equals("quit"))
			System.exit(0);
		else if (uInput.equals("h") || uInput.equals("help"))
			help();
		else if (uInput.equals("load")) {
			Saved saved = (Saved) loadFile("Enter the path for your saved game: ");
			player = saved.getPlayer();
		} else if (uInput.equals("myinfo")) {
			System.out.println("Today is " + displayDate() + ".");
			player.displayInfo();
		} else if (uInput.equals("date"))
			displayDate();
		else if (uInput.equals("feedback"))
			System.out.println(addLinebreaks("Go to https://github.com/zhantongz/person-of-interest/"
					+ "issues to submit feedback, thank you."));
		else if (uInput.equals("newmission") || uInput.equals("n"))
			newMission = true;
		else if (uInput.equals("newgame"))
			main(null);
		else
			System.out.println("Invalid Input or Wrong Answer, enter h or help for help information.");
	}

	/**
	 * Process the user's input that intends for a problem
	 * 
	 * @param problem
	 *            the problem
	 * @param player
	 *            the player
	 * @return true if the user answers the question correctly; false if answers
	 *         incorrectly or doesn't answer
	 */
	public static boolean processUserInput(Problem problem, Player player) throws IOException,
			ClassNotFoundException,
			InterruptedException {
		String uInput = "";
		boolean correct = false;
		try {
			switch (problem.type) {
			case 0: // int
				uInput = input("Solve the problem:");
				correct = problem.ifCorrect(Integer.parseInt(uInput));
				break;
			case 1: // double
				uInput = input("Solve the problem:");
				correct = problem.ifCorrect(Double.parseDouble(uInput));
				break;
			case 2: // boolean
				uInput = input("Solve the problem:").toLowerCase();
				if (uInput.equals("true"))
					correct = problem.ifCorrect(true);
				else if (uInput.equals("false"))
					correct = problem.ifCorrect(false);
				break;
			case 3: // string
				uInput = inputLn("Solve the problem:").toLowerCase();
				correct = problem.ifCorrect(uInput);
				break;
			}
		} catch (Exception e) {
			correct = false;
		}

		if (!correct)
			processUserInput(uInput, player);

		return correct;
	}

	/**
	 * Display help information
	 */
	public static void help() throws FileNotFoundException {
		File helpFile = new File("res/help.txt");
		Scanner infile = new Scanner(helpFile);
		while (infile.hasNext())
			System.out.println(infile.nextLine());
		infile.close();
	}

	/**
	 * Pause the program for a period of time while outputing three dots ref:
	 * http://docs.oracle.com/javase/tutorial/essential/concurrency/sleep.html
	 * 
	 * @param timeInMs
	 *            the period of time to be paused for
	 */
	public static void sleep(int timeInMs) throws InterruptedException {
		timeInMs /= 3;
		Thread.sleep(timeInMs);
		System.out.print(".");
		Thread.sleep(timeInMs);
		System.out.print(".");
		Thread.sleep(timeInMs);
		System.out.println(".");
	}

	/**
	 * Print SCCI logo
	 */
	public static void scciSeal() {
		System.out.println(" __           _                      __");
		System.out.println("/ _\\ ___  ___| |_ ___  _ __    ___  / _|");
		System.out.println("\\ \\ / _ \\/ __| __/ _ \\| \'__|  / _ \\| |_");
		System.out.println("_\\ \\  __/ (__| || (_) | |    | (_) |  _|");
		System.out.println("\\__/\\___|\\___|\\__\\___/|_|     \\___/|_|");
		System.out.println("   ___     _           _             _     ___            _             _ _ _");
		System.out.println("  / __\\ __(_)_ __ ___ (_)_ __   __ _| |   / __\\___  _ __ | |_ _ __ ___ | | (_)_ __   __ _");
		System.out.println(" / / | \'__| | \'_ ` _ \\| | \'_ \\ / _` | |  / /  / _ \\| \'_ \\| __| \'__/ _ \\| | | | \'_ \\ / _` |");
		System.out.println("/ /__| |  | | | | | | | | | | | (_| | | / /__| (_) | | | | |_| | | (_) | | | | | | | (_| |");
		System.out.println("\\____/_|  |_|_| |_| |_|_|_| |_|\\__,_|_| \\____/\\___/|_| |_|\\__|_|  \\___/|_|_|_|_| |_|\\__, |");
		System.out.println("  _____       _       _ _ _                                                         |___/");
		System.out.println("  \\_   \\_ __ | |_ ___| | (_) __ _  ___ _ __   ___ ___");
		System.out.println("   / /\\/ \'_ \\| __/ _ \\ | | |/ _` |/ _ \\ \'_ \\ / __/ _ \\");
		System.out.println("/\\/ /_ | | | | ||  __/ | | | (_| |  __/ | | | (_|  __/");
		System.out.println("\\____/ |_| |_|\\__\\___|_|_|_|\\__, |\\___|_| |_|\\___\\___|");
		System.out.println("                            |___/");

	}

	/**
	 * Show the start page with ASCII art
	 */
	public static void showStartPage() {
		System.out.println("ooooooooo.   oooooooooooo ooooooooo.    .oooooo..o   .oooooo.   ooooo      ooo");
		System.out.println("`888   `Y88. `888\'     `8 `888   `Y88. d8P\'    `Y8  d8P\'  `Y8b  `888b.     `8\'");
		System.out.println(" 888   .d88\'  888          888   .d88\' Y88bo.      888      888  8 `88b.    8");
		System.out.println(" 888ooo88P\'   888oooo8     888ooo88P\'   `\"Y8888o.  888      888  8   `88b.  8");
		System.out.println(" 888          888    \"     888`88b.         `\"Y88b 888      888  8     `88b.8");
		System.out.println(" 888          888       o  888  `88b.  oo     .d8P `88b    d88\'  8       `888");
		System.out.println("o888o        o888ooooood8 o888o  o888o 8\"\"88888P\'   `Y8bood8P\'  o8o        `8");
		System.out.println();
		System.out.println("    ____  ______   _____ _   _ _______ ______ _____  ______  _____ _______");
		System.out.println("   / __ \\|  ____| |_   _| \\ | |__   __|  ____|  __ \\|  ____|/ ____|__   __|");
		System.out.println("  | |  | | |__      | | |  \\| |  | |  | |__  | |__) | |__  | (___    | |");
		System.out.println("  | |  | |  __|     | | | . ` |  | |  |  __| |  _  /|  __|  \\___ \\   | |");
		System.out.println("  | |__| | |       _| |_| |\\  |  | |  | |____| | \\ \\| |____ ____) |  | |");
		System.out.println("   \\____/|_|      |_____|_| \\_|  |_|  |______|_|  \\_\\______|_____/   |_|");
	}

	/**
	 * Get the user's input with space as the delimiter
	 * 
	 * @param prompt
	 *            prompt for the user
	 * @return the string user entered
	 */
	public static String input(String prompt) throws IOException {
		System.out.println(prompt);
		System.out.print(">> ");
		String data = input.next();
		input.nextLine();
		return data;
	}

	/**
	 * Get the user's input with line break as the delimiter
	 * 
	 * @param prompt
	 *            prompt for the user
	 * @return the string user entered
	 */
	public static String inputLn(String prompt) {
		System.out.println(prompt);
		System.out.print(">> ");
		return input.nextLine();
	}

	/**
	 * Check if the user's input is valid and return a valid data
	 * 
	 * @param prompt
	 *            prompt for the user
	 * @param min
	 *            the minimum value for a valid data
	 * @param max
	 *            the maximum value for a valid data
	 * @return a valid integer data between min and max
	 */
	public static int input(String prompt, int min, int max) {
		// ensure the max and min are correct
		if (max < min) {
			int temp = min;
			min = max;
			max = temp;
		}

		boolean passed;
		int data = min - 1;

		do {
			System.out.println(prompt);
			System.out.print(">> ");
			passed = true;
			try {
				data = input.nextInt();
				if (data < min || data > max) {
					passed = false;
					System.out.println("Invalid range, enter a number between " + min + " and " + max + ".");
				}
			} catch (Exception e) {
				passed = false;
				System.out.println("Invalid type, enter a number between " + min + " and " + max + ".");
				input.next();
			}
		} while (!passed);
		input.nextLine();
		return data;
	}

	/**
	 * Load a serialized file
	 * 
	 * @param prompt
	 *            prompt for the user
	 * @return the object included in the file
	 */
	public static Object loadFile(String prompt) throws IOException {
		boolean errorExists = false;
		Object object = null;
		do {
			errorExists = false;
			try {
				ObjectInputStream inobj = new ObjectInputStream(new FileInputStream(input(prompt)));
				object = inobj.readObject();
				inobj.close();
			} catch (FileNotFoundException e) {
				System.out.println("Error. The file doesn't exist.");
				errorExists = true;
			} catch (ClassNotFoundException e) {
				System.out.println("Error. File type may be wrong.");
				errorExists = true;
			} catch (StreamCorruptedException e) {
				System.out.println("Error. File type may be wrong.");
				errorExists = true;
			} catch (NullPointerException e) {
				errorExists = true;
			}
		} while (errorExists);

		return object;
	}

	/**
	 * Transport an agent from his/her current location to a location requested
	 * through a certain way
	 * 
	 * @param agent
	 *            the agent that need to be transported
	 * @param method
	 *            the way of transportation; 1 for train, 2 for plane, 3 for
	 *            normal vehicle and 4 for high-speed rail
	 * @param from
	 *            the current location
	 * @param to
	 *            the location requested
	 */
	public static void transport(Agent agent, int method, Location from, Location to) {
		double accidentRate = 0;
		double speed = 0;
		int distance = 0;

		switch (method) {
		case 1:
			accidentRate = 0.00001;
			speed = 105;
			break;
		case 2:
			accidentRate = 0.0001;
			speed = 885;
			distance += 90;
			break;
		case 3:
			accidentRate = 0.005;
			speed = 100;
			break;
		case 4:
			accidentRate = 0.0005;
			speed = 300;
			break;
		}

		if (randomNum(1, 100000) < 100000 * accidentRate)
			if (randomNum(1, 10) == 5) {
				System.out.println("Accident happened, you are killed.");
				agent.kill();
			} else {
				int delay = randomNum(0, 3);
				System.out.println("Accident happened, you are injured and your agenda is delayed for " + delay
						+ " day(s).");
				for (int i = 0; i < delay; i++) {
					agent.passDay();
					passDay();
				}
				distance -= 200;
			}

		if (!from.country.equals(to))
			distance += randomNum(1500, 8000);
		else
			distance += randomNum(500, 2000);

		for (int i = 0; i < (int) Math.ceil((distance / speed) / 20); i++) {
			agent.passDay();
			passDay();
		}

		System.out.println("You are now arrived in " + to.name + ".");
		System.out.println(to.description);
	}

	/**
	 * Add a certain number of days to the game calendar
	 * 
	 * @param days
	 *            the certain number of days
	 */
	public static void passDays(int days) {
		gameCal.add(Calendar.DATE, days);
	}

	/**
	 * Add 1 day to the calendar
	 */
	public static void passDay() {
		gameCal.add(Calendar.DATE, 1);
	}

	/**
	 * Display the date today (as in the game)
	 * 
	 * @return the current date in the game
	 */
	public static String displayDate() {
		return new SimpleDateFormat("MMMMM d, yyyy").format(gameCal.getTime());
	}

	/**
	 * Determine if a player complete his/her mission in a certain time
	 * 
	 * @param calendar
	 *            the calendar of the player/mission
	 * @return true if the player doesn't fail to comply the calendar; false
	 *         otherwise
	 */
	public static boolean ifGoodCalendar(int[] calendar) {
		for (int i = 0; i < calendar.length; i++)
			if (calendar[i] != 1)
				return true;
		return false;
	}

	/**
	 * Break lines in a long string to make display nicer
	 * 
	 * ref: http://stackoverflow.com/questions/7528045/large-string-split-into-
	 * lines-with-maximum-length-in-java
	 * 
	 * @param input
	 *            the string need to be broken
	 * @param maxLineLength
	 *            the maxium line length to insert break line
	 * @return the string with proper breaks
	 */
	public static String addLinebreaks(String input, int maxLineLength) {
		StringTokenizer tok = new StringTokenizer(input, " ");
		StringBuilder output = new StringBuilder(input.length());
		int lineLen = 0;
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken() + " ";

			if (lineLen + word.length() > maxLineLength) {
				output.append("\n");
				lineLen = 0;
			}
			output.append(word);
			lineLen += word.length();
		}
		return output.toString();
	}

	public static boolean takeChance(int divisor) {
		return randomNum(1, divisor) == divisor;
	}

	public static boolean takeChance(double chance) {
		return 100 * chance >= randomNum(1, 100);
	}

	/**
	 * Show a string in typewriter style
	 * 
	 * @param message
	 *            the string need to be displayed
	 */
	public static void typeString(String message) throws InterruptedException {
		char[] mes = message.toCharArray();
		for (int i = 0; i < mes.length; i++) {
			System.out.print(mes[i]);
			if (!(mes[i] == ' ') || (i < mes.length - 1 && mes[i + 1] == ' '))
				Thread.sleep(50);
		}
		System.out.println();
	}

	/**
	 * Break lines in a long string with maximum length of 80
	 * 
	 * @param input
	 *            the string need to be broken
	 * @return the string with proper breaks
	 */
	public static String addLinebreaks(String input) {
		return addLinebreaks(input, 80);
	}

	/**
	 * Main program
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// update the game from resource files
		locs();
		pois();
		msts();
		prbs();

		// start the game
		showStartPage(); // show the start
		Player player = null; // game's player

		boolean started = false; // if the player has started
		do {
			started = false; // reset to false
			System.out.println("1. Start a new game;");
			System.out.println("2. Load a saved game;");
			System.out.println("3. Read the instruction;");
			System.out.println("0. Exit.");

			// initialization
			gameCal.add(Calendar.DATE, randomNum(-256, 256));
			gameCal.add(Calendar.YEAR, randomNum(25, 100));

			switch (input("Choose your option (0 - 3):", -1, 3)) {
			case -1: // update problems mode
				main(null);
				break;
			case 0:
				System.exit(0);
				break;
			case 1: // new game
				// set base location
				Location base = null;
				int baseIndex = 0;
				switch (input("Choose your base location (1 - 3):\n"
						+ "\t1. Harbin, China (Asia-Pacific and Middle East)\n" + "\t2. Calgary, Canada (Americas)\n"
						+ "\t3. Lyon, France (Europe and Africa)", 1, 3)) {
				case 1:
					baseIndex = 5;
					break;
				case 2:
					baseIndex = 24;
					break;
				case 3:
					baseIndex = 49;
				}
				ObjectInputStream inloc = new ObjectInputStream(new FileInputStream("res/locs/" + baseIndex + ".loc"));
				base = (Location) inloc.readObject();
				System.out.println(base.description);

				// set player's name
				player = new Player(inputLn("Enter your name:"), 1, base);
				started = true; // the player has started the game
				break;
			case 2: // load saved game
				Saved saved = (Saved) loadFile("Enter the path for your saved game: ");
				player = saved.getPlayer();
				started = true; // the player has started the game
				break;
			case 3:
				help();
			}
		} while (!started);
		player.displayInfo();
		System.out.println();
		/*
		 * typeString(addLinebreaks(" " +
		 * "SCCI is responsible for security and safety of people by analyzing intelligence from sources all over the world. "
		 * +
		 * "Our analysts will provide the profile(s) of Person(s) of Interest (POI) who will be victim(s) or criminal(s). "
		 * +
		 * "Special agents of the organization like you will prevent violent crime from the beginning. "
		 * +
		 * "Your role is to find and monitor POI, in the event of crime, stop it, help victim(s) and arrest criminal(s)."
		 * ) +
		 * "\n\tIf you have any questions about the operation, enter h or help to obtain\n"
		 * + "help information.\n" + "\tGood luck on your mission.\n" +
		 * "\t- Director of the Sector of Criminal Controlling Intelligence");
		 */System.out.print("Enter any key to start your first mission:\n>>");
		player.location = player.base;
		input.nextLine();
		scciSeal();
		sleep(3000);

		// start the game loop
		do {
			newMission = false;
			Mission mission = Mission.generateMission(player.base.area, player.rank);
			if (player.rank == 6) {
				mission = Mission.generateFinalMission(player.base.area);
			}
			mission.player = player;
			System.out.print("Analyzing intelligence.");
			sleep(2400);
			System.out.print("Potential Criminal Involver:");
			sleep(1500);
			System.out.println("ID: " + mission.poi.id);
			System.out.print("Creating emergency mission.");
			sleep(2000);
			System.out.println("--------------------------------------------------");
			System.out.println("Mission #" + (player.missionCompleted + 1));
			System.out.println(mission.name);
			System.out.println("Location: " + mission.location.name + ", " + mission.location.country);
			System.out.println("Person of Interest: " + mission.poi.name);
			System.out.println("Time Permitted: " + mission.calendar.length + " days");
			System.out.println("Potential Difficulties and Dangers: ");
			System.out.println(mission.dangers);
			System.out.println("--------------------------------------------------");
			sleep(900);
			typeString(mission.description);

			player.calendar = mission.calendar;

			if (inputLn("Type decline to decline this mission, type any other keys to start").equalsIgnoreCase("decline")) {
				System.out.println("Due to the importance of emergency missions, you are now discharged.\n"
						+ "SCCI acknowledges your service and thanks you.\n" + "GAME OVER");
				System.exit(0);
			}

			System.out.println("Today is " + displayDate() + ".");

			transport(player,
					input(addLinebreaks("Choose your transportation, your current location is "
							+ player.location.name
							+ ", you should consider carefully about your transportation so that you can complete mission.")
							+ "\n\t1. Train\t2. Airplane\n" + "\t3. Automobile\t4. High-speed rail",
							1,
							4),
					player.location,
					mission.location);
			System.out.println();
			typeString(addLinebreaks(mission.arriveMessage));

			if (!mission.completing())
				if (randomNum(1, 10) == 5) {
					System.out.println(addLinebreaks("You failed to complete mission in required time, "
							+ "the mission failed as a result. As SCCI need agents who are "
							+ "with great strength and intelligence in order to keep world "
							+ "safe and secure, you are now discharged.\n")
							+ "SCCI acknowledges your service and thanks you.\n"
							+ "SCCI welcomes you to re-apply for agents after training.");
					System.exit(0);
				} else {
					System.out.println(addLinebreaks("You failed to complete mission in required time, "
							+ "however, you still complete the mission. As SCCI need agents "
							+ "with great strength and intelligence in order to keep world "
							+ "safe and secure, you are now demoted in order to get training."));
					if (player.rank > 1)
						player.demote();
					else
						player.points = 5;
				}

			processUserInput(input(""), player);
		} while (!isGameOver(player) && newMission);
	}
}