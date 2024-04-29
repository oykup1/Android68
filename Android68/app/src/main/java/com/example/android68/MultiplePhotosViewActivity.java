package com.example.android68;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class MultiplePhotosViewActivity extends AppCompatActivity {
    final Context context = this;
    private int photoPos;
    private TextView nameOfFile, textForTag;
    private Button previousBtn, nextBtn;
    private ArrayList<Photo> listOfPhotosInAlbum = new ArrayList<Photo>();
    private Album currentAlbum = null;
    private int currentIndex;
    ImageView photoVisible;

    //Tag variables
    private EditText userEnteredTag;
    private ArrayAdapter<String> tagA;
    private ListView tagList;
    private ArrayList<String> listOfTags = new ArrayList<String>();
    private Button removeTagBtn, addTagLocBtn, addTagPerBtn;
    private String[] listAttri = {"Location", "Person"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_photos_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent i = getIntent();
        this.photoPos = i.getIntExtra("photoPosition", -1);

        photoVisible = (ImageView) findViewById(R.id.imageView);
        nameOfFile = (TextView) findViewById(R.id.filename);
        if(this.photoPos >= 0)
        {
            Album currentAlbum = MainActivity.director.getCurrntAlbum();
            Photo currentPhoto = currentAlbum.getListOfPhotos().get(photoPos);
            nameOfFile.setText(currentPhoto.getCaptionNameFile());

            Uri img_uri = Uri.parse(currentPhoto.getFilePath());
            photoVisible.setImageURI(img_uri);
        }
        else
            showAlert("Error","Image Not found because position < 0");

        previousBtn = (Button) findViewById(R.id.previousBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousPhoto();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextPhoto();
            }
        });


        tagList = (ListView) findViewById(R.id.tagsOfPhotoSlideshow);
        listOfTags = MainActivity.director.getCurrntAlbum().getCurrPhoto().getTags();
        tagA = new ArrayAdapter<String>(this, R.layout.activity_list_of_tags_on_photo, R.id.tagOfPhotoSlideshow, listOfTags);
        tagList.setAdapter(tagA);

        userEnteredTag = (EditText) findViewById(R.id.addTagV);
        addTagLocBtn = (Button) findViewById(R.id.addTagAsLocation);
        addTagPerBtn = (Button) findViewById(R.id.addTagAsPerson);

        addTagLocBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                addingTagAsLocation();
            }
        });
        addTagPerBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                addingTagAsPerson();
            }
        });

        removeTagBtn = (Button) findViewById(R.id.removeTagBtn);
        tagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                removingTagSteps(position);
            }
        });

    }

    private void showPreviousPhoto()
    {
        listOfPhotosInAlbum.clear();
        int sizeOfAlbum = MainActivity.director.getCurrntAlbum().getListOfPhotos().size();

        for(int i = 0; i < sizeOfAlbum; i++)
        {
            listOfPhotosInAlbum.add(MainActivity.director.getCurrntAlbum().getListOfPhotos().get(i));
        }

        if(currentAlbum != MainActivity.director.getCurrntAlbum())
        {
            currentIndex = photoPos;
            currentAlbum = MainActivity.director.getCurrntAlbum();
        }
        currentIndex--;
        if(currentIndex == -1)
            currentIndex = sizeOfAlbum - 1;


        Photo nP = MainActivity.director.getCurrntAlbum().getListOfPhotos().get(currentIndex);
        MainActivity.director.getCurrntAlbum().setCurrPhoto(nP);

        Album currentAlbum = MainActivity.director.getCurrntAlbum();
        Photo currentPhoto = currentAlbum.getListOfPhotos().get(currentIndex);
        nameOfFile.setText(currentPhoto.getFilePath());
        Uri img_uri = Uri.parse(currentPhoto.getFilePath());
        photoVisible.setImageURI(img_uri);

        tagList = (ListView) findViewById(R.id.tagsOfPhotoSlideshow);
        listOfTags.clear();
        listOfTags.addAll(MainActivity.director.getCurrntAlbum().getCurrPhoto().getTags());
        tagA.notifyDataSetChanged();
        tagList.setAdapter(tagA);
    }

    private void showNextPhoto()
    {
        listOfPhotosInAlbum.clear();
        int sizeOfAlbum = MainActivity.director.getCurrntAlbum().getListOfPhotos().size();

        for(int i = 0; i < sizeOfAlbum; i++)
        {
            listOfPhotosInAlbum.add(MainActivity.director.getCurrntAlbum().getListOfPhotos().get(i));
        }

        if(currentAlbum != MainActivity.director.getCurrntAlbum())
        {
            currentIndex = photoPos;
            currentAlbum = MainActivity.director.getCurrntAlbum();
        }
        currentIndex++;

        if(currentIndex == sizeOfAlbum)
            currentIndex = 0;

        Photo nP = MainActivity.director.getCurrntAlbum().getListOfPhotos().get(currentIndex);
        MainActivity.director.getCurrntAlbum().setCurrPhoto(nP);

        Album currentAlbum = MainActivity.director.getCurrntAlbum();
        Photo currentPhoto = currentAlbum.getListOfPhotos().get(currentIndex);
        nameOfFile.setText(currentPhoto.getFilePath());
        Uri img_uri = Uri.parse(currentPhoto.getFilePath());
        photoVisible.setImageURI(img_uri);

        tagList = (ListView) findViewById(R.id.tagsOfPhotoSlideshow);
        listOfTags.clear();
        listOfTags.addAll(MainActivity.director.getCurrntAlbum().getCurrPhoto().getTags());
        tagA.notifyDataSetChanged();
        tagList.setAdapter(tagA);
    }

    private void addingTagAsLocation()
    {
        String tV = userEnteredTag.getText().toString();
        if (tV.isEmpty())
        {
            showAlert("Error", "Tag cannont be empty!");
            return;
        }

        Photo currentPhoto = MainActivity.director.getCurrntAlbum().getCurrPhoto();
        currentPhoto.addLTag(tV);

        userEnteredTag.setText("");

        try {
            MainActivity.saveAlbumList(MainActivity.director, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tagList = (ListView) findViewById(R.id.tagsOfPhotoSlideshow);
        listOfTags.clear();
        listOfTags.addAll(MainActivity.director.getCurrntAlbum().getCurrPhoto().getTags());
        tagA.notifyDataSetChanged();
        tagList.setAdapter(tagA);
    }

    public void addingTagAsPerson()
    {
        String tV = userEnteredTag.getText().toString();
        if (tV.isEmpty())
        {
            showAlert("Error", "Tag cannont be empty!");
            return;
        }

        Photo currentPhoto = MainActivity.director.getCurrntAlbum().getCurrPhoto();
        currentPhoto.addPTag(tV);

        userEnteredTag.setText("");

        try {
            MainActivity.saveAlbumList(MainActivity.director, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tagList = (ListView) findViewById(R.id.tagsOfPhotoSlideshow);
        listOfTags.clear();
        listOfTags.addAll(MainActivity.director.getCurrntAlbum().getCurrPhoto().getTags());
        tagA.notifyDataSetChanged();
        tagList.setAdapter(tagA);
    }

    private void removingTagSteps(int i)
    {
        removeTagBtn.setVisibility(View.VISIBLE);
        final String tagToBeRemoved = tagList.getItemAtPosition(i).toString();

        removeTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] temp = tagToBeRemoved.split(": ");
                String tagK = temp[0];
                String tagV = temp[1];
                Photo currentPhoto = MainActivity.director.getCurrntAlbum().getCurrPhoto();

                if(tagK.toLowerCase().equals(listAttri[0].toLowerCase()))
                    currentPhoto.deleteLTag(tagV);

                else if(tagK.toLowerCase().equals(listAttri[1].toLowerCase()))
                    currentPhoto.deletePTag(tagV);

                try {
                    MainActivity.saveAlbumList(MainActivity.director, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tagList = (ListView) findViewById(R.id.tagsOfPhotoSlideshow);
                listOfTags.clear();
                listOfTags.addAll(MainActivity.director.getCurrntAlbum().getCurrPhoto().getTags());
                tagA.notifyDataSetChanged();
                tagList.setAdapter(tagA);
            }
        });
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