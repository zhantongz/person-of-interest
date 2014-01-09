package personOfInterest;

import java.io.Serializable;

/**
 * Saved file for the game
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-12 UTC
 */
public class Saved implements Serializable {
	/** serialization id */
	private static final long serialVersionUID = -364063165363614855L;
	/** player saved */
	public Player savedPlayer;
	/** location saved */
	public Location savedLocation;
	/** mission number */
	public int missionNum;
	/** problem number */
	public int prbNum;

	/**
	 * Construct a Saved class containing player and location
	 * 
	 * @param player
	 *            the player intending to be saved
	 * @param location
	 *            the location intending to be saved
	 */
	public Saved(Player player, Location location) {
		savedPlayer = player;
		savedLocation = location;
	}

	/**
	 * Construct a Saved class containing player only
	 * 
	 * @param player
	 *            the player intending to be saved
	 */
	public Saved(Player player) {
		savedPlayer = player;
		savedLocation = null;
	}
}