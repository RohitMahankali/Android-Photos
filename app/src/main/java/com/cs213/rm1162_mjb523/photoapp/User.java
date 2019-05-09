/**
 * Contains the all objects associated with a PhotoApp: Admin, User, Album, Photo, and Tag
 */
package com.cs213.rm1162_mjb523.photoapp;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Object representing a User of the PhotoApp
 * @author Rohit Mahankali & Michael Belmont
 *
 */
public class User implements Serializable{

	/**
	 * Singleton instance of User. Stores all of the albums and their data.
	 * Initially will be set to serialized file
	 */
	//private final static User instance = loadData(C);

	/**
	 * UID for Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of Albums that the user has on the application
	 */
	private ArrayList<Album> albums;
	private Context context;

	private ArrayList<String> personTags;
	private ArrayList<String> locationTags;
	private static String path;
	private Album searchResults;
	

	public User() {
		albums = new ArrayList<Album>();
		personTags = new ArrayList<String>();
		locationTags = new ArrayList<String>();
		searchResults = null;
	}


	/**
	 * @return the albums
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	public ArrayList<String> getPersonTags() {
		return personTags;
	}

	public ArrayList<String> getLocationTags() {
		return locationTags;
	}

	public boolean personTagExists(String str){
		return personTags.contains(str);
	}

	public boolean locationTagExists(String str){
		return locationTags.contains(str);
	}

	/**
	 * Check if album of same name exists
	 * @param a album you're testing if already exists
	 * @return if album exists already
	 */
	public boolean albumExists(Album a){
		return albums.contains(a);
	}



	/**
	 * @return the single User instance
	 */
	/*
	public static User getInstance() {
		return instance;
	}

	public void setAlbums(ArrayList<Album> albo){
		albums.clear();
		for(Album alb : albo){
		    albums.add(alb);
        }
	}
	*/

    public static User loadData(Context context){ //throws IOException, ClassNotFoundException{
        ObjectInputStream in;
        try {
            //change to actual path

            FileInputStream fos = context.openFileInput("user.dat");
            in = new ObjectInputStream(fos);
            return (User)in.readObject();

        } catch (Exception e) {
            return new User();
        }
    }

    @SuppressWarnings("resource") //for leak
    public void saveData(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("user.dat", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
            fos.close();
        }catch(Exception e){

        }
    }


	public Album getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(Album sr) {
		this.searchResults = sr;
	}
}
