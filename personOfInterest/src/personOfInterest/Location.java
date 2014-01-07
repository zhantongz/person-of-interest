package personOfInterest;

import java.io.Serializable;

/**
 * Location
 * 
 * @author ZHANG, Zhantong <zhantongz@gmail.com>
 * @version 1.0
 * @since 2013-12-10 UTC
 */
public class Location implements Serializable {
	/** serialization id */
	private static final long serialVersionUID = -2479125786089738935L;
	/** a location's name */
	String name;
	/** a location's area as the index in AREAS */
	int area;
	/** city that a location situated */
	String city;
	/** country that a location situated */
	String country;
	/** locations that are in a location */
	Location[] sublocations;
	/** the number of sublocations */
	int subNum;
	/** description of a location */
	String description;
	/** areas that a location can be included in */
	static final String[] AREAS = {"Antarctica", "Asia-Pacific", "Middle East", "North America", "South America",
		"Europe", "Africa"};

	/**
	 * Construct a location with name.
	 * 
	 * @param inName
	 *            the location's name
	 */
	public Location(String inName, String inCity, String inCountry, int inArea, String inDescription) {
		name = inName;
		city = inCity;
		country = inCountry;
		area = inArea;
		description = inDescription;
		subNum = 0;
	}

	/**
	 * Add a sublocation
	 * 
	 * @param subloc
	 *            the sublocation that needs to be added
	 */
	public void addSublocation(Location subloc) {
		this.subNum++;
		Location[] temp = this.sublocations;
		this.sublocations = new Location[subNum];
		for (int i = 0; i < this.subNum - 1; i++)
			sublocations[i] = temp[i];
		this.sublocations[subNum - 1] = subloc;
	}

	/**
	 * Add a series of sublocation
	 * 
	 * @param sublocs
	 *            the sublocations that needs to be added
	 */
	public void addSublocations(Location[] sublocs) {
		for (int i = 0; i < sublocs.length; i++)
			this.addSublocation(sublocs[i]);
	}
}