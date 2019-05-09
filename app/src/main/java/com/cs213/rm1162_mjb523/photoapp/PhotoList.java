package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

import java.io.File;

public class PhotoList extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 679;
    private Album album;
    private int albumIndex;
    private GridView photoGridView;
    private int photoIndex = -1;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.loadData(this);
        setContentView(R.layout.activity_photo_list);
        //Toast.makeText(this,"hello", Toast.LENGTH_SHORT).show();
        photoGridView = findViewById(R.id.photoGridView);
        Bundle albumsData = getIntent().getExtras();
        if (albumsData == null) {
            //Toast.makeText(this, "line 29", Toast.LENGTH_SHORT).show();
            return;
        }
        albumIndex = (int)albumsData.get("albumIndex");
        //Toast.makeText(this, "line 29", Toast.LENGTH_SHORT).show();
        album = user.getAlbums().get(albumIndex);
        this.setTitle(album.getAlbumName());
        if(album == null) {
            //Toast.makeText(this, "line 34", Toast.LENGTH_SHORT).show();
            return;
        }
        configureGridview();

    }

    private void configureGridview(){
        photoGridView.setAdapter(new ImageAdapter(this, album));
        photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                photoIndex = position;
            }
        });
    }

    public void displayOnClick(View v){
        //int photoIndex = photoGridView.getSelectedItemPosition();
        if(photoIndex < 0){
            Toast.makeText(PhotoList.this, "Select a Photo", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, DisplayPhoto.class);
        intent.putExtra("photoIndex", photoIndex);
        intent.putExtra("albumIndex", albumIndex);
        user.saveData(this);
        startActivity(intent);
    }

    public void addPhotoOnClick(View v){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        File picChooser = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picPath = picChooser.getAbsolutePath();
        Uri picURI = Uri.parse(picPath);
        intent.setDataAndType(picURI, "image/*");
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
        user.saveData(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                Uri picURI = data.getData();
                //Toast.makeText(this, "Album already contains this photo", Toast.LENGTH_SHORT).show();
                try {
                    Photo newPhoto = new Photo(picURI.toString());
                    if(album.getPhotos().contains(newPhoto)){
                        Toast.makeText(this, "Album already contains this photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    album.addPhoto(newPhoto);

                    configureGridview();
                } catch (NullPointerException e) {
                    Toast.makeText(this, "Cannot Open Image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void removePhotoOnClick(View v){
        //photoIndex = photoGridView.getSelectedItemPosition();
        if(photoIndex < 0){
            Toast.makeText(PhotoList.this, "Select a Photo", Toast.LENGTH_SHORT).show();
            return;
        }
        album.removePhoto(photoIndex);
        user.saveData(this);
        configureGridview();
        photoGridView.setItemChecked(-1, true);
        photoIndex =- 1;
    }

    public void slideshowOnClick(View v){
        if(album.getPhotos().isEmpty()){
            Toast.makeText(this, "No Photos in Album", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, Slideshow.class);
        intent.putExtra("albumIndex", albumIndex);
        startActivity(intent);
    }

    public void moveOnClick(View v){
        //photoIndex = photoGridView.getSelectedItemPosition();
        if(photoIndex < 0){
            Toast.makeText(PhotoList.this, "Select a Photo", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, Move.class);
        intent.putExtra("photoIndex", photoIndex);
        intent.putExtra("albumIndex", albumIndex);
        user.saveData(this);
        startActivity(intent);
        //removePhotoOnClick(v);
        photoGridView.setItemChecked(-1, true);
        photoIndex =- 1;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Albums.class);
        try {
            user.saveData(this);
        }catch (Exception e){

        }
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //photoGridView.setAdapter(null);
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

