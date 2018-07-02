package com.phantomartist.sinquizcity.game;

public class Bar {

	public static final String PROPERTY_TAG = "property";
	public static final String NAME_TAG = "name";
	public static final String INFO_TAG = "info";
	public static final String POINTS_TAG = "points";
	
	private String property;
	private String name;
	private String info;
	private int points;
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	/**
	 * Returns true if bar name equals this bar name.
	 * 
	 * @param barName
	 * @return
	 */
	public boolean hasSameBarName(String barName) {
		if (getName() == null && barName != null) {
			return false;
		}
		if (getName() != null && barName == null) {
			return false;
		}
		return getName().equals(barName);
	}
	@Override
	public String toString() {
		return "Bar [property=" + property + ", name=" + name + ", info="
				+ info + ", points=" + points + "]";
	}
}
