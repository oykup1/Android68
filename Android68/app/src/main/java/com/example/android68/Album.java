package com.example.android68;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class Album implements Serializable {
    private String albumName;
    private ArrayList<Photo> listOfPhotos;
    private int numOfPhotos;
    private int id;
    private Photo currPhoto = null;
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
    public void addPhoto(String filePath) {
        Photo nP = new Photo(filePath);
        listOfPhotos.add(nP);
        numOfPhotos++;
    }
    public void setCurrPhoto(Photo currPhoto)
    {
        this.currPhoto = currPhoto;
    }
    public Photo getCurrPhoto()
    {
        return currPhoto;
    }
    public void removePhoto(int i) {
        listOfPhotos.remove(i);
        numOfPhotos--;
    }
    @Override
    public String toString() {
        return this.albumName;
    }

}
