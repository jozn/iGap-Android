// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;


/**
 * 
 * braye namayesh list block shodeh ha ba emkan unblock kardan shomareh delkhah
 *
 */

public class BlockList extends Activity {

    private ArrayList<Item> listitem = new ArrayList<Item>();
    private ListView        registeredlv;
    private EditText        edtsearch;
    private ImageLoader1    il;
    private TextView        txt_block;
    private Dialog          pDialog;
    private boolean         success;
    private JSONParser      jParser  = new JSONParser();
    private int             pos;
    private AlertDialog     alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_block_list);

        il = new ImageLoader1(BlockList.this, G.basicAuth);

        registeredlv = (ListView) findViewById(R.id.registeredlv);
        txt_block = (TextView) findViewById(R.id.txt_str_block);
        edtsearch = (EditText) findViewById(R.id.edt_search);
        edtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                search(s.toString());

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button btnback = (Button) findViewById(R.id.btn_back);
        btnback.setTypeface(G.fontAwesome);
        btnback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        Button btnsearchicon = (Button) findViewById(R.id.btn_searchicon);
        btnsearchicon.setTypeface(G.fontAwesome);
        btnsearchicon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (txt_block.getVisibility() == View.VISIBLE) {

                    AlphaAnimation alpha = new AlphaAnimation(1, 0);
                    alpha.setDuration(200);
                    txt_block.setAnimation(alpha);
                    txt_block.setVisibility(View.GONE);
                    edtsearch.setVisibility(View.VISIBLE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(edtsearch.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

                    edtsearch.requestFocus();
                    ScaleAnimation scale = new ScaleAnimation(0, 1f, 1f, 1f, 1, 1f, 1, 1f);
                    scale.setDuration(200);
                    edtsearch.setAnimation(scale);

                } else {
                    ScaleAnimation scale = new ScaleAnimation(1f, 0, 1f, 1f, 1, 1f, 1, 1f);
                    scale.setDuration(50);
                    edtsearch.setAnimation(scale);
                    edtsearch.setVisibility(View.GONE);
                    edtsearch.clearFocus();
                    txt_block.setVisibility(View.VISIBLE);
                    AlphaAnimation alpha = new AlphaAnimation(0, 1);
                    alpha.setDuration(200);
                    alpha.setStartOffset(100);
                    txt_block.setAnimation(alpha);

                }

            }
        });

        registeredlv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                unblockdialog(position);
                return false;
            }
        });

        loaddata();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    private void loaddata() {

        try {
            listitem.clear();

        }
        catch (Exception e) {}

        int sizeregistered = G.cmd.getblocklistcount();

        for (int i = 0; i < sizeregistered; i++) {

            Item item = new Item();

            item.name = G.cmd.getBlockContact(1, i);
            item.mobile = G.cmd.getBlockContact(2, i);
            item.avatar_lq = G.cmd.getBlockContact(3, i);
            item.avatar_hq = G.cmd.getBlockContact(4, i);
            item.uid = G.cmd.getBlockContact(5, i);

            listitem.add(item);

        }

        registeredlv.setAdapter(new AA(listitem));
    }


    class AA extends ArrayAdapter<Item> {

        private ArrayList<Item> list;


        public AA(ArrayList<Item> list) {
            super(BlockList.this, R.layout.list_item_allcontacts, list);
            this.list = list;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater in = getLayoutInflater();
            View row = in.inflate(R.layout.list_item_allcontacts, parent, false);

            TextView txtcontacticon = (TextView) row.findViewById(R.id.txt_contacticon);
            txtcontacticon.setTypeface(G.fontAwesome);

            TextView txtname = (TextView) row.findViewById(R.id.txt_name);
            txtname.setTypeface(G.robotoBold);

            TextView txtdesc = (TextView) row.findViewById(R.id.txt_member_des);
            txtdesc.setTypeface(G.robotoLight);

            TextView txtlastseen = (TextView) row.findViewById(R.id.txt_last_seen);
            txtlastseen.setVisibility(View.GONE);

            ImageView chbselected = (ImageView) row.findViewById(R.id.imageView_checkbox_selected);
            chbselected.setVisibility(View.GONE);
            final ImageView img_avatar = (ImageView) row.findViewById(R.id.img_avatar);

            txtname.setText(list.get(position).name);
            txtdesc.setText(list.get(position).mobile);

            if (list.get(position).avatar_lq != null && !list.get(position).avatar_lq.isEmpty() && !list.get(position).avatar_lq.equals("null") && !list.get(position).avatar_lq.equals("") && !list.get(position).avatar_lq.equals("empty")) {
                il.DisplayImage(list.get(position).avatar_lq, R.drawable.difaultimage, img_avatar);
            } else {

                if (list.get(position).name != null && !list.get(position).name.isEmpty() && !list.get(position).name.equals("null") && !list.get(position).name.equals("")) {
                    HelperDrawAlphabet pf = new HelperDrawAlphabet();
                    Bitmap bm = pf.drawAlphabet(BlockList.this, list.get(position).name, img_avatar);
                    img_avatar.setImageBitmap(bm);

                } else {

                    Bitmap bm = G.utileProg.drawAlphabetOnPicture(img_avatar.getLayoutParams().width, "", "");
                    img_avatar.setImageBitmap(bm);
                }
            }

            return (row);
        }
    }


    private void unblockdialog(int poss) {
        pos = poss;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BlockList.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_unblock, null);
        dialogBuilder.setView(dialogView);

        Button btnunblock = (Button) dialogView.findViewById(R.id.btn_unblock);

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnunblock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

                new unblock().execute();

            }
        });
    }


    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        super.onDestroy();
    }


    class unblock extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(BlockList.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(pDialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            pDialog.getWindow().setAttributes(layoutParams);
            pDialog.setCancelable(false);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                pDialog.setContentView(R.layout.dialog_wait);
            } else {
                pDialog.setContentView(R.layout.dialog_wait_simple);
            }
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.userblacklist + listitem.get(pos).mobile, params, "DELETE", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {
                        G.cmd.unblockUsers(listitem.get(pos).mobile);
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(BlockList.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(BlockList.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(BlockList.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (success == true) {
                loaddata();
            }
        }

    }


    private class Item {

        String name;
        String mobile;
        String avatar_lq;
        String avatar_hq;
        String uid;

    }


    private void search(String name) {

        try {
            ArrayList<Item> subList = new ArrayList<Item>();

            if (name != null) {
                if (name.trim().length() > 0) {
                    String s = name.trim().toLowerCase();
                    for (int i = 0; i < listitem.size(); i++) {

                        if (listitem.get(i).name.trim().toLowerCase().startsWith(s)) {
                            subList.add(listitem.get(i));
                        }
                    }
                    registeredlv.setAdapter(new AA(subList));

                } else {
                    registeredlv.setAdapter(new AA(listitem));
                }

            } else {
                registeredlv.setAdapter(new AA(listitem));
            }
        }
        catch (Exception e) {}

    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }

}
