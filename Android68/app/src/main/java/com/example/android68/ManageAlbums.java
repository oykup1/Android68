package com.example.android68;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

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

    public ArrayList<Photo> getAllPhotosWithSearchedTags(ArrayList<String> lT, ArrayList<String> pT)
    {
        HashSet<Photo> setToBeReturned = new HashSet<Photo>();
        ArrayList<Photo> listToBeReturned = new ArrayList<Photo>();

        for(Album a: this.listOfAlbums)
        {
            for(Photo p: a.getListOfPhotos())
            {
                if(!setToBeReturned.contains(p) && (hasMatchingLTag(p, lT) || hasMatchingPTag(p, pT)))
                    setToBeReturned.add(p);
            }
        }
        listToBeReturned.addAll(setToBeReturned);

        return listToBeReturned;
    }

    private boolean hasMatchingLTag(Photo p, ArrayList<String> lT)
    {
        for (String searchLTag : lT)
        {
            for (String photoLTag : p.getLocationTags())
            {
                if (photoLTag.toLowerCase().contains(searchLTag.toLowerCase()))
                    return true;
            }
        }
        return false;
    }
    private boolean hasMatchingPTag(Photo p, ArrayList<String> pT)
    {
        for (String searchPTag : pT)
        {
            for (String photoPTag : p.getPersonTags())
            {
                if (photoPTag.toLowerCase().contains(searchPTag.toLowerCase()))
                    return true;
            }
        }
        return false;
    }
}