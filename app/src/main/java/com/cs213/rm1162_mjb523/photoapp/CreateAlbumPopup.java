package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;


public class CreateAlbumPopup extends AppCompatActivity {
    public static final String CREATE = "NEW_ALBUM_CREATED";
    static final int OK = -1;
    static final int CANCEL = 0;

    private Button createButton;
    private Button cancelButton;
    private EditText createAlbumEditText;
    private User u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album_popup);

        u = User.loadData(this);

        createButton = (Button) findViewById(R.id.createButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        createAlbumEditText = (EditText) findViewById(R.id.createAlbumEditText);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str  = createAlbumEditText.getText().toString();
                if(str == null || str.trim().isEmpty()){
                    Toast.makeText(CreateAlbumPopup.this, "Need a non-empty album name", Toast.LENGTH_SHORT).show();
                    return;
                }
                Album a = new Album(str.trim());
                if(u.albumExists(a)){
                    Toast.makeText(CreateAlbumPopup.this, "Album already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                u.getAlbums().add(a);
                goBack(OK, a.getAlbumName());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack(CANCEL, null);
            }
        });
    }

    private void goBack(int code, String str){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, Albums.class);

        if(code == OK){
            setResult(RESULT_OK);
            bundle.putString(CREATE, str);
        }else{
            setResult(RESULT_CANCELED);
            bundle.putString(CREATE, "");
        }
        u.saveData(this);
        intent.putExtras(bundle);
        try {
            u.saveData(this);
        }catch (Exception e){

        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Albums.class);
        try {
            u.saveData(this);
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
            u.saveData(this);
        }catch (Exception e){

        }
        super.onStop();
    }

}