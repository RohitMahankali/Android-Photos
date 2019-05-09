package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

public class Move extends AppCompatActivity {

    private ListView albumList;
    private int photoIndex;
    private Album album;
    private int albumIndex;
    private int selectedIndex = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        user = User.loadData(this);
        albumList = findViewById(R.id.albumList);
        Bundle data = getIntent().getExtras();
        if (data == null) {
            return;
        }
        photoIndex = (int) data.get("photoIndex");
        if (photoIndex == -1) {
            return;
        }
        albumIndex = (int)data.get("albumIndex");
        album = user.getAlbums().get(albumIndex);
        albumList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        albumList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, user.getAlbums()));
        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
            }
        });
    }

    public void move(View v){
        if(selectedIndex < 0){
            Toast.makeText(this, "Select another Album", Toast.LENGTH_SHORT).show();
            return;
        }
        Photo currPhoto = album.getPhotos().get(photoIndex);
        Album moveTo = user.getAlbums().get(selectedIndex);
        for(Photo ph : moveTo.getPhotos()){
            if(currPhoto.equals(ph)){
                Toast.makeText(this, "Photo is already in selected album", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        moveTo.addPhoto(album.removePhoto(photoIndex));
        user.saveData(this);
        cancel(v);
    }

    public void cancel(View v){
        Intent intent = new Intent(this, PhotoList.class);
        intent.putExtra("albumIndex", albumIndex);
        startActivity(intent);
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
