package com.cs213.rm1162_mjb523.photoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cs213.rm1162_mjb523.photoapp.R;

import java.util.ArrayList;

public class Search extends AppCompatActivity {


    private RadioGroup tagTypeRadioGroup;
    private RadioButton personRadioButton;
    private RadioButton locationRadioButton;
    private AutoCompleteTextView tagValueAutoText;
    private RadioGroup andOrRadioGroup;
    private RadioButton andRadioButton;
    private RadioButton orRadioButton;
    private Button addParameter;
    private Button deleteParameter;
    private GridView searchParametersGridView;
    private Button searchButton;
    private Button goBack;

    private User user;
    private ArrayList<Photo> photos;
    private ArrayList<SearchCriteria> sclist = new ArrayList<SearchCriteria>();
    /**
     * false means location
     * true means person
     */
    private boolean personType = true;
    /**
     * false means or
     * true means and
     */
    private boolean andType = true;
    private int index  = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tagTypeRadioGroup = (RadioGroup) findViewById(R.id.tagTypeRadioGroup);
        personRadioButton = (RadioButton) findViewById(R.id.personRadioButton);
        locationRadioButton = (RadioButton) findViewById(R.id.locationRadioButton);
        tagValueAutoText = (AutoCompleteTextView) findViewById(R.id.tagValueAutoText);
        andOrRadioGroup = (RadioGroup) findViewById(R.id.andOrRadioGroup);
        andRadioButton = (RadioButton) findViewById(R.id.andRadioButton);
        orRadioButton = (RadioButton) findViewById(R.id.orRadioButton);
        addParameter = (Button) findViewById(R.id.addParameter);
        deleteParameter = (Button) findViewById(R.id.deleteParameter);
        searchParametersGridView = (GridView) findViewById(R.id.searchParametersGridView);
        searchButton = (Button) findViewById(R.id.searchButton);
        goBack = (Button) findViewById(R.id.goBack);

        user = User.loadData(this);
        tagValueAutoText.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, user.getPersonTags()));
        searchParametersGridView.setAdapter(new ArrayAdapter<SearchCriteria>(this,
                android.R.layout.simple_list_item_single_choice, sclist)
        );
        searchParametersGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        searchParametersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                index = position;
            }
        });
    }

    public void add(View view){
        String str = tagValueAutoText.getText().toString().trim();
        if(str == null || str.isEmpty()){
            Toast.makeText(this, "Enter a non-empty Tag Value", Toast.LENGTH_SHORT).show();
            return;
        }
        personType = tagTypeRadioGroup.getCheckedRadioButtonId() == personRadioButton.getId();
        andType = andOrRadioGroup.getCheckedRadioButtonId() == andRadioButton.getId();
        SearchCriteria sc = new SearchCriteria(andType, personType, str);
        sclist.add(sc);
        searchParametersGridView.setAdapter(new ArrayAdapter<SearchCriteria>(this,
                android.R.layout.simple_list_item_single_choice, sclist)
        );
        index = -1;
    }

    public void delete(View view){
        if(index < 0){
            Toast.makeText(Search.this, "Select a Search Parameter first", Toast.LENGTH_SHORT).show();
            return;
        }
        sclist.remove(index);
        searchParametersGridView.setAdapter(new ArrayAdapter<SearchCriteria>(this,
                android.R.layout.simple_list_item_single_choice, sclist)
        );
        index = -1;
    }

    public void search(View view){
        if(sclist.size() <1){
            Toast.makeText(Search.this, "Need Search Parameters first", Toast.LENGTH_SHORT).show();
            return;
        }

        this.photos = new ArrayList<Photo>();
        ArrayList<ArrayList<SearchCriteria>> sclists = separateSearchCriteria();
        Photo pic;
        for(Album a: this.user.getAlbums()) {
            for(Photo p: a.getPhotos()) {
                for(ArrayList<SearchCriteria> scl: sclists) {
                    if(this.checkCriteria(p, scl)) {
                        if(!this.photos.contains(p)) {
                            pic = new Photo(p);
                            this.photos.add(pic);
                        }
                        break;
                    }
                }
            }
        }

        //send to search results
        Album sr = new Album("sr");
        sr.changeAllPhotos(this.photos);
        user.setSearchResults(sr);
        user.saveData(this);

        Intent intent = new Intent(this, SearchResults.class);
        startActivity(intent);
    }

    /**
     * separates list of searchcrit into AND groups (lists of criteria that are ANDed together)
     * @return list of AND groups
     */
    private ArrayList<ArrayList<SearchCriteria>> separateSearchCriteria(){
        ArrayList<ArrayList<SearchCriteria>> sclists = new ArrayList<ArrayList<SearchCriteria>>();
        ArrayList<SearchCriteria> scrit = new ArrayList<SearchCriteria>(sclist);
        ArrayList<SearchCriteria> group = new ArrayList<SearchCriteria>();
        int i =0;
        for(SearchCriteria sc: scrit) {
            if(sc.isAnd()) {
                group.add(sc);
            }else {
                if(group.size() > 0 && i > 0) {
                    sclists.add(group);
                    group = new ArrayList<SearchCriteria>();
                }
                group.add(sc);
            }
            i++;
        }
        sclists.add(group);
        return sclists;
    }

    /**
     * checks if photo p fits the search criteria (all ANDed together)
     * @param p to check
     * @param andgroup list of searchcrit (all AND)
     * @return
     */
    private boolean checkCriteria(Photo p, ArrayList<SearchCriteria> andgroup) {
        boolean retval = true;
        SearchCriteria sc;
        for(int i = 0; i < andgroup.size(); i++) {
            sc = andgroup.get(i);
            retval = sc.meetsCriteria(p);
            if(retval == false) {
                return false;
            }
        }
        return retval;
    }

    public void changeAutoComplete(View view){
        if (tagTypeRadioGroup.getCheckedRadioButtonId() == personRadioButton.getId()){
            tagValueAutoText.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, user.getPersonTags()));
        }else{
            tagValueAutoText.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, user.getLocationTags()));
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, Albums.class);
        user.saveData(this);
        startActivity(intent);
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
