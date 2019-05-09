package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

public class SearchResults extends AppCompatActivity {

    private GridView photoGrid;
    private Button goBack;
    private Album album;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        user = User.loadData(this);
        photoGrid = (GridView) findViewById(R.id.photoGrid);
        goBack = (Button) findViewById(R.id.goBack);

        album = user.getSearchResults();
        if(album == null){
            Toast.makeText(SearchResults.this, "No Search Results", Toast.LENGTH_LONG).show();
            return;
        }
        if(album.getNumPhotos() < 1){
            Toast.makeText(SearchResults.this, "No Search Results", Toast.LENGTH_LONG).show();
        }
        photoGrid.setAdapter(new ImageAdapter(this, album));
        photoGrid.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //.setAdapter(new ImageAdapter(this, album));

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    private void goBack(){
        Intent intent = new Intent(this, Albums.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Search.class);
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
