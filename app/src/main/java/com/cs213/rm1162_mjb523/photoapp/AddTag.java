package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddTag extends AppCompatActivity {

    private RadioGroup tagTypeRadioGroup;
    private RadioButton personRadioButton;
    private EditText tagValueEditText;
    private Photo myPhoto;
    private Album album;

    private int photoIndex;
    private int albumIndex;


    private User u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        tagTypeRadioGroup = findViewById(R.id.tagTypeRadioGroup);
        personRadioButton = findViewById(R.id.personRadioButton);
        tagValueEditText = findViewById(R.id.tagValueEditText);
        Bundle displayPhotoBundle = getIntent().getExtras();
        u = User.loadData(this);
        if(displayPhotoBundle == null){
            return;
        }
        photoIndex= (int) displayPhotoBundle.get("photoIndex");
        albumIndex = (int)displayPhotoBundle.get("albumIndex");
        album = u.getAlbums().get(albumIndex);
        myPhoto = album.getPhotos().get(photoIndex);
    }

    public void add(View v){
        String val = tagValueEditText.getText().toString();
        if(val == null || val.isEmpty()){
            Toast.makeText(this, "Enter a Value", Toast.LENGTH_SHORT).show();
            return;
        }
        Tag newTag;
        if (tagTypeRadioGroup.getCheckedRadioButtonId() == personRadioButton.getId()){
            newTag = new Tag("person", val);
        }else{
            newTag = new Tag("location", val);
        }
        for(Tag tag : myPhoto.getTags()){
            if(newTag.equals(tag)){
                Toast.makeText(this, "Tag Already Exists", Toast.LENGTH_SHORT).show();
                tagValueEditText.clearComposingText();
                return;
            }
        }
        myPhoto.addTag(newTag);
        if (tagTypeRadioGroup.getCheckedRadioButtonId() == personRadioButton.getId()){
            if(u.personTagExists(val) == false)
                u.getPersonTags().add(val);
        }else{
            if(u.locationTagExists(val) == false)
                u.getLocationTags().add(val);
        }
        cancel(v);
    }

    public void cancel(View v){
        Intent intent = new Intent(this, DisplayPhoto.class);
        intent.putExtra("photoIndex", photoIndex);
        intent.putExtra("albumIndex", albumIndex);
        u.saveData(this);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, DisplayPhoto.class);
        intent.putExtra("albumIndex", albumIndex);
        intent.putExtra("photoIndex", photoIndex);
        try {
            u.saveData(this);
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
            u.saveData(this);
        }catch (Exception e){

        }
        super.onStop();
    }


}
