package com.example.android68;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatSpinner;

/**
 * @author Ali Rehman
 * @author Oyku Pul
 */
public class MultipleOptions extends AppCompatSpinner {
    OnItemSelectedListener listener;
    public MultipleOptions(Context c, AttributeSet a)
    {
        super(c, a);
    }

    public void setSelection(int p)
    {
        super.setSelection(p);
        if(listener != null)
            listener.onItemSelected(null, null, p, 0);
    }
    public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener l)
    {
        this.listener = l;
    }
}
