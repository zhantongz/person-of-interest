package personOfInterest;

import java.io.Serializable;

/**
 * Person including POI, agents of the organization, and others
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-12 UTC
 */
public class Person implements Serializable {
	/** serialization id */
	private static final long serialVersionUID = -2117345102201788590L;
	/** person's name */
	String name;
	/** person's living status */
	boolean ifAlive;
	/** person's location */
	Location location;

	/**
	 * Display a person's information
	 */
	public void displayInfo() {
		System.out.println(this.name);
		System.out.println("Location: " + this.location.name + ", " + this.location.country);
	}

	/**
	 * Kill a person
	 */
	public void kill() {
		this.ifAlive = false;
	}
}