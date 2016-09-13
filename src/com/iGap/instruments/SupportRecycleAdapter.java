// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iGap.R;
import com.iGap.adapter.G;


/**
 * 
 * adapter recycle view in support page
 *
 */

public class SupportRecycleAdapter extends RecyclerView.Adapter<SupportRecycleAdapter.ViewHolder> {

    private ArrayList<String> msgarray;
    private ArrayList<String> statusarray;
    private ArrayList<String> msgtimearray;
    private ArrayList<String> msgtypearray;
    private ArrayList<String> msgidarray;

    private String            textcolor;
    private String            textsize;
    private String            day;
    private String            month;
    private String            year;

    private ViewHolder        viewholder;
    private Context           mContext;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView     txtSeenNumber;
        public TextView     txttimeday;
        public TextView     messagetxv;
        public TextView     txtreplayfrom;
        public TextView     txtreplaymessage;
        public TextView     txtfilesname;
        public TextView     datetxv;
        public TextView     messagestatusicon;

        public ImageView    img_avatar;
        public ImageView    imgreply;
        public ImageView    img_tregonal;

        public Button       btnDelete;
        public Button       btnAlarm;
        public Button       btnclearhistory;

        public LinearLayout llcontainermain;
        public LinearLayout llcontainer;
        public LinearLayout lltime;
        public LinearLayout llbg;

        public View         roww;
        public View         viewdate;


        public ViewHolder(View row) {
            super(row);
            roww = row;

            txtSeenNumber = (TextView) row.findViewById(R.id.txt_seen_number);

            //Main Layout
            llcontainer = (LinearLayout) row.findViewById(R.id.ll_container);
            llcontainermain = (LinearLayout) row.findViewById(R.id.ll_container_main);

            //timelayout
            lltime = (LinearLayout) row.findViewById(R.id.ll_time);
            txttimeday = (TextView) row.findViewById(R.id.txt_timeday);
            viewdate = row.findViewById(R.id.view_date);

            //bg layout
            llbg = (LinearLayout) row.findViewById(R.id.ll_bg);

            //textv layout
            messagetxv = (TextView) row.findViewById(R.id.message_txv);

            imgreply = (ImageView) row.findViewById(R.id.img_reply);
            txtreplayfrom = (TextView) row.findViewById(R.id.txt_replay_from);
            txtreplaymessage = (TextView) row.findViewById(R.id.txt_replay_message);

            //files layout
            txtfilesname = (TextView) row.findViewById(R.id.txt_filesname);

            messagestatusicon = (TextView) row.findViewById(R.id.message_status_icon);
            datetxv = (TextView) row.findViewById(R.id.date_txv);
            img_tregonal = (ImageView) row.findViewById(R.id.imageView_threegonal);

            txtSeenNumber.setVisibility(View.GONE);

        }
    }


    public SupportRecycleAdapter(ArrayList<String> msgarray1, ArrayList<String> statusarray1, ArrayList<String> msgtimearray1, ArrayList<String> msgtypearray1
                                 , ArrayList<String> msgidarray1, Context context, String year1, String month1, String day1) {
        mContext = context;
        msgarray = msgarray1;
        statusarray = statusarray1;
        msgtimearray = msgtimearray1;
        msgtypearray = msgtypearray1;
        msgidarray = msgidarray1;
        year = year1;
        month = month1;
        day = day1;
        textcolor = G.textColor;
        textsize = G.textSize;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_support, parent, false);
        viewholder = new ViewHolder(v);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int position) {

        if (textcolor != null && !textcolor.isEmpty() && !textcolor.equals("null") && !textcolor.equals("") && !textcolor.equals("0")) {
            h.messagetxv.setTextColor(Color.parseColor(textcolor));
        }

        if (textsize != null && !textsize.isEmpty() && !textsize.equals("null") && !textsize.equals("") && !textsize.equals("0")) {
            h.messagetxv.setTextSize(Float.parseFloat(textsize));
        }

        String[] splitedrowtime = msgtimearray.get(position).split("\\s+");
        String time = splitedrowtime[1];

        try {
            String[] t = splitedrowtime[1].split(":");
            time = t[0] + ":" + t[1];
        }
        catch (Exception e1) {}

        h.datetxv.setText(time);

        if (position != 0) {
            //current
            String[] splitedcurrentposition = msgtimearray.get(position).split("\\s+");
            String currentpositiondate = splitedcurrentposition[0];
            String[] splited_currentpositiondate = currentpositiondate.split("-");
            String currentpositionyear = splited_currentpositiondate[0];
            String currentpositionmonth = splited_currentpositiondate[1];
            String currentpositionday = splited_currentpositiondate[2];
            //befor
            String[] splitedbeforposition = msgtimearray.get(position - 1).split("\\s+");
            String beforpositiondate = splitedbeforposition[0];
            String[] splited_beforpositiondate = beforpositiondate.split("-");
            String beforpositionyear = splited_beforpositiondate[0];
            String beforpositionmonth = splited_beforpositiondate[1];
            String beforpositionday = splited_beforpositiondate[2];
            int intcurrentpositionyear = Integer.parseInt(currentpositionyear);
            int intcurrentpositionmonth = Integer.parseInt(currentpositionmonth);
            int intcurrentpositionday = Integer.parseInt(currentpositionday);
            int intbeforpositionyear = Integer.parseInt(beforpositionyear);
            int intbeforpositionmonth = Integer.parseInt(beforpositionmonth);
            int intbeforpositionday = Integer.parseInt(beforpositionday);
            if (intcurrentpositionyear > intbeforpositionyear) {
                h.lltime.setVisibility(View.VISIBLE);
                if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                    h.txttimeday.setText(mContext.getString(R.string.today_en));
                } else {
                    h.txttimeday.setText("--  " + currentpositiondate + "  --");
                }
            } else {
                if (intcurrentpositionmonth > intbeforpositionmonth) {
                    h.lltime.setVisibility(View.VISIBLE);
                    if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                        h.txttimeday.setText(mContext.getString(R.string.today_en));
                    } else {
                        h.txttimeday.setText("--  " + currentpositiondate + "  --");
                    }
                } else {
                    if (intcurrentpositionday > intbeforpositionday) {
                        h.lltime.setVisibility(View.VISIBLE);
                        if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                            h.txttimeday.setText(mContext.getString(R.string.today_en));
                        } else {
                            h.txttimeday.setText("--  " + currentpositiondate + "  --");
                        }
                    } else {
                        h.lltime.setVisibility(View.GONE);
                        h.viewdate.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            String[] splitedcurrentposition = msgtimearray.get(position).split("\\s+");
            String currentpositiondate = splitedcurrentposition[0];
            String[] splited_currentpositiondate = currentpositiondate.split("-");
            String currentpositionyear = splited_currentpositiondate[0];
            String currentpositionmonth = splited_currentpositiondate[1];
            String currentpositionday = splited_currentpositiondate[2];
            int intcurrentpositionyear = Integer.parseInt(currentpositionyear);
            int intcurrentpositionmonth = Integer.parseInt(currentpositionmonth);
            int intcurrentpositionday = Integer.parseInt(currentpositionday);
            h.lltime.setVisibility(View.VISIBLE);
            if (intcurrentpositionyear == Integer.parseInt(year) && intcurrentpositionmonth == Integer.parseInt(month) && intcurrentpositionday == Integer.parseInt(day)) {
                h.txttimeday.setText(mContext.getString(R.string.today_en));
            } else {
                h.txttimeday.setText("--  " + currentpositiondate + "  --");
            }
        }

        if (statusarray.get(position).equals("0")) {
            h.messagestatusicon.setVisibility(View.VISIBLE);
            h.messagestatusicon.setText(R.string.fa_repeat);
            h.messagestatusicon.setTypeface(G.fontAwesome);
            h.messagestatusicon.setTextColor(mContext.getResources().getColor(R.color.red_light));
        } else if (statusarray.get(position).equals("1")) {
            h.messagestatusicon.setVisibility(View.VISIBLE);
            h.messagestatusicon.setTypeface(G.fontAwesome);
            h.messagestatusicon.setText(R.string.fa_check_square_o);
            h.messagestatusicon.setTextColor(mContext.getResources().getColor(R.color.maingrey));
        } else if (statusarray.get(position).equals("2")) {
            h.messagestatusicon.setVisibility(View.VISIBLE);
            h.messagestatusicon.setTypeface(G.fontAwesome);
            h.messagestatusicon.setText(R.string.fa_check_square);
            h.messagestatusicon.setTextColor(mContext.getResources().getColor(R.color.orang));
        } else if (statusarray.get(position).equals("3")) {
            h.messagestatusicon.setVisibility(View.VISIBLE);
            h.messagestatusicon.setTypeface(G.fontAwesome);
            h.messagestatusicon.setText(R.string.fa_eye);
            h.messagestatusicon.setTextColor(mContext.getResources().getColor(R.color.light_green));
        } else {
            h.messagestatusicon.setVisibility(View.GONE);
        }

        if (msgtypearray.get(position).equals("1")) {

            h.llcontainer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            h.llcontainer.setGravity(Gravity.LEFT);
            h.roww.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            h.img_tregonal.setImageResource(R.drawable.podrecive);
            h.llbg.setBackgroundResource(R.drawable.chatboxrecive);

            h.llcontainermain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            h.llcontainermain.setGravity(Gravity.LEFT);
        } else {

            h.llcontainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            h.llcontainer.setGravity(Gravity.RIGHT);
            h.roww.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            h.img_tregonal.setImageResource(R.drawable.pod3);
            h.llbg.setBackgroundResource(R.drawable.chatboxsend);

            h.llcontainermain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            h.llcontainermain.setGravity(Gravity.RIGHT);

        }

        h.messagetxv.setText(msgarray.get(position));

    }


    @Override
    public int getItemCount() {
        return msgidarray.size();
    }


    public void newPost(String msg, String status, String msgtime, String msgtype, String msgid) {

        msgarray.add(msg);
        statusarray.add(status);
        msgtimearray.add(msgtime);
        msgtypearray.add(msgtype);
        msgidarray.add(msgid);
        notifyDataSetChanged();

    }

}