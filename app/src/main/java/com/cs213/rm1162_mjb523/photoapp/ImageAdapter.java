package com.cs213.rm1162_mjb523.photoapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs213.rm1162_mjb523.photoapp.R;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Album currAlbum;
    private int selectedPosition=-1;

    public ImageAdapter(Context c, Album currAlbum) {
        this.currAlbum = currAlbum;
        mContext = c;
    }

    public int getCount() {
        return currAlbum.getNumPhotos();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Photo myPhoto = currAlbum.getPhotos().get(position);


        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_photo, null);
        }

        final ImageView photoImageView = convertView.findViewById(R.id.imageview_cover_art);
        final TextView captionTextView = convertView.findViewById(R.id.capTextView);
        /*
        photoImageView = new ImageView(mContext);
        photoImageView.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
        photoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoImageView.setPadding(8, 8, 8, 8);
        */


        photoImageView.setImageURI(Uri.parse(myPhoto.getPath()));
        captionTextView.setText(Photo.getFileName(myPhoto, convertView.getContext()));
        convertView.setLayoutParams(new ViewGroup.LayoutParams(250,250));
        return convertView;
    }
}
