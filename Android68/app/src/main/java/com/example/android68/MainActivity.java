package com.example.android68;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private ListView albumListView;
    private ArrayAdapter<Album> adapter;
    private ArrayList<Album> albumList;
    private Button createButton, deleteButton, openButton, renameButton, handleSearchButton;
    private int selectedAlbumIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        albumListView = findViewById(R.id.albumListView);
        createButton = findViewById(R.id.createButton);
        deleteButton = findViewById(R.id.deleteButton);
        openButton = findViewById(R.id.openButton);
        renameButton = findViewById(R.id.renameButton);
        handleSearchButton = findViewById(R.id.handleSearchButton);

        albumList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumList);
        albumListView.setAdapter(adapter);
        // Initialize buttons to be disabled
        updateButtonStates(false);
        // load album list
        loadAlbumList();
        // Set listeners
        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAlbumIndex = position; // Update the selected index
                view.setSelected(true); // Highlight the selected item visually
                updateButtonStates(true); // Enable buttons since an item is selected
            }});

        createButton.setOnClickListener(v -> {
                createAlbum();

        });

        deleteButton.setOnClickListener(v -> {
            if (selectedAlbumIndex >= 0) {
                deleteAlbum(albumList.get(selectedAlbumIndex));
            }
            });

        openButton.setOnClickListener(v -> {
            if (selectedAlbumIndex >= 0) {
                openAlbum(albumList.get(selectedAlbumIndex));
            }
        });

        renameButton.setOnClickListener(v ->{
                    if (selectedAlbumIndex >= 0) {
                        renameAlbum(albumList.get(selectedAlbumIndex));
                    }
            });

        handleSearchButton.setOnClickListener(v -> {

                        handleSearchButton();

            });


    }

// Other methods

    private void createAlbum() {
        // Open a dialog to prompt the user for the album name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Album");
        builder.setMessage("Enter the name of the new album:");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString().trim();
                if (!albumName.isEmpty()) {
                    if (!isAlbumExist(albumName)) {
                        Album newAlbum = new Album(albumName);
                        albumList.add(newAlbum);
                        adapter.notifyDataSetChanged();
                        saveAlbumList();
                    } else {
                        showAlert("Error", "Album name already exists.");
                    }
                } else {
                    showAlert("Error", "Please enter a valid album name.");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteAlbum(Album selectedAlbum) {
        if (albumList.contains(selectedAlbum)) {
            albumList.remove(selectedAlbum);
            adapter.notifyDataSetChanged();
            saveAlbumList();
            selectedAlbumIndex = -1;
            updateButtonStates(false); // Update button states to reflect no selection
        } else {
            showAlert("Error", "Album not found in the list.");
        }
    }

    private void openAlbum(Album selectedAlbum) {
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra("selectedAlbum", selectedAlbum);  // Add the bundle to the intent
        startActivity(intent);
    }

    private void renameAlbum(Album selectedAlbum) {
        // Create a dialog to prompt the user for a new album name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename Album");
        builder.setMessage("Enter a new name for the album:");

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(selectedAlbum.getNameOfAlbum());  // Pre-fill with the current album name
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();
                // Check if the new name is not empty and different from the old name
                if (!newName.isEmpty() && !newName.equals(selectedAlbum.getNameOfAlbum())) {
                    if (!isAlbumExist(newName)) {
                        selectedAlbum.setAlbumName(newName);
                        adapter.notifyDataSetChanged();
                        saveAlbumList();
                    } else {
                        showAlert("Error", "An album with this name already exists. Please choose a different name.");
                    }
                } else {
                    if (newName.isEmpty()) {
                        showAlert("Error", "The album name cannot be empty.");
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void handleSearchButton() {
        // Implement the search functionality
    }
    private void updateButtonStates(boolean isEnabled) {
        openButton.setEnabled(isEnabled);
        deleteButton.setEnabled(isEnabled);
        renameButton.setEnabled(isEnabled);
    }
    private boolean isAlbumExist(String albumName) {
        for (Album album : albumList) {
            if (album.getNameOfAlbum().equals(albumName)) {
                return true;
            }
        }
        return false;
    }
    private void saveAlbumList() {
        try {
            FileOutputStream fos = openFileOutput("albums.dat", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(albumList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not save albums.");
        }
    }
    private void loadAlbumList() {
        try {
            FileInputStream fis = openFileInput("albums.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Album> tempList = (ArrayList<Album>) ois.readObject();
            ois.close();
            fis.close();
            albumList.clear(); // Clear the current list
            albumList.addAll(tempList); // Add all loaded albums to the current list
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            albumList = new ArrayList<>(); // No data file found, create a new list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load albums.");
        }
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