package personOfInterest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

/**
 * Mission of the player
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-15 UTC
 */
public class Mission {
	/** location that a mission takes place */
	Location location;
	/** name of a mission */
	String name;
	/** person of interest in a mission */
	POI poi;
	/** if a mission is completed */
	boolean completed;
	/** rank that have abilities to complete a mission */
	int rank;
	/** money that can be used to complete a mission */
	int budget;
	/** calendar of a mission; time permitted */
	int[] calendar;
	/** points that awarded after completion */
	int points;
	/** player who is completing a mission */
	Player player;
	/** potential dangers of a mission */
	String dangers;
	/** description of a mission */
	String description;
	/** situations in a mission */
	Problem[] problems;
	/** message displayed after arrival */
	String arriveMessage;
	/** indication of where minor situations happens */
	int[] minorNum;

	/**
	 * 
	 * Construct a mission
	 * 
	 * @param person
	 *            the POI in a mission
	 * @param inname
	 *            the name of a mission
	 * @param inloc
	 *            the location of a mission
	 * @param days
	 *            the time limit in days for the mission
	 * @param inPoints
	 *            the points required to complete the mission
	 */
	public Mission(POI person, String inname, Location inloc, int days, int inPoints) {
		name = inname;
		poi = person;
		location = inloc;
		completed = false;
		points = inPoints;
		calendar = new int[days];
	}

	/**
	 * Generate a random mission
	 * 
	 * @param area
	 *            the area of the mission
	 * @param rank
	 *            the rank that the mission is designed for
	 * @return generated mission object
	 */
	public static Mission generateMission(int area, int rank) throws FileNotFoundException,
	IOException,
	ClassNotFoundException {
		ObjectInputStream inloc = new ObjectInputStream(new FileInputStream("res/locs/"
				+ (Game.randomNum(Game.locArea[area], Game.locArea[area + 2] - 1)) + ".loc"));
		Location misLoc = (Location) inloc.readObject();
		inloc.close();

		ObjectInputStream inpoi = new ObjectInputStream(new FileInputStream("res/pois/"
				+ (Game.randomNum(Game.poiArea[area], Game.poiArea[area + 2] - 1)) + ".poi"));
		POI poi = (POI) inpoi.readObject();
		inpoi.close();
		if (rank == 1)
			return new Mission1(poi, "", misLoc, Game.randomNum(10, 15), 40);
		return null;
	}

	/**
	 * Complete a mission
	 * 
	 * @param player
	 *            the player who completed the mission
	 */
	public void complete() {
		this.completed = true;
		this.player.missionCompleted++;
	}

	/**
	 * Generate a final Mission
	 * 
	 * @param area
	 *            the area of the mission
	 * @return the generated final mission
	 */
	public static Mission generateFinalMission(int area) throws FileNotFoundException,
	IOException,
	ClassNotFoundException {
		ObjectInputStream inloc = new ObjectInputStream(new FileInputStream("res/locs/"
				+ (Game.randomNum(Game.locArea[area], Game.locArea[area + 2] - 1)) + ".loc"));
		Location misLoc = (Location) inloc.readObject();
		inloc.close();

		File[] finalPOIs = new File("res/pois/final/").listFiles();
		ObjectInputStream inpoi = new ObjectInputStream(new FileInputStream(finalPOIs[Game.randomNum(0,
				finalPOIs.length)]));
		POI poi = (POI) inpoi.readObject();
		inpoi.close();

		return new Mission(poi, "Grande Finale", misLoc, 180, 500);
	}

	/**
	 * Process a mission
	 * 
	 * @return	true if complete the mission; false otherwise
	 */
	public boolean completing() throws InterruptedException, ClassNotFoundException, IOException {
		int prbNum = 0;
		while (!this.completed) {
			System.out.println("\nToday is " + Game.displayDate() + ".");

			// mission problems
			Problem currentProblem = this.problems[prbNum];
			currentProblem.mission = this;
			System.out.println("You have a situation now: ");
			System.out.println("Situation #" + (prbNum + 1) + " " + currentProblem.name);

			int wrongCount = 0;
			boolean correct = false;
			Game.typeString(Game.addLinebreaks(replaceName(currentProblem.description, this)));
			do {
				if (wrongCount % 3 == 0 && wrongCount != 0)
					System.out.println(replaceName(currentProblem.description, this));

				wrongCount++;
				if (wrongCount > currentProblem.chances)
					currentProblem.punishment();
				else {
					System.out.println("The answer is [a(n)] " + currentProblem.getType() + ".");
					correct = Game.processUserInput(currentProblem, player);
				}
			} while (!correct && wrongCount <= currentProblem.chances);
			if (correct) {
				player.points += currentProblem.points;
				Game.typeString(Game.addLinebreaks(replaceName(currentProblem.goodMessage, this)));
				for (int i = 0; i < minorNum.length; i++)
					if (prbNum + 1 == this.minorNum[i])
						this.minorSituation(this.player.rank, i + 1);
			}
			Game.passDays(currentProblem.days);
			player.passDays(currentProblem.days);

			player.promote();
			if (!Game.ifGoodCalendar(this.calendar))
				return false;

			prbNum++;
			if (prbNum == this.problems.length) {
				this.complete();
				this.player.points += this.points;
				return true;
			}
		}
		return completed;
	}

	/**
	 * Replace certain tags in a string with certain variables
	 * 
	 * @param s
	 *            the string need to be replaced
	 * @param mission
	 *            the mission that indicate variables
	 * @return the string that the certain tags are replaced
	 */
	public static String replaceName(String s, Mission mission) {
		String[] divide = s.split("\\|");
		if (divide.length != 0) {
			for (int i = 0; i < divide.length; i++) {
				if (divide[i].equals("POI"))
					divide[i] = mission.poi.name;
				if (divide[i].equals("LocMst"))
					divide[i] = mission.location.name;
			}
			String result = "";
			for (int i = 0; i < divide.length; i++)
				result += divide[i];
			return result;
		}
		return s;
	}
	
	public Mission(POI person, String inname, Location inloc, int days, int inPoints, int rank)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		this(person, inname, inloc, days, inPoints);
		this.rank = 1;
		File[] caughtPrbs = new File("res/prbs/1/").listFiles();
		this.problems = new Problem[caughtPrbs.length];

		for (int i = 0; i < caughtPrbs.length; i++) {
			ObjectInputStream inprb = new ObjectInputStream(new FileInputStream("res/prbs/1/" + (i + 1) + ".prb"));
			this.problems[i] = (Problem) inprb.readObject();
			inprb.close();
		}

		File file = new File("res/msts/" + rank + ".csv");
		Scanner inmst = new Scanner(file);
		String[] infos = inmst.next().split("=");
		inmst.close();

		this.name = infos[0];

		this.dangers = Game.addLinebreaks(replaceName(infos[1], this));
		this.description = Game.addLinebreaks(replaceName(infos[2], this));
		this.arriveMessage = Game.addLinebreaks(replaceName(infos[3], this));
		this.minorNum = new int[] {Integer.parseInt(infos[4])};
	}

	/**
	 * Execute a customized minor situation
	 * 
	 * @param index
	 *            the index of the minor situation
	 */
	public void minorSituation(int rank, int index) throws InterruptedException {
		switch (rank) {
		case 1:
			switch (index) {
			case 1:
				Game.typeString(Game.addLinebreaks("Your movement in a private residence causes"
						+ " a neighbour to call the police. A police officer is asking you to show your ID"
						+ " and explain your purpose. What should you do?"));
				switch (Game.input("1: Show your SCCI badge and told him you are on duty;\n"
						+ "2: Tell him you are here by accident, you thought this is your grandma's house;\n"
						+ "3: Get him inside and sedate the officer.", 1, 3)) {
						case 1:
							if (Game.takeChance(8)) {
								System.out.println("A dirty police informs " + this.poi.name + ".");
								if (Game.takeChance(2)) {
									System.out.println("The mission continues with a delay of 2 days.");
									Game.passDays(2);
									player.passDays(2);
								} else {
									System.out.println("The mission failed.");
									for (int i = 0; i < this.calendar.length; i++)
										this.calendar[i] = 1;
								}
							} else if (Game.takeChance(2)) {
								System.out.println(Game.addLinebreaks("You are arrested for impersonation of an officer of law as the police don't know this operation."
										+ "You are detained for 1 day until your ID is verified. Your rank credit is cleared for your misdoing."));
								Game.passDay();
								player.passDays(1);
								player.points = 0;
							} else
								System.out.println("The officer accepts your badge and leaves.");
							break;
						case 2:
							if (Game.takeChance(2)) {
								System.out.println("The officer arrests you for invasion of private property.");
								if (Game.takeChance(7)) {
									System.out.println("You are charged and prosecuted, GAME OVER.");
									System.exit(0);
								} else
									System.out.println("You are released after paying $200 of fine.");
							} else
								System.out.println("The officer reminds you of local laws and asks you to leave.");
							break;
						case 3:
							if (Game.takeChance(2))
								System.out.println("The officer forgets everything.");
							else {
								System.out.println("The officer shoots you for self-defence, GAME OVER.");
								System.exit(0);
							}
							break;
				}
			}
		}
	}
}
