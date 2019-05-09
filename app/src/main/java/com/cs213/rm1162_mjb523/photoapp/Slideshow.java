package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Slideshow extends AppCompatActivity {

    private int albumIndex;
    private Album album;
    private ImageView slideshowImageView;
    private Button nextButton, prevButton;
    private int currPosition = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        user = User.loadData(this);
        slideshowImageView = findViewById(R.id.slideshowImageView);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        prevButton.setEnabled(false);
        Bundle data = getIntent().getExtras();
        if(data == null){
            return;
        }
        albumIndex = (int)data.get("albumIndex");
        album = user.getAlbums().get(albumIndex);

        if(album.getPhotos().size() == 1){
            nextButton.setEnabled(false);
        }
        InputStream inputStream;
        try{
            inputStream = getContentResolver().openInputStream(Uri.parse(album.getPhotos().get(currPosition).getPath()));
            Bitmap myImage = BitmapFactory.decodeStream(inputStream);
            slideshowImageView.setImageBitmap(myImage);

        }catch (FileNotFoundException e){
            Toast.makeText(this, "Cannot Open Image", Toast.LENGTH_LONG).show();
        }
    }

    public void prevPhoto(View v){
        nextButton.setEnabled(true);
        currPosition = currPosition - 1;
        if(currPosition == 0){
            prevButton.setEnabled(false);
        }
        InputStream inputStream;
        try{
            inputStream = getContentResolver().openInputStream(Uri.parse(album.getPhotos().get(currPosition).getPath()));
            Bitmap myImage = BitmapFactory.decodeStream(inputStream);
            slideshowImageView.setImageBitmap(myImage);

        }catch (FileNotFoundException e){
            Toast.makeText(this, "Cannot Open Image", Toast.LENGTH_LONG).show();
        }
    }

    public void nextPhoto(View v){
        prevButton.setEnabled(true);
        currPosition = currPosition+1;
        if(album.getPhotos().size() == currPosition+1){
            nextButton.setEnabled(false);
        }
        InputStream inputStream;
        try{
            inputStream = getContentResolver().openInputStream(Uri.parse(album.getPhotos().get(currPosition).getPath()));
            Bitmap myImage = BitmapFactory.decodeStream(inputStream);
            slideshowImageView.setImageBitmap(myImage);

        }catch (FileNotFoundException e){
            Toast.makeText(this, "Cannot Open Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, PhotoList.class);
        intent.putExtra("albumIndex", albumIndex);
        try {
            user.saveData(this);
        }catch (Exception e){

        }
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    @Override
    public void onStop(){
        try{
            user.saveData(this);
        }catch (Exception e){

        }
        super.onStop();
    }


}
