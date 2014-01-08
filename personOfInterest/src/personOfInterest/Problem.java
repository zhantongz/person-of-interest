package personOfInterest;

import java.io.Serializable;

/**
 * Problem that will occur in a mission
 * 
 * ref: http://docs.oracle.com/javase/tutorial/java/javaOO/index.html
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-17 UTC
 */
public class Problem implements Serializable {
	/** serialization id */
	private static final long serialVersionUID = 4395782311889272031L;
	/** mission that contains a problem */
	Mission mission;
	/** a problem's name */
	String name;
	/** details of a problem */
	String description;
	/** a problem's answer; might be integer, string, boolean, etc. */
	Object answer;
	/**
	 * type of a problem's answer; 0 for int, 1 for double, 2 for boolean, 3 for
	 * String
	 */
	int type;
	/** location where a problem occurred */
	Location location;
	/** value of a problem in points (promotion system) */
	int points;
	/** maximum chances to answer a problem */
	int chances;
	/** message after good answer */
	String goodMessage;
	/** days that a mission used */
	int days;
	/** punishment */
	String punishment;

	/**
	 * Construct a problem
	 * 
	 * @param inName
	 *            problem's name
	 * @param inDesc
	 *            problem's description
	 * @param type
	 *            type of problem answer
	 * @param answer
	 *            problem's answer
	 */
	public Problem(String inName, String inDesc, int inType, Object inAnswer, int inPoints, int inChances) {
		name = inName;
		description = inDesc;
		answer = inAnswer;
		type = inType;
		points = inPoints;
		chances = inChances;
	}

	/**
	 * Check if a response from a source is correct
	 * 
	 * @param response
	 *            the response from the user
	 * @return if the response is consistent with the answer
	 */
	public boolean ifCorrect(Object response) {
		return response.equals(answer);
	}

	/**
	 * Get a problem's type
	 * 
	 * @return the type of the problem
	 */
	public String getType() {
		switch (this.type) {
		case 0: // int
			return "INTEGER";
		case 1: // double
			return "DECIMAL NUMBER";
		case 2: // boolean
			return "TRUE OR FALSE";
		case 3: // string
			return "WORD(S)";
		default:
			return "";
		}
	}

	/**
	 * Punishment for wrong answers
	 */
	public boolean punishment() {
		String[] puns = this.punishment.split("]");
		for (int i = 0; i < puns.length; i++) {
			String pun = puns[i];
			String[] detail = pun.split("<");
			if (Game.takeChance(Double.parseDouble(detail[0]))) {
				System.out.println(Game.addLinebreaks(Mission.replaceName(detail[1], this.mission)));
				int delay = Game.randomNum(Integer.parseInt(detail[2]), Integer.parseInt(detail[3]));
				if (delay < 100) {
					try {
						Game.passDays(delay);
						this.mission.player.passDays(delay);
					} catch (ArrayIndexOutOfBoundsException e) {
						for (int j = 0; j < this.mission.calendar.length; j++)
							this.mission.calendar[j] = 1;
					}
				} else if (delay == 110) {// code for game over
					System.out.println("GAME OVER");
					System.exit(0);
				} else if (delay == 911)
					for (int j = 0; j < this.mission.calendar.length; j++)
						this.mission.calendar[j] = 1;
				return true;
			}
		}
		return false;
	}
}
