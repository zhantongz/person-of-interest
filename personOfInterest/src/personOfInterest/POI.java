package personOfInterest;

/**
 * Persons of Interest
 * 
 * ref: http://docs.oracle.com/javase/tutorial/java/javaOO/index.html
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-15 UTC
 */
public class POI extends Person {
	/** serialization id */
	private static final long serialVersionUID = 9179990849010679531L;
	/** true is the POI is criminal; false otherwise */
	boolean ifCriminal;
	/** POI's id number */
	int id;
	/** area of a POI */
	int area;

	/**
	 * Construct a POI with a name specifying if he/she is a criminal
	 * 
	 * @param inName
	 *            POI's name
	 * @param criminality
	 *            true if the POI is a criminal; false otherwise
	 */
	public POI(String inName, boolean criminality) {
		name = inName;
		ifAlive = true;
		id = Game.randomID();
	}

	/**
	 * Get POI's ID
	 */
	public int getID() {
		return this.id;
	}
}
