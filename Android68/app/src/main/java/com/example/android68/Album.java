package com.example.android68;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Parcelable, Serializable {
    private String albumName;
    private ArrayList<Photo> listOfPhotos;
    private int numOfPhotos;
    private int id;
    public Album(String name) {
        this.albumName = name;
        this.listOfPhotos = new ArrayList<>();
        this.numOfPhotos = 0;
    }
    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNameOfAlbum() {
        return albumName;
    }
    public void setAlbumName(String name) {
        this.albumName = name;
    }
    public void renameAlbum(String name) {
        this.albumName = name;
    }
    public int getNumOfPhotos() {
        return numOfPhotos;
    }
    public ArrayList<Photo> getListOfPhotos() {
        return listOfPhotos;
    }
    public void addPhoto(Photo photo) {
        listOfPhotos.add(photo);
        numOfPhotos++;
    }
    public void removePhoto(Photo i) {
        listOfPhotos.remove(i);
        numOfPhotos--;
    }
    @Override
    public String toString() {
        return this.albumName;
    }

}
