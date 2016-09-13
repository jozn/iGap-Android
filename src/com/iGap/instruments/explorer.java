// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.instruments;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.iGap.R;
import com.iGap.adapter.G;
import com.iGap.adapter.RecycleAdapterExplorer;
import com.iGap.adapter.StructListItem;


/**
 * 
 * igap file manager
 *
 */

public class explorer extends Activity {

    String                             nextnode;
    int                                rootcount = 0;
    ArrayList<StructListItem>          item;
    ArrayList<String>                  node;          //path of the hierychical directory
    ArrayList<Integer>                 mscroll;

    Button                             btnBack;

    StructListItem                     x;
    RecyclerView                       list;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.layout_explorer);

        list = (RecyclerView) findViewById(R.id.e_listView_explorer);
        list.setItemViewCacheSize(100);
        mLayoutManager = new LinearLayoutManager(explorer.this);
        list.setLayoutManager(mLayoutManager);

        item = new ArrayList<StructListItem>();
        node = new ArrayList<String>();
        mscroll = new ArrayList<Integer>();

        btnBack = (Button) findViewById(R.id.le_btn_back);
        btnBack.setTypeface(G.fontAwesome);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        firstfill();

    }


    private void onItemClickInernal(int position) {

        if (node.size() == rootcount)
            nextnode = node.get(position);
        else
            nextnode = node.get(node.size() - 1) + "/" + item.get(position).name;

        fill(nextnode, position);
    }


    @Override
    public void onBackPressed() {
        int size = node.size();
        if (size == rootcount)
            super.onBackPressed();
        else if (size == rootcount + 1)
            firstfill();
        else {
            node.remove(node.size() - 1);
            fill(node.get(node.size() - 1), 0);
            node.remove(node.size() - 1);
            mscroll.remove(mscroll.size() - 1);
            list.scrollToPosition(mscroll.remove(mscroll.size() - 1));

        }
    }


    void firstfill() {

        item.clear();
        node.clear();
        rootcount = 0;
        mscroll.clear();
        mscroll.add(0);

        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath()).exists()) {
            x = new StructListItem();
            x.name = "Device";
            x.image = R.drawable.j_device;
            x.path = Environment.getExternalStorageDirectory().getAbsolutePath();
            item.add(x);
            node.add(Environment.getExternalStorageDirectory().getAbsolutePath());
            rootcount++;
        }

        if (new File("/storage/extSdCard/").exists()) {
            x = new StructListItem();
            x.name = "SdCard";
            x.image = R.drawable.j_sdcard;
            x.path = "/storage/extSdCard/";
            item.add(x);
            node.add("/storage/extSdCard/");
            rootcount++;
        }
        if (new File("/storage/sdcard1/").exists()) {
            x = new StructListItem();
            x.name = "SdCard1";
            x.image = R.drawable.j_sdcard;
            x.path = "/storage/sdcard1/";
            item.add(x);
            node.add("/storage/sdcard1/");
            rootcount++;
        }
        if (new File("/storage/usbcard1/").exists()) {
            x = new StructListItem();
            x.name = "usbcard";
            x.image = R.drawable.j_usb;
            x.path = "/storage/usbcard1/";
            item.add(x);
            node.add("/storage/usbcard1/");
            rootcount++;
        }

        list.setAdapter(new RecycleAdapterExplorer(item, new com.iGap.interfaces.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                onItemClickInernal(position);
            }
        }));

    }


    void fill(String nextnod, int position) {

        try {

            File fileDir = new File(nextnod);

            if (fileDir.isDirectory())
            {
                mscroll.add(position);

                String[] tmpname = fileDir.list();

                if (tmpname == null) {
                    return;
                }

                item.clear();
                for (int i = 0; i < tmpname.length; i++)
                {
                    if (tmpname[i].startsWith("."))
                        continue;
                    else {
                        File tmp = new File(fileDir.getAbsolutePath() + "/" + tmpname[i]);
                        if (tmp.canRead()) {
                            x = new StructListItem();
                            x.name = tmpname[i];
                            x.image = setPicture(tmp);
                            x.path = tmp.getAbsolutePath();
                            item.add(x);
                        }
                    }
                }

                list.setAdapter(new RecycleAdapterExplorer(item, new com.iGap.interfaces.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        onItemClickInernal(position);
                    }
                }));

                node.add(nextnod);
            }
            else if (fileDir.isFile()) {
                Intent data = new Intent();
                data.setData(Uri.parse(fileDir.getAbsolutePath()));

                setResult(Activity.RESULT_OK, data);

                finish();

            }

        }
        catch (Exception e) {
            Toast.makeText(explorer.this, e.toString(), 7000).show();
        }

    }


    Integer setPicture(File f) {

        Integer x = null;
        String extention;

        if (f.getName().indexOf(".") == -1) {
            extention = "";
        }
        else {
            extention = f.getName().toLowerCase();//.substring(f.getName().length() - 3, f.getName().length());
        }

        if (extention.endsWith("jpg") || extention.endsWith("jpeg") || extention.endsWith("png") || extention.endsWith("bmp"))
            x = R.drawable.j_pic;
        else if (extention.endsWith("apk"))
            x = R.drawable.j_apk;
        else if (extention.endsWith("mp3") || extention.endsWith("ogg") || extention.endsWith("wma"))
            x = R.drawable.j_mp3;
        else if (extention.endsWith("mp4") || extention.endsWith("3gp") || extention.endsWith("avi") || extention.endsWith("mpg") || extention.endsWith("flv") || extention.endsWith("wmv") || extention.endsWith("m4v"))
            x = R.drawable.j_video;
        else if (extention.endsWith("m4a") || extention.endsWith("amr") || extention.endsWith("wav"))
            x = R.drawable.j_audio;
        else if (extention.endsWith("html") || extention.endsWith("htm"))
            x = R.drawable.j_html;
        else if (extention.endsWith("pdf"))
            x = R.drawable.j_pdf;
        else if (extention.endsWith("ppt"))
            x = R.drawable.j_ppt;
        else if (extention.endsWith("snb"))
            x = R.drawable.j_snb;
        else if (extention.endsWith("txt"))
            x = R.drawable.j_txt;
        else if (extention.endsWith("doc"))
            x = R.drawable.j_word;
        else if (extention.endsWith("xls"))
            x = R.drawable.j_xls;
        else if (f.isFile())
            x = R.drawable.j_ect;
        else if (f.isDirectory())
            x = R.drawable.actionbar_icon_myfiles;
        else
            x = R.drawable.j_ect;

        return x;
    }

}
