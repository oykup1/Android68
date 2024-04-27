package com.example.android68;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AlbumActivity extends AppCompatActivity {
    private Album currentAlbum;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // Retrieve the album object from the bundle
        Intent intent = getIntent();
        if (intent != null) {
            Album album = intent.getParcelableExtra("album_data", Album.class);
            currentAlbum = album;
        }
    }
    }

