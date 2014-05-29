package com.bienprogramming.pound.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bienprogramming.pound.app.POJO.Color;
import com.bienprogramming.pound.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 5/05/2014.
 */
public class ColorAdapter extends ArrayAdapter<Color> {
    int listResource;

    public ColorAdapter(Context context, int resource, List<Color> objects) {
        super(context, resource, objects);
        listResource = resource;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {

            LayoutInflater vi;

            vi = LayoutInflater.from(getContext());
            v = vi.inflate(listResource, null);

        }

        Color c = getItem(position);

        if (c != null) {

            TextView title = (TextView) v.findViewById(R.id.title);
            LinearLayout colorLayout = (LinearLayout) v.findViewById(R.id.colorLayout);

            if (title != null) {
                title.setText((c.getName()));
            }

            if(colorLayout!=null)
            {
                colorLayout.setBackgroundColor(android.graphics.Color.parseColor(c.getValue()));
            }
        }



        return v;

    }
}
