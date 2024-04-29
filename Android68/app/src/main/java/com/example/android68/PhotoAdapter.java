package com.example.android68;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.*;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class PhotoAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Photo> listOfPhotosInAlbum;

    public PhotoAdapter(Context c, ArrayList<Photo> lOIA)
    {
        this.context = c;
        this.listOfPhotosInAlbum = lOIA;
    }
    @Override
    public int getCount()
    {
        return listOfPhotosInAlbum.size();
    }
    @Override
    public Object getItem(int p)
    {
        return null;
    }
    @Override
    public long getItemId(int p)
    {
        return 0;
    }

    public View getView(int p, View convertView, ViewGroup parent)
    {
        LayoutInflater a = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridV;
        if(convertView == null)
        {
            gridV = new View(context);
            gridV = a.inflate(R.layout.activity_photo, null);

            ImageView photoView = (ImageView) gridV.findViewById(R.id.image_box);
            Uri b = Uri.parse(listOfPhotosInAlbum.get(p).getFilePath());
            photoView.setImageURI(b);
        }
        else
            gridV = (View) convertView;

        return gridV;
    }
}