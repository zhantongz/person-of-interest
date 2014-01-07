package personOfInterest;

/**
 * Agent of the organization
 * 
 * ref: http://docs.oracle.com/javase/tutorial/java/javaOO/index.html
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-13 UTC
 */
public class Agent extends Person {
	/** serialization id */
	private static final long serialVersionUID = -3828196555631548910L;
	/** base location of an agent */
	Location base;
	/** an agent's id number */
	int id;
	/** rank of an agent */
	int rank;
	/**
	 * ranks' list
	 *
	 * ref:
	 * http://java.about.com/od/understandingdatatypes/a/Using-Constants.htm
	 */
	public final static String[] RANKS = {"", "Constable", "Corporal", "Sergeant", "Staff Sergeant", "Sergeant Major",
		"Inspector", "Superintendent", "Deputy Commissioner", "Commissioner"};
	/** calendar of an agent */
	int[] calendar;
	/** current day in the calendar */
	int currentDay;

	/**
	 * Construct a new agent with specified name, rank and base location and a
	 * random id
	 * 
	 * @param inName
	 *            specified name for the new agent
	 * @param inRank
	 *            specified rank for the new agent
	 * @param inBase
	 *            specified base location for the new agent
	 */
	public Agent(String inName, int inRank, Location inBase) {
		this(inName, inRank, inBase, Game.randomID());
	}

	/**
	 * Construct a new agent with specified name, rank and base location and a
	 * random id
	 * 
	 * @param inName
	 *            specified name for the new agent
	 * @param inRank
	 *            specified rank for the new agent
	 * @param inBase
	 *            specified base location for the new agent
	 * @param inId
	 *            specified id number for the new agent
	 */
	public Agent(String inName, int inRank, Location inBase, int inId) {
		name = inName;
		if (inRank <= 9 && inRank >= 1)
			rank = inRank;
		else
			System.out.println("Rank out of range.");
		base = inBase;
		ifAlive = true;

		this.id = inId;
	}

	/**
	 * Display an agent's info including rank, name, base and ID
	 */
	public void displayInfo() {
		System.out.println(RANKS[this.rank] + " " + this.name);
		System.out.println("Base: " + this.base.name + ", " + this.base.country);
		System.out.println("Special Agent #" + this.id);
	}

	/**
	 * Reset calendar of an agent
	 */
	public void resetCal() {
		this.calendar = null;
		this.currentDay = 0;
	}

	/**
	 * Reset calendar of an agent to a certain period
	 */
	public void resetCal(int days) {
		this.calendar = new int[days];
		this.currentDay = 0;
	}

	/**
	 * Pass a certain number of days
	 * 
	 * @param days
	 *            the number of days passed
	 */
	public void passDays(int days) {
		for (int i = 0; i < days; i++)
			this.passDay();
	}

	/**
	 * Pass current day
	 */
	public void passDay() {
		this.calendar[this.currentDay] = 1;
		this.currentDay++;
	}
}