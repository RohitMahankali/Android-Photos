package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

public class RenameAlbumPopup extends AppCompatActivity {
    public static final String RENAME = "RENAMED";
    static final int OK = -1;
    static final int CANCEL = 0;

    private Button renameButton;
    private Button cancelButton;
    private EditText currentNameEditText;
    private EditText renameAlbumEditText;
    private User u;
    private int index;
    private Album a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_album_popup);

        renameButton = (Button) findViewById(R.id.renameButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        currentNameEditText = (EditText) findViewById(R.id.currentNameEditText);
        renameAlbumEditText = (EditText) findViewById(R.id.renameAlbumEditText);
        currentNameEditText.setEnabled(false);

        u = User.loadData(this);
        index = getIntent().getIntExtra(Albums.KEY, 0);
        a = u.getAlbums().get(index);
        /*
        Bundle dataBundle = new Bundle();
        dataBundle.putInt(YOUR_KEY, id);
        intent.putExtras(dataBundle);

        int defaultValue = 0;
        int id = getIntent().getIntExtra(YOUR_KEY, defaultValue);
        */
        currentNameEditText.setText("Current Name: " + a.getAlbumName(), TextView.BufferType.NORMAL);

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str  = renameAlbumEditText.getText().toString().trim();
                if(str == null || str.isEmpty()){
                    Toast.makeText(RenameAlbumPopup.this, "Need a non-empty album name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(str.equals(a.getAlbumName())){
                    Toast.makeText(RenameAlbumPopup.this, "No change made", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Album alb : u.getAlbums()) {
                    if(alb.equals(a)) continue;
                    if(alb.getAlbumName().equals(str)) {
                        renameAlbumEditText.setText("", TextView.BufferType.EDITABLE);
                        Toast.makeText(RenameAlbumPopup.this, "Album already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                a.setAlbumName(str);
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
        u.saveData(this);
        if(code == OK){
            setResult(RESULT_OK);
            bundle.putString(RENAME, str);
        }else{
            setResult(RESULT_CANCELED);
            bundle.putString(RENAME, "");
        }
        intent.putExtras(bundle);
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
