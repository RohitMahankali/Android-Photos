/**
 * Contains the all objects associated with a PhotoApp: Admin, User, Album, Photo, and Tag
 */
package com.cs213.rm1162_mjb523.photoapp;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

//import javax.imageio.ImageIO;

import android.provider.OpenableColumns;
//import javafx.scene.image.Image;

/**
 * photo class wraps around actual photo location, tags, caption
 * @author Rohit Mahankali & Michael Belmont
 *
 */
public class Photo implements Serializable{

	/**
	 *  default UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * path to actual photo
	 */


	private String path;
	/**
	 * all the tags photo has
	 */
	private ArrayList<Tag> tags;

	//private ImageView thumbnail = null;
	

	/**
	 * Create Photo from scratch
	 * @param path
     *
     */

	public Photo(String path) {
	    this.path = path;
		this.tags = new ArrayList<>();
	}
	

	/**
	 * Copy photo (details into new obj)
	 * @param original - photo you want to copy
	 */
	public Photo(Photo original) {
		this.path = original.getPath();
		this.tags = new ArrayList<>(original.getTags());
	}
	
	/**
	 * @return path
	 */
	public String getPath() {
		return this.path;
	}


	
	/**
	 * @return imageview of thumbnail
	 *
	public ImageView getThumbnail() {
		return this.thumbnail;
	}
    /\*/
	
	/**
	 * @return list of tags in photo
	 */
	public ArrayList<Tag> getTags(){
		return this.tags;
	}
	
	/**
	 * Add Tag to photo
	 * @param tag
	 */
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	/**
	 * @param tag you're searching for (case insensitive type-val pairs)
	 * @return if tag is present (checks type-val pairs using Tag.equals)
	 */
	public boolean hasTag(Tag tag) {
		if(tags == null || tags.isEmpty())
			return false;
		for(Tag t: tags) {
			if(t.equals(tag)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * creates thumbnail for photo
	 */
	public void createThumbnail() {
	/*	Image img = new Image("file:" + path);
		ImageView tn = new ImageView();
		tn.setImage(img);
		tn.setFitWidth(100);
		tn.setFitHeight(100);
		tn.setPreserveRatio(true);
		tn.setSmooth(true);
        //tn.setCache(true);
		this.thumbnail = tn;*/
	}
	
	/**
	 * for serialization, displaying
	 * @return if thumbnail == null
	 *
	public boolean hasThumbnail() {
		return !(this.thumbnail == null);
	}
	*/
	/**
	 * deletes thumbnail for serialization
	 *
	public void deleteThumbnail() {
		this.thumbnail = null;
	}
	*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Photo))
			return false;
		Photo other = (Photo) o;
		return this.path.equals(other.path);
	}

	public static String getFileName(Photo photo, Context context) {
		String fileName = null;
		Uri uri = Uri.parse(photo.getPath());
		if (uri.getScheme().equals("content")) {
			Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (fileName == null) {
			fileName = uri.getPath();
			int cut = fileName.lastIndexOf('/');
			if (cut != -1) {
				fileName = fileName.substring(cut + 1);
			}
		}
		return fileName;
	}
}
