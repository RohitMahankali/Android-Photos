/**
 * Contains the all objects associated with a PhotoApp: Admin, User, Album, Photo, and Tag
 */
package com.cs213.rm1162_mjb523.photoapp;

import java.io.Serializable;

/**
 * Tag has (location, london) = type: location = value: london
 * and helper methods for it
 * @author Rohit Mahankali & Michael Belmont
 *
 */
public class Tag implements Serializable{

	/**
	 *  default UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Tag type/category, Ex. location
	 */
	private String type;
	/**
	 * Tag value, Ex. london
	 */
	private String value;
	
	/**
	 * creates tag
	 * @param type
	 * @param value
	 */
	public Tag(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * getter because u can't edit
	 * @return type of Tag
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * getter because u can't edit
	 * @return value of Tag
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * if tag has value
	 * @param str - to check
	 * @return if tag has value
	 */
	public boolean hasValue(String str) {
		return this.value.equalsIgnoreCase(str);
	}
	
	/**
	 * if tag has type
	 * @param str - to check
	 * @return if tag has type
	 */
	public boolean hasType(String str) {
		return this.type.equalsIgnoreCase(str);
	}
	
	/* (non-Javadoc)
	 * Override
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Tag))
			return false;
		Tag other = (Tag) o;
		return this.type.equalsIgnoreCase(other.getType()) && this.value.equalsIgnoreCase(other.getValue());
	}

	public boolean sameTag(Tag t){
		String val = t.getValue();
		if(this.value.length() < val.length()){
			val = val.substring(0, this.value.length());
		}
		return this.value.equalsIgnoreCase(val);
	}
	
	/**
	 * @return String version of the Tag, ex. (location, London)
	 */
	public String toString() {
		return "(" + type + ", " + value + ")";
	}
		
}
