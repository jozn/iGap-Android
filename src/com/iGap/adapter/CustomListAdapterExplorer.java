// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.iGap.R;


/**
 * 
 * this adapter fill data in listView Country
 * 
 * {@link com.iGap.Register Register}
 *
 */

public class CustomListAdapterExplorer extends ArrayAdapter<String> {

    ArrayList<String> Items;
    Context           mContext;
    LayoutInflater    Inflater;
    ViewHolder        Holder;


    public CustomListAdapterExplorer(Context context, ArrayList<String> items) {
        super(context, R.layout.list_item_select_countries, items);

        mContext = context;
        Items = items;
        Inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }


    private class ViewHolder
    {

        ImageView imageView;
        TextView  txtTitle;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            view = Inflater.inflate(R.layout.list_item_select_countries, null);
            Holder = new ViewHolder();
            Holder.txtTitle = (TextView) view.findViewById(R.id.textView_list_country_name);
            Holder.imageView = (ImageView) view.findViewById(R.id.imageView_list_country_pic);
            view.setTag(Holder);
        }
        else
        {
            view = convertView;
            Holder = (ViewHolder) view.getTag();
        }

        String rowItem = Items.get(position);

        String[] str = rowItem.split(";;");
        String picName = null, countryName = null;
        if (str != null) {
            if (str.length > 0)
                picName = str[0];
            if (str.length > 1)
                countryName = str[1];
        }

        Holder.txtTitle.setText(countryName);
        try {
            int resPic = mContext.getResources().getIdentifier(mContext.getPackageName() + ":drawable/f" + picName, null, null);
            Holder.imageView.setImageResource(resPic);
        }
        catch (Exception e) {

        }

        //	mHolder.imageView.setImageDrawable(getPic(_picName));

        return view;

    }

}