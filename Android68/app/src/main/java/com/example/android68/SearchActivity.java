package com.example.android68;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class SearchActivity extends AppCompatActivity {
    private String[] listOfTOptions = {"Location", "Person"};
    private ArrayList<String> lTags;
    private ArrayList<String> pTags;
    private ArrayList<Photo> listOfSearchOutcome = new ArrayList<Photo>();
    private MultipleOptions optionsList;
    private int selectedOptionIndex;
    private SearchAdapter searchAd;
    GridView grdVSearch;

    private EditText userEnteredValue;
    private TextView selectedTags;
    private Button addTagsToSearchListBtn, findMatchesBtn, exitSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        optionsList = (MultipleOptions) findViewById(R.id.searchList);
        ArrayAdapter<CharSequence> listAd = ArrayAdapter.createFromResource(this, R.array.search_options,android.R.layout.simple_spinner_item);
        listAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsList.setAdapter(listAd);

        optionsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOptionIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        exitSearchBtn = (Button) findViewById(R.id.exitSearchBtn);

        userEnteredValue = (EditText) findViewById(R.id.userEnteredTag);
        selectedTags = (TextView) findViewById(R.id.listOfEnteredTags);
        findMatchesBtn = (Button) findViewById(R.id.searchButton);
        addTagsToSearchListBtn = (Button) findViewById(R.id.addToSearchListBtn);
        lTags = new ArrayList<String>();
        pTags = new ArrayList<String>();

        exitSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and return to the previous one
            }
        });

        addTagsToSearchListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String value = userEnteredValue.getText().toString();
                if(value.isEmpty())
                {
                    showAlert("Error", "No Tag Entered, cannot be empty");
                    return;
                }
                addingTagstoSearchList(value);
            }
        });

        grdVSearch = (GridView) findViewById(R.id.searchedPhotosGridView);
        searchAd = new SearchAdapter(this, listOfSearchOutcome);
        grdVSearch.setAdapter(searchAd);

        findMatchesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listOfSearchOutcome.clear();
                listOfSearchOutcome.addAll(MainActivity.director.getAllPhotosWithSearchedTags(lTags, pTags));

                grdVSearch = (GridView) findViewById(R.id.searchedPhotosGridView);
                searchAd.notifyDataSetChanged();
                grdVSearch.setAdapter(searchAd);
            }
        });


    }
    private void addingTagstoSearchList(String v)
    {
        String currentEntry = selectedTags.getText().toString();
        String tagType = listOfTOptions[selectedOptionIndex];

        selectedTags.setText(currentEntry + "\n" + tagType + ": " + v);

        if(selectedOptionIndex == 0)
            lTags.add(v);
        if(selectedOptionIndex == 1)
            pTags.add(v);

        findMatchesBtn.setVisibility(View.VISIBLE);
        userEnteredValue.setText("");
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
    }
}