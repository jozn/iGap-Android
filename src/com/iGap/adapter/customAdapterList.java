// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.R;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.instruments.ImageLoader1;


/**
 * this adapter use for show contact information
 * 
 * 
 * {@link com.iGap.AddMemberToGroup AddMemberToGroup} , {@link com.iGap.InviteAdmin InviteAdmin} , {@link com.iGap.InviteMemberToChannel InviteMemberToChannel}
 */

public class customAdapterList extends ArrayAdapter<String> {

    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> lastSeen;
    public ArrayList<Boolean> checks;

    public int                animationState = -1;
    private Context           mContext;
    private ImageLoader1      il;


    public customAdapterList(Context context, ArrayList<String> image, ArrayList<String> name, ArrayList<Boolean> check, ArrayList<String> lastSeen) {
        super(context, R.layout.list_item_allcontacts, name);
        mContext = context;
        images = image;
        names = name;
        checks = check;
        this.lastSeen = lastSeen;
        il = new ImageLoader1(mContext, G.basicAuth);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_allcontacts, null, true);
        final ImageView ivPicture = (ImageView) rowView.findViewById(R.id.img_avatar);

        TextView txt_contacticon = (TextView) rowView.findViewById(R.id.txt_contacticon);
        txt_contacticon.setTypeface(G.fontAwesome);

        ImageView ivCheckBoxSelected = (ImageView) rowView.findViewById(R.id.imageView_checkbox_selected);
        LinearLayout layoutCheckbox = (LinearLayout) rowView.findViewById(R.id.ll_checkbox_selected);
        TextView txtName = (TextView) rowView.findViewById(R.id.txt_name);
        txtName.setTypeface(G.robotoBold);

        TextView txtdesc = (TextView) rowView.findViewById(R.id.txt_member_des);
        txtdesc.setTypeface(G.robotoLight);

        TextView txtLastSeen = (TextView) rowView.findViewById(R.id.txt_last_seen);
        txtLastSeen.setTypeface(G.robotoLight);
        txtLastSeen.setText(lastSeen.get(position));

        if (images.get(position) != null && !images.get(position).isEmpty() && !images.get(position).equals("null") && !images.get(position).equals("")) {
            il.DisplayImage(images.get(position), R.drawable.difaultimage, ivPicture);
        } else {
            if (names != null && !names.isEmpty() && !names.equals("null") && !names.equals("")) {
                HelperDrawAlphabet pf = new HelperDrawAlphabet();
                Bitmap bm = pf.drawAlphabet(mContext, names.get(position), ivPicture);
                ivPicture.setImageBitmap(bm);

            } else {
                Bitmap bm = G.utileProg.drawAlphabetOnPicture(ivPicture.getLayoutParams().width, "", "");
                ivPicture.setImageBitmap(bm);
            }
        }

        txtName.setText(names.get(position));

        Animation animUp = AnimationUtils.loadAnimation(mContext, R.anim.checkup);
        Animation animDown = AnimationUtils.loadAnimation(mContext, R.anim.checkdown);

        if (checks.get(position) == true) {
            ivCheckBoxSelected.setVisibility(View.VISIBLE);
            layoutCheckbox.setVisibility(View.VISIBLE);
        }
        else {
            ivCheckBoxSelected.setVisibility(View.INVISIBLE);
            layoutCheckbox.setVisibility(View.INVISIBLE);
        }

        if (animationState == position) {

            if (ivCheckBoxSelected.getVisibility() == View.VISIBLE)
                ivCheckBoxSelected.startAnimation(animUp);
            else {
                ivCheckBoxSelected.startAnimation(animDown);
            }
        }

        return rowView;

    }

}
