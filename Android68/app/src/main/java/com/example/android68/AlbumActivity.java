package com.example.android68;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class AlbumActivity extends AppCompatActivity {

    private Album currAlbum;
    private static ArrayList<Photo> listOfPhotosInAlbum = new ArrayList<Photo>();
    private Button deletePhotoBtn, addPhotoBtn, movePhotoBtn;
    final Context c = this;
    private String id;
    GridView gridV;
    private PhotoAdapter pAdap;
    final Context context = this;
    ActivityResultLauncher<Intent> launcher;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        TextView albumNameDisplay = (TextView) findViewById(R.id.albumName);
        albumNameDisplay.setText(MainActivity.director.getCurrntAlbum().getNameOfAlbum());

        retrievePhotosOfAlbum();

        gridV = (GridView) findViewById(R.id.gridView1);
        pAdap = new PhotoAdapter(this, listOfPhotosInAlbum);
        gridV.setAdapter(pAdap);


        addPhotoBtn = findViewById(R.id.addPhotoBtn);
        registerResults();
        addPhotoBtn.setOnClickListener(view -> pickImage());

        deletePhotoBtn = findViewById(R.id.deletePhotoBtn);
        movePhotoBtn = findViewById(R.id.movePhotoBtn);

        updateButtonStates(false);
        gridV.setLongClickable(true);

        gridV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                int index = position;
                view.setSelected(true);
                updateButtonStates(true);
                final Photo photoAtPosition = MainActivity.director.getCurrntAlbum().getListOfPhotos().get(index);

                deletePhotoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removePhotoFromAlbum(index);
                    }
                });

                movePhotoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        movePhotoBetweenAlbums(photoAtPosition, index);
                    }
                });
                return true;
            }
        });

        //transitioning to differnt activity for single photo view
        gridV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Photo p = MainActivity.director.getCurrntAlbum().getListOfPhotos().get(position);
                MainActivity.director.getCurrntAlbum().setCurrPhoto(p);

                Intent multiplePhotoViewIntent = new Intent(AlbumActivity.this, MultiplePhotosViewActivity.class);
                multiplePhotoViewIntent.putExtra("photoPosition", position);
                startActivity(multiplePhotoViewIntent);
            }
        });
    }

    public void removePhotoFromAlbum(int index)
    {
        MainActivity.director.getCurrntAlbum().removePhoto(index);
        try{
            MainActivity.saveAlbumList(MainActivity.director, context);
        }catch (IOException e){
            e.printStackTrace();
        }
        gridV = (GridView) findViewById(R.id.gridView1);
        retrievePhotosOfAlbum();
        pAdap.notifyDataSetChanged();
        gridV.setAdapter(pAdap);

        updateButtonStates(false);
    }

    public void movePhotoBetweenAlbums(Photo photoAtPosition, int index)
    {
        LayoutInflater lIA = LayoutInflater.from(c);
        View v = lIA.inflate(R.layout.dialog_box, null);
        AlertDialog.Builder aDBUserInput = new AlertDialog.Builder(c);
        aDBUserInput.setView(v);

        TextView heading = (TextView) v.findViewById(R.id.title);
        heading.setText("Photo Moving");
        final EditText albumND = (EditText) v.findViewById(R.id.userInputDialog);
        aDBUserInput.setCancelable(false).setPositiveButton("Move", new DialogInterface.OnClickListener(){
           public void onClick(DialogInterface dB, int id)
           {
               String nameOfAlbum = albumND.getText().toString();
               String photoPath = photoAtPosition.getFilePath();

               String res = checkMovingPhotoValidity(nameOfAlbum, photoPath);
               if(res.equals("a")) {
                   showAlert("Error", "Photo already exists in the specified album");
                   return;
               }
               if(res.equals("c")) {
                   showAlert("Error", "No such Album exists");
                   return;
               }

               int newAlbumIndex = 0;
               for(int i = 0; i < MainActivity.director.getListOfAlbums().size(); i++)
               {
                   if(MainActivity.director.getListOfAlbums().get(i).getNameOfAlbum().equals(nameOfAlbum))
                       newAlbumIndex = i;
               }
               MainActivity.director.getListOfAlbums().get(newAlbumIndex).addPhoto(photoPath);

               //copying all tags
               Photo origP = MainActivity.director.getCurrntAlbum().getListOfPhotos().get(index);
               Album newAlbum = MainActivity.director.getListOfAlbums().get(newAlbumIndex);
               int newPhotoIndex = newAlbum.getListOfPhotos().size() - 1;
               Photo newPhoto = newAlbum.getListOfPhotos().get(newPhotoIndex);
               for(int i = 0; i < origP.getLocationTags().size(); i++)
               {
                   newPhoto.addLTag(origP.getLocationTags().get(i));
               }
               for(int i = 0; i < origP.getPersonTags().size(); i++)
               {
                   newPhoto.addPTag((origP.getPersonTags().get(i)));
               }

               //Deleting from old album
               MainActivity.director.getCurrntAlbum().removePhoto(index);

               showAlert("Success", "Photo has been moved to the specified Album");

               try{
                   MainActivity.saveAlbumList(MainActivity.director, context);
               }catch (IOException e){
                   e.printStackTrace();
               }

               gridV = (GridView) findViewById(R.id.gridView1);
               retrievePhotosOfAlbum();
               pAdap.notifyDataSetChanged();
               gridV.setAdapter(pAdap);
           }}).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dB, int id)
            {
                dB.cancel();
            }
        });
        AlertDialog aDA = aDBUserInput.create();
        aDA.show();

        updateButtonStates(false);
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

    private void updateButtonStates(boolean isEnabled) {
        if(isEnabled)
        {
            deletePhotoBtn.setVisibility(View.VISIBLE);
            movePhotoBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            deletePhotoBtn.setVisibility(View.INVISIBLE);
            movePhotoBtn.setVisibility(View.INVISIBLE);
        }
    }
    private static void retrievePhotosOfAlbum()
    {
        listOfPhotosInAlbum.clear();
        listOfPhotosInAlbum.addAll(MainActivity.director.getCurrntAlbum().getListOfPhotos());
    }

    public void registerResults()
    {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Uri imageUri = result.getData().getData();
                        getContentResolver().takePersistableUriPermission(imageUri, (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
                        String image_str = imageUri.toString();

                        for(int i = 0; i < listOfPhotosInAlbum.size(); i++)
                        {
                            if(image_str.equals(listOfPhotosInAlbum.get(i).getFilePath()))
                            {
                                showAlert("Error", "Photo Already Exists");
                                return;
                            }
                        }

                        MainActivity.director.getCurrntAlbum().addPhoto(image_str);
                        try{
                            MainActivity.saveAlbumList(MainActivity.director, context);
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                        gridV = (GridView) findViewById(R.id.gridView1);
                        retrievePhotosOfAlbum();
                        pAdap.notifyDataSetChanged();
                        gridV.setAdapter(pAdap);
                    }
                    });
    }
    private String checkMovingPhotoValidity(String aN, String pP)
    {
        for(int i = 0; i < MainActivity.director.getListOfAlbums().size(); i++)
        {
            if(aN.equals(MainActivity.director.getListOfAlbums().get(i).getNameOfAlbum()))
            {
                for(int j = 0; j < MainActivity.director.getListOfAlbums().get(i).getListOfPhotos().size(); j++)
                {
                    if(MainActivity.director.getListOfAlbums().get(i).getListOfPhotos().get(j).getFilePath().equals(pP))
                        return "a"; //photo alr in album
                }
                return "b"; //photo can be moved
            }
        }
        return "c"; //no such album exists
    }
    public void  pickImage()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        launcher.launch(intent);
    }
}