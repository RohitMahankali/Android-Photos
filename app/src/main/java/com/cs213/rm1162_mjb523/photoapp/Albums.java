package com.cs213.rm1162_mjb523.photoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.cs213.rm1162_mjb523.photoapp.R;

import java.util.ArrayList;

public class Albums extends AppCompatActivity {
    static final int AlBUM_REQUEST = 1;
    static final int RENAME_REQUEST = 2;
    static final int SEARCH_REQUEST = 3;


    public static final String KEY = "INDEX";

    private ListView albumsListView;
    private Button openAlbumButton;
    private Button renameAlbumButton;
    private Button deleteAlbumButton;
    private Button createAlbumButton;
    private Button searchButton;

    private User user;
    private ArrayList<Album> albums;
    private ArrayList<String> albumNames = new ArrayList<String>();
    private int index = -1;
    private boolean deletion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        setTitle("Albums");
        init();

        albumsListView = (ListView) findViewById(R.id.albumsListView);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                albumNames);
*/
        //	simple_list_item_single_choice
        albumsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_single_choice,
                albumNames) ;
        /*
        * {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                //ImageView icon = (ImageView) v.findViewById(R.id.img);
                if (albumsListView.isItemChecked(position)) {
                    //index = position;
                    //icon.setImageResource(R.drawable.checked);
                } else {
                    // icon.setImageResource(R.drawable.unchecked);
                }
                return v;
            }
        }*/
        albumsListView.setAdapter(adapter);
        albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                  index = position;
              }
          });

        openAlbumButton = (Button) findViewById(R.id.openAlbumButton);
        renameAlbumButton = (Button) findViewById(R.id.renameAlbumButton);
        deleteAlbumButton = (Button) findViewById(R.id.deleteAlbumButton);
        createAlbumButton = (Button) findViewById(R.id.createAlbumButton);
        searchButton = (Button) findViewById(R.id.searchButton);
/*
        openAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */

        renameAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameAlbum();
            }
        });

        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlbum();
            }
        });

       /* searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view);
            }
        });*/
    }

    public void deleteAlbum(View v){
        if(index < 0){
            Toast.makeText(Albums.this, "Select something first", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(Albums.this).create();
        alertDialog.setTitle("Delete Album");
        alertDialog.setMessage("Are you sure you want to delete: " + albumNames.get(index));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        albums.remove(index);
                        resetList();
                        albumsListView.setAdapter(new ArrayAdapter<String>(
                                Albums.this,
                                android.R.layout.simple_list_item_single_choice,
                                albumNames) );
                        albumsListView.setItemChecked(-1, true);
                        index = -1;

                        user.saveData(Albums.this);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    private void init(){
        //User.loadData();
        this.user = User.loadData(this);
        this.albums = this.user.getAlbums();
        this.user.setSearchResults(null);
        if(this.albums == null)
            this.albums = new ArrayList<Album>();

        for(Album a: this.albums){
            this.albumNames.add(a.getAlbumName());
        }
        this.index = -1;
        this.deletion = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == AlBUM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                resetList();
                user.saveData(this);
            }
            return;
        }
        if (requestCode == RENAME_REQUEST){
            if (resultCode == RESULT_OK) {
                resetList();
                user.saveData(this);
            }
            return;
        }
        if (requestCode == SEARCH_REQUEST){
            return;
        }
    }

    private void resetList(){
        this.albums = user.getAlbums();
        albumNames = new ArrayList<String>();
        for(Album a: this.albums){
            this.albumNames.add(a.getAlbumName());
        }
    }


    private void openAlbum(int i){

    }


    private void createAlbum(){
        Intent intent = new Intent(this, CreateAlbumPopup.class);
        startActivityForResult(intent, AlBUM_REQUEST);
        user.saveData(this);
    }

    public void openTest(View v){
        //int albumIndex = albumsListView.getSelectedItemPosition();
        if(index<0){
            Toast.makeText(this, "Select an Album", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PhotoList.class);
        intent.putExtra("albumIndex", index);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void renameAlbum(){
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, index);
        Intent intent = new Intent(this, RenameAlbumPopup.class);
        intent.putExtras(bundle);
        if(index < 0){
            Toast.makeText(Albums.this, "Select something first", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(intent, RENAME_REQUEST);
    }

    public void search(View view){
        if(user.getAlbums().size() < 1){
            Toast.makeText(Albums.this, "Need albums first", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        user.saveData(this);
        return;
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
