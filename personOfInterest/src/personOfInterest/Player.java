package personOfInterest;

/**
 * Player who play the game in the virtual world
 * 
 * ref: http://docs.oracle.com/javase/tutorial/java/javaOO/index.html
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-10 UTC
 */
public class Player extends Agent {
	/** serialization id */
	private static final long serialVersionUID = -3247273499544130537L;
	/** number of completed missions */
	int missionCompleted;
	/** a player's points used in promotion system */
	int points;

	/**
	 * Construct a player with a name, a rank and a base
	 * 
	 * @param inName
	 *            specified name
	 * @param inRank
	 *            specified rank
	 * @param inBase
	 *            specified base
	 */
	public Player(String inName, int inRank, Location inBase) {
		super(inName, inRank, inBase);
		this.missionCompleted = 0;
	}

	/**
	 * Promote a player's rank according to his/her points
	 */
	public void promote() {
		if (this.points >= 100) { // corporal
			this.rank = 2;
			if (this.points >= 150) { // sergeant
				this.rank++;
				if (this.points >= 220) { // staff sergeant
					this.rank++;
					if (this.points >= 320) { // sergeant major
						this.rank++;
						if (this.points >= 500) { // inspector
							this.rank++;
							if (this.points > 1000) // superintendent
								this.rank++;
						}
					}
				}
			}
		}
	}

	/**
	 * Demote a player
	 */
	public void demote() {
		this.rank--;
		switch (rank) {
		case 1:
			this.points = 20;
			break;
		case 2:
			this.points = 100;
			break;
		case 3:
			this.points = 150;
			break;
		case 4:
			this.points = 220;
			break;
		case 5:
			this.points = 320;
			break;
		case 6:
			this.points = 500;
			break;
		}
	}
}