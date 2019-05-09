/**
 * Contains the all objects associated with a PhotoApp: Admin, User, Album, Photo, and Tag
 */
package com.cs213.rm1162_mjb523.photoapp;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An album object that has a name and contains photos
 * @author Bhargav
 *
 */
public class Album implements Serializable{

	/**
	 *  default UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name of the Album
	 */
	private String albumName;
	/**
	 * Number of photos in the Album
	 */
	private int numPhotos;
	/**
	 * List of Photos in the Album
	 */
	private ArrayList<Photo> photos;
	
	/**
	 * Constructor for an Album that is used when an Album is created without any Photos. Must add Photos to the Album later
	 * @param name User chosen name for an Album
	 */
	public Album(String name) {
		setAlbumName(name);
		numPhotos = 0;
		photos = new ArrayList<>();
	}
	/**
	 * Constructor used to create an Album from search results
	 * @param name User chosen name for an Album
	 * @param searchResults Photos that were found when the used the search function
	 */
	public Album(String name, ArrayList<Photo> searchResults) {
		setAlbumName(name);
		photos = searchResults;
		numPhotos = searchResults.size();
	}

	/**
	 * @return all photos in album
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	/**
	 * changes all internal vars
	 * @param photo to add
	 */
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
		numPhotos++;
	}

	public void changeAllPhotos(ArrayList<Photo> pics){
		this.photos = new ArrayList<Photo>(pics);
		this.numPhotos = pics.size();
	}
	
	/**
	 * changes all internal vars
	 * @param index of photo to remove
	 * @return photo
	 */
	public Photo removePhoto(int index) {
		Photo val = this.photos.remove(index);
		numPhotos--;
		return val;
	}
	
	/**
	 * @return the numPhotos
	 */
	public int getNumPhotos() {
		return numPhotos;
	}

	/**
	 * @return the albumName
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * @param albumName A new Album name that the user has chosen
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String toString(){
	    return getAlbumName();
    }

    public boolean equals(Object o) {
        if(o == null || !(o instanceof Album))
            return false;
        Album other = (Album) o;
        return this.getAlbumName().equalsIgnoreCase(other.getAlbumName());
    }



}
