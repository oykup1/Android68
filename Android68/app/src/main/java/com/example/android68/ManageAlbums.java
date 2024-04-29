package com.example.android68;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class ManageAlbums implements Serializable{
    public static final long serialVersionUID = 42L;

    private ArrayList<Album> listOfAlbums;
    private Album currAlbum;

    public ManageAlbums()
    {
        listOfAlbums = new ArrayList<Album>();
    }

    public void addAlbumToList(Album a)
    {
        listOfAlbums.add(a);
    }

    public void removeAlbumFromList(int i)
    {
        listOfAlbums.remove(i);
    }

    public ArrayList<Album> getListOfAlbums()
    {
        return listOfAlbums;
    }

    public Album getCurrntAlbum()
    {
        return currAlbum;
    }

    public void setCurrAlbum(Album cA)
    {
        currAlbum = cA;
    }
}