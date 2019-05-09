package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DisplayPhoto extends AppCompatActivity {

    private GridView tagGridView;
    private ImageView photoImageView;
    private Photo myPhoto;
    private Album album;
    private TextView captionTextView;
    private int photoIndex;
    private int albumIndex;
    private int tagIndex = -1;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

        user = User.loadData(this);

        tagIndex = -1;
        //this.setTitle(album.getAlbumName());
        //Toast.makeText(this,"here", Toast.LENGTH_LONG).show();
        captionTextView = findViewById(R.id.captionTextView);
        tagGridView = findViewById(R.id.tagGridView);
        photoImageView = findViewById(R.id.photoImageView);
        Bundle photoListData = getIntent().getExtras();
        if(photoListData == null){
            return;
        }

        photoIndex = (int)photoListData.get("photoIndex");
        //Toast.makeText(this,"" + photoIndex, Toast.LENGTH_LONG).show();
        //tagGridView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        albumIndex = (int)photoListData.get("albumIndex");
        album = user.getAlbums().get(albumIndex);
        myPhoto = album.getPhotos().get(photoIndex);
        //Toast.makeText(this,myPhoto.getPath(), Toast.LENGTH_LONG).show();

        try {
            tagGridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, myPhoto.getTags()));
        }catch(NullPointerException e) {
            return;
        }
        tagGridView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tagGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                tagIndex = position;
            }
        });

        InputStream inputStream;
        try{
            //Toast.makeText(this,myPhoto.getPath(), Toast.LENGTH_LONG).show();
            inputStream = getContentResolver().openInputStream(Uri.parse(myPhoto.getPath()));
            Bitmap myImage = BitmapFactory.decodeStream(inputStream);
            photoImageView.setImageBitmap(myImage);

        }catch (FileNotFoundException e){
            Toast.makeText(this, "Cannot Open Image", Toast.LENGTH_LONG).show();
        }
        captionTextView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        captionTextView.setText(Photo.getFileName(myPhoto,this));
    }

    public void addTag(View v){
        Intent intent = new Intent(this, AddTag.class);
        intent.putExtra("photoIndex", photoIndex);
        intent.putExtra("albumIndex", albumIndex);
        startActivity(intent);
        finish();
    }

    public void deleteTag(View v){

        if(tagIndex < 0){
            Toast.makeText(this, "Select a Tag", Toast.LENGTH_SHORT).show();
            return;
        }
        myPhoto.getTags().remove(tagIndex);
        user.saveData(this);
        tagGridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, myPhoto.getTags()));
        tagIndex = -1;
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
