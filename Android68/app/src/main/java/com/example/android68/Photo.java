package com.example.android68;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
public class Photo implements Parcelable, Serializable {
    private String filePath;
    private String caption;
    private ArrayList<Tag> tags;
    private int id;


    public Photo(int id, String filePath, String caption) {
        this.filePath = filePath;
        this.caption = caption;
        this.tags = new ArrayList<>();
        this.id = id;

    }
    protected Photo(Parcel in) {
        id = in.readInt();
        filePath = in.readString();
        caption = in.readString();
        tags = in.createTypedArrayList(Tag.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(filePath);
        dest.writeString(caption);
        dest.writeTypedList(tags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }


    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
