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
	public static Player savedPlayer;
	/** ocation saved */
	public static Location savedLocation;

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

	/**
	 * Get the saved player in a class
	 * 
	 * @return the saved player
	 */
	public Player getPlayer() {
		return savedPlayer;
	}

	/**
	 * Get the saved location in a class
	 * 
	 * @return the saved location
	 */
	public Location getLocation() {
		return savedLocation;
	}
}