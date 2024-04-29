package com.example.android68;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class Photo implements Serializable {
    private String filePath;
    private String captionNameFile;
    private ArrayList<String>  personTags, locationTags;
    private int id;

    public Photo(String filePath) {
        this.filePath = filePath;
        this.captionNameFile = filePath.substring(filePath.lastIndexOf('/')+1);
        this.personTags = new ArrayList<String>();
        this.locationTags = new ArrayList<String>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCaptionNameFile() {
        return captionNameFile;
    }

    public ArrayList<String> getTags()
    {
        ArrayList<String> combinedTags = new ArrayList<String>();

        for(int i = 0; i < this.getLocationTags().size(); i++)
        {
            String insert = this.getLocationTags().get(i);
            combinedTags.add("Location: " + insert);
        }
        for(int i = 0; i < this.getPersonTags().size(); i++)
        {
            String insert = this.getPersonTags().get(i);
            combinedTags.add("Person: " + insert);
        }
        return combinedTags;
    }

    public ArrayList<String> getLocationTags()
    {
        return locationTags;
    }
    public ArrayList<String> getPersonTags()
    {
        return personTags;
    }

    public void addPTag(String personTag){
        this.personTags.add(personTag.toLowerCase());
    }

    public void deletePTag(String personTag){
        this.personTags.remove(personTag.toLowerCase());
    }

    public void addLTag(String locationTag){
        this.locationTags.add(locationTag.toLowerCase());
    }

    public void deleteLTag(String locationTag){
        this.locationTags.remove(locationTag.toLowerCase());
    }

    @Override
    public int hashCode() {
        return 17*11 + this.getFilePath().hashCode();
    }
}
