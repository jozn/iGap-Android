// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.adapter.GroupAdapter;
import com.iGap.customviews.ImageSquareProgressBarDialog;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperGetTime;
import com.iGap.helpers.HelperString;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.ImageLoaderProfile;
import com.iGap.instruments.ImageLoaderProfileLq;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnDeleteComplete;
import com.iGap.interfaces.OnDownloadListener;
import com.iGap.interfaces.OnLoadPhotoListener;


/**
 * 
 * braye namayesh va tanzimat etelate grouh
 *
 */
public class GroupProfile extends Activity {

    //************************* ArrayLists
    private ArrayList<String>     namearray     = new ArrayList<String>();
    private ArrayList<String>     avatarlqarray = new ArrayList<String>();
    private ArrayList<String>     avatarhqarray = new ArrayList<String>();
    private ArrayList<String>     lastseenarray = new ArrayList<String>();
    private ArrayList<String>     usernamearray = new ArrayList<String>();
    private ArrayList<String>     uidarray      = new ArrayList<String>();

    private ArrayList<Boolean>    selectedItem  = new ArrayList<Boolean>();

    private ArrayAdapter<String>  adapter;

    //************************* private values

    private int                   numberOfMembers;

    private String                gchavatarHq;
    private String                gchactive;
    private String                sound;
    private String                gchmembership;
    private String                gchid;
    private String                gchname;
    private String                gchavatar;
    private String                gchdescription;
    private String                members;

    private boolean               success;
    private boolean               enableSelection = false, checkAll = false;

    //************************* private variables

    private Button                btnBack;
    private Button                btnNav;

    private TextView              txtusergroupmemberch;
    private TextView              txtAddMember;
    private TextView              txtListUl;
    private TextView              txtShared;
    private TextView              txtNotifi;

    private LinearLayout          lytKickUser;

    private Dialog                pDialog;
    private Dialog                dialog;
    private ListView              lvgroup;
    private ImageView             imgusericonch;
    private ImageLoader1          il;
    private ImageLoaderProfileLq  imageLoaderProfileLq;
    private PopupWindow           popUp;
    private JSONArray             array1;
    private GroupAdapter          GA;
    private DrawableManagerDialog dmDialog;
    private JSONParser            jParser         = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_group_profile);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gchid = extras.getString("gchid");
            gchname = extras.getString("gchname");
            gchavatar = extras.getString("gchavatar");
            gchavatarHq = extras.getString("gchavatarHq");
            gchdescription = extras.getString("gchdescription");
            gchmembership = extras.getString("gchmembership");
            gchactive = extras.getString("gchactive");
        }
        GA = new GroupAdapter(GroupProfile.this);
        members = G.cmd.selectNumberOfMembers(gchid);
        sound = G.cmd.getgroupchatroomssound(gchid);

        imageLoaderProfileLq = new ImageLoaderProfileLq(GroupProfile.this, G.basicAuth);
        dmDialog = new DrawableManagerDialog(GroupProfile.this);

        il = new ImageLoader1(GroupProfile.this, G.basicAuth);

        intiUi();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        popupoptions(findViewById(R.id.btn_nav_ch));

        return false;
    }


    private void intiUi() {

        array1 = new JSONArray();
        final SwitchCompat switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);
        if (sound.equals("0")) {
            switchNotification.setChecked(true);
        } else {
            switchNotification.setChecked(false);
        }

        switchNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int isexist = G.cmd.isgroupmessageexist("groupchatrooms", gchid);
                if (isexist != 0) {
                    if (sound.equals("1")) {
                        G.cmd.updatesound(gchid, "0");
                        sound = "0";
                    } else {
                        G.cmd.updatesound(gchid, "1");
                        sound = "1";
                    }
                    updatesoundwithuid(gchid, sound);
                } else {
                    Toast.makeText(GroupProfile.this, getString(R.string.you_are_not_member_ofthis_group_en), Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView txtGroupLinkCh = (TextView) findViewById(R.id.txt_group_link_ch);
        txtGroupLinkCh.setTypeface(G.robotoLight);

        TextView txtGroupMember = (TextView) findViewById(R.id.txt_group_member);
        txtGroupMember.setTypeface(G.robotoLight);

        TextView txtAddMemberIcon = (TextView) findViewById(R.id.txt_add_member_icon);
        txtAddMemberIcon.setTypeface(G.fontAwesome);

        txtAddMember = (TextView) findViewById(R.id.txt_add_member);
        txtAddMember.setTypeface(G.fontAwesome);

        btnNav = (Button) findViewById(R.id.btn_nav_ch);
        btnNav.setTypeface(G.fontAwesome);

        imgusericonch = (ImageView) findViewById(R.id.img_user_icon_ch);

        imgusericonch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (gchavatar == null || gchavatar.isEmpty() || gchavatar.equals("null") || gchavatar.equals("")) {
                    Toast.makeText(GroupProfile.this, getString(R.string.group_avatar_not_set_yet_en), Toast.LENGTH_SHORT).show();
                } else {
                    dialogimageview();
                }
            }
        });

        TextView txtgroupname = (TextView) findViewById(R.id.txt_group_name1);
        txtgroupname.setTypeface(G.robotoBold);
        txtgroupname.setText(gchname);

        TextView txtgroupdes = (TextView) findViewById(R.id.txt_group_des);
        txtgroupdes.setTypeface(G.robotoLight);
        txtgroupdes.setText(gchdescription);

        LinearLayout layoutSharedMedia = (LinearLayout) findViewById(R.id.gp_ll_shared_media);
        layoutSharedMedia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupProfile.this, SharedMedia.class);
                intent.putExtra("type", "2");
                intent.putExtra("uid", gchid);
                intent.putExtra("basicAuth", G.basicAuth);
                intent.putExtra("name", gchname);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        txtusergroupmemberch = (TextView) findViewById(R.id.txt_user_group_member_ch);
        txtusergroupmemberch.setTypeface(G.robotoLight);

        members = G.cmd.selectNumberOfMembers(gchid);

        if (members != null && !members.isEmpty() && !members.equals("null") && !members.equals("") && !members.equals("0")) {
            txtusergroupmemberch.setText(members + "/5k");
        } else {
            txtusergroupmemberch.setText(getString(R.string.number_of_member_en));
        }

        LinearLayout llleavegroup = (LinearLayout) findViewById(R.id.ll_leavegroup);
        ScrollView scrollview = (ScrollView) findViewById(R.id.scrollView1);

        llleavegroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConfirmationDialog cm = new ConfirmationDialog(GroupProfile.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            GA.deleteGroup(gchid, G.username, gchmembership, new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {
                                        Intent intent = new Intent(GroupProfile.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_group));

            }
        });

        txtListUl = (TextView) findViewById(R.id.txt_leave_icon_ch);
        txtListUl.setTypeface(G.fontAwesome);

        txtNotifi = (TextView) findViewById(R.id.txt_bell_icon_ch);
        txtNotifi.setTypeface(G.fontAwesome);

        txtShared = (TextView) findViewById(R.id.txt_puzzle_icon_ch);
        txtShared.setTypeface(G.fontAwesome);

        lvgroup = (ListView) findViewById(R.id.lv_group);

        btnBack = (Button) findViewById(R.id.btn_back_ch);
        btnBack.setTypeface(G.fontAwesome);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        if (gchavatar != null && !gchavatar.isEmpty() && !gchavatar.equals("null") && !gchavatar.equals("") && !gchavatar.equals("empty")) {
            il.DisplayImage(gchavatarHq, R.drawable.difaultimage, imgusericonch);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(GroupProfile.this, gchname, imgusericonch);
            imgusericonch.setImageBitmap(bm);

        }
        btnNav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptions(v);

            }
        });

        lytKickUser = (LinearLayout) findViewById(R.id.lyt_kick_user);

        final CheckBox chkAll = (CheckBox) findViewById(R.id.chk_all);
        final TextView txtSelectedItem = (TextView) findViewById(R.id.textView_selected_item);
        chkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( !checkAll) {
                    for (int i = 0; i < selectedItem.size(); i++) {
                        selectedItem.set(i, true);
                    }
                    txtSelectedItem.setText(getString(R.string.select_all_en) + " " + selectedItem.size() + " " + getString(R.string.user_selected_en));
                    checkAll = true;
                } else {
                    for (int i = 0; i < selectedItem.size(); i++) {
                        selectedItem.set(i, false);
                    }
                    txtSelectedItem.setText(getString(R.string.select_all_number_Of_selected_en));
                    checkAll = false;
                }
                adapter.notifyDataSetChanged();
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (gchmembership.equals("1")) {

                    for (int i = 0; i < selectedItem.size(); i++) {

                        if (selectedItem.get(i) == true) {

                            array1.put(usernamearray.get(i));

                        }
                    }
                    new kickuser().execute();
                } else {
                    Toast.makeText(GroupProfile.this, getString(R.string.you_can_not_kick_en), Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvgroup.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (gchmembership.equals("1")) {

                    lytKickUser.setVisibility(View.VISIBLE);

                    enableSelection = true;
                    if (selectedItem.get(position) == true) {
                        selectedItem.set(position, false);
                    } else {
                        selectedItem.set(position, true);
                    }

                    int count = 0;
                    for (int i = 0; i < selectedItem.size(); i++) {
                        if (selectedItem.get(i) == true) {
                            count++;
                        }
                    }

                    if (selectedItem.size() == count) {
                        chkAll.setChecked(true);
                    }

                    if (count == 0) {
                        chkAll.setChecked(false);
                    }

                    txtSelectedItem.setText(getString(R.string.select_all_en) + " " + count + " " + getString(R.string.user_selected_en));

                    adapter.notifyDataSetChanged();

                }

                return false;
            }
        });

        lvgroup.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (enableSelection == true) {
                    if (selectedItem.get(position) == true) {
                        selectedItem.set(position, false);
                    } else {
                        selectedItem.set(position, true);
                    }

                    int count = 0;
                    for (int i = 0; i < selectedItem.size(); i++) {
                        if (selectedItem.get(i) == true) {
                            count++;
                        }
                    }

                    txtSelectedItem.setText(getString(R.string.select_all_en) + " " + count + " " + getString(R.string.user_selected_en));

                    if (selectedItem.size() == count) {
                        chkAll.setChecked(true);
                    }
                    if (count == 0) {
                        chkAll.setChecked(false);
                        txtSelectedItem.setText(getString(R.string.select_all_number_Of_selected_en));
                    }

                    adapter.notifyDataSetChanged();
                }
                else {
                    Intent intent = new Intent(GroupProfile.this, UserProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chatroomid", "");
                    intent.putExtra("userchat", usernamearray.get(position));
                    intent.putExtra("userchatname", namearray.get(position));
                    intent.putExtra("userchatavatar", avatarlqarray.get(position));
                    startActivity(intent);
                }

            }
        });

        txtAddMember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (gchactive.equals("1")) {
                    Intent intent = new Intent(GroupProfile.this, AddMemberToGroup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", gchid);
                    intent.putExtra("name", gchname);
                    intent.putExtra("groupdesc", gchdescription);
                    intent.putExtra("gchmembership", gchmembership);
                    startActivity(intent);

                }

            }
        });
        scrollview.pageScroll(View.FOCUS_UP);//if you move at the middle of the scroll
        scrollview.fullScroll(View.FOCUS_UP);//if you move at the end of the scroll
        scrollview.smoothScrollTo(0, 0);
        getAlldataFromGroupMember();

        new groupinfo().execute();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        enableSelection = false;
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {

        if (enableSelection) {
            enableSelection = false;
            lytKickUser.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

    }


    class getmembers extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                String[] splited1 = gchid.split("@");
                String id = splited1[0];
                JSONObject jsonobj = jParser.getJSONFromUrl(G.chatroommembers + id + "/members", params, "GET", G.basicAuth, null);

                try {
                    //String statuscode  = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");

                    JSONObject json = new JSONObject(jsonstring);
                    success = json.getBoolean(G.TAG_SUCCESS);
                    JSONArray products = json.getJSONArray("result");

                    if (success == true) {

                        deleteRowTableGroupMember();

                        try {
                            uidarray.clear();
                            usernamearray.clear();
                            namearray.clear();
                            avatarlqarray.clear();
                            avatarhqarray.clear();
                            lastseenarray.clear();
                            selectedItem.clear();

                        }
                        catch (Exception e) {}

                        numberOfMembers = products.length();
                        for (int i = 0; i < products.length(); i++) {

                            JSONObject c = products.getJSONObject(i);

                            String username = c.getString("username");
                            String name = c.getString("fullname");
                            //String role = c.getString("role");
                            String avatarlq = c.getString("avatarLq");
                            String avatarhq = c.getString("avatarHq");

                            //String uid = result.getString("uid");
                            String mobile = username.split("@")[0];
                            String userchatname;
                            try {
                                userchatname = G.cmd.namayeshname(1, mobile);
                            }
                            catch (Exception e) {

                                if (name != null && !name.isEmpty() && !name.equals("null") && !name.equals("")) {
                                    userchatname = name;
                                } else {
                                    userchatname = username;
                                }

                            }

                            String lastseen;
                            String s = c.getString("lastSeen");

                            lastseen = HelperGetTime.getStringTime(s, G.cmd);

                            addItemToTableGroupMember("", gchid, username, userchatname, avatarlq, avatarhq, "");

                            namearray.add(userchatname);
                            avatarlqarray.add(avatarlq);
                            avatarhqarray.add(avatarhq);
                            lastseenarray.add(lastseen);
                            usernamearray.add(username);
                            uidarray.add("empty");
                            selectedItem.add(false);

                        }

                    } else {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(GroupProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(GroupProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(GroupProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (success == true) {
                G.cmd.updateGroupMembersNumber(gchid, numberOfMembers + "");
                updateNumberOfMembersInMainList();
                txtusergroupmemberch.setText(numberOfMembers + "/5k");
                setlistviewadapter();
            }
        }

    }


    private void updateNumberOfMembersInMainList() { // PageMessagingGroup & PageMessagingAll
        Intent intent = new Intent("updateGroupMembers");
        intent.putExtra("groupID", gchid);
        LocalBroadcastManager.getInstance(GroupProfile.this).sendBroadcast(intent);

        //===========Update in All

        Intent intentAll = new Intent("updateMembersNumberToAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("ID", gchid);
        intentAll.putExtra("NUMBER_OF_MEMBER", numberOfMembers + "");
        LocalBroadcastManager.getInstance(GroupProfile.this).sendBroadcast(intentAll);
    }


    private void addItemToTableGroupMember(String uid, String groupchatid, String username, String name, String avatarlq, String avatarhq, String lastseen) {

        try {
            G.cmd.addToGroupMember(uid, groupchatid, username, name, avatarlq, avatarhq, lastseen);
        }
        catch (Exception e) {}
    }


    private void deleteRowTableGroupMember() {
        try {
            G.cmd.deleteRowFromGroupMember(gchid);
        }
        catch (Exception e) {}
    }


    private void getAlldataFromGroupMember() {

        try {

            Cursor cu = G.cmd.getdataFromGroupMember(gchid);

            while (cu.moveToNext()) {

                uidarray.add("empty");
                usernamearray.add(cu.getString(3));
                namearray.add(cu.getString(4));
                avatarlqarray.add(cu.getString(5));
                avatarhqarray.add(cu.getString(6));
                lastseenarray.add(cu.getString(7));

                selectedItem.add(false);

            }
            cu.close();
            setlistviewadapter();

        }
        catch (Exception e) {}

    }


    private void setlistviewadapter() {

        adapter = new AA();
        lvgroup.setAdapter(adapter);

        Utility utility = new Utility();
        utility.setListViewHeightBasedOnChildren(lvgroup);

    }


    class AA extends ArrayAdapter<String> {

        public AA() {
            super(GroupProfile.this, R.layout.list_item_allcontacts, namearray);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item_allcontacts, parent, false);

            TextView txtcontacticon = (TextView) row.findViewById(R.id.txt_contacticon);
            txtcontacticon.setTypeface(G.fontAwesome);

            TextView txtname = (TextView) row.findViewById(R.id.txt_name);
            txtname.setTypeface(G.robotoBold);

            TextView txtdesc = (TextView) row.findViewById(R.id.txt_member_des);
            txtdesc.setTypeface(G.robotoLight);

            TextView txtlastseen = (TextView) row.findViewById(R.id.txt_last_seen);
            txtlastseen.setTypeface(G.robotoLight);

            LinearLayout ll_checkbox_selected = (LinearLayout) row.findViewById(R.id.ll_checkbox_selected);
            ll_checkbox_selected.setVisibility(View.INVISIBLE);

            final ImageView img_avatar = (ImageView) row.findViewById(R.id.img_avatar);

            txtcontacticon.setTypeface(G.fontAwesome);
            txtname.setText(namearray.get(position));
            txtlastseen.setText(lastseenarray.get(position));

            if ( !selectedItem.isEmpty()) {
                if (selectedItem.get(position) == true) {
                    ll_checkbox_selected.setVisibility(View.VISIBLE);
                }
            }

            if (avatarlqarray.get(position) != null && !avatarlqarray.get(position).isEmpty() && !avatarlqarray.get(position).equals("null") && !avatarlqarray.get(position).equals("")) {
                il.DisplayImage(avatarlqarray.get(position), R.drawable.difaultimage, img_avatar);
            } else {

                if (namearray.get(position) != null && !namearray.get(position).isEmpty() && !namearray.get(position).equals("null") && !namearray.get(position).equals("")) {

                    HelperDrawAlphabet pf = new HelperDrawAlphabet();
                    Bitmap bm = pf.drawAlphabet(GroupProfile.this, namearray.get(position), imgusericonch);
                    img_avatar.setImageBitmap(bm);

                } else {

                    Bitmap bm = G.utileProg.drawAlphabetOnPicture(img_avatar.getLayoutParams().width, "", "");
                    img_avatar.setImageBitmap(bm);
                }
            }

            return (row);
        }
    }


    @SuppressWarnings("deprecation")
    private void popupoptions(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_group_chat, null);

        Button btnClearHistory = (Button) mView.findViewById(R.id.btn_clear_history);

        btnClearHistory.setVisibility(View.GONE);

        popUp = new PopupWindow(GroupProfile.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btnleftgroup = (Button) mView.findViewById(R.id.btn_left_group);
        Button btngroupinfo = (Button) mView.findViewById(R.id.btn_group_info);
        Button btnaddmember = (Button) mView.findViewById(R.id.btn_add_member);
        Button btneditegroup = (Button) mView.findViewById(R.id.btn_edite_group);

        String editGroup = getString(R.string.edit_group_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btneditegroup.setAllCaps(false);
        } else {
            editGroup = editGroup.substring(0, 1).toUpperCase() + editGroup.substring(1, 5).toLowerCase() + editGroup.substring(5, 6).toUpperCase() + editGroup.substring(7).toLowerCase();
        }
        btneditegroup.setText(editGroup);

        String leftGroup = getString(R.string.left_group_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnleftgroup.setAllCaps(false);
        } else {
            leftGroup = leftGroup.substring(0, 1).toUpperCase() + leftGroup.substring(1, 5).toLowerCase() + leftGroup.substring(5, 6).toUpperCase() + leftGroup.substring(7).toLowerCase();
        }
        btnleftgroup.setText(leftGroup);

        String addMember = getString(R.string.add_member_en);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btnaddmember.setAllCaps(false);
        } else {
            addMember = addMember.substring(0, 1).toUpperCase() + addMember.substring(1, 4).toLowerCase() + addMember.substring(4, 5).toUpperCase() + addMember.substring(5).toLowerCase();
        }
        btnaddmember.setText(addMember);

        if ( !gchactive.equals("1")) {

            btnaddmember.setVisibility(View.GONE);
        }
        btngroupinfo.setVisibility(View.GONE);
        btnleftgroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                ConfirmationDialog cm = new ConfirmationDialog(GroupProfile.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {}


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            GA.deleteGroup(gchid, G.username, gchmembership, new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {
                                        Intent intent = new Intent(GroupProfile.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_group));

            }
        });
        btnaddmember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (gchactive.equals("1")) {

                    if (popUp != null && popUp.isShowing()) {
                        popUp.dismiss();
                    }
                    Intent intent = new Intent(GroupProfile.this, AddMemberToGroup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", gchid);
                    intent.putExtra("name", gchname);
                    intent.putExtra("groupdesc", gchdescription);
                    intent.putExtra("gchmembership", gchmembership);
                    startActivity(intent);
                }

            }
        });

        if (gchmembership == null) {
            gchmembership = G.cmd.select("groupchatrooms", "groupchatid = '" + gchid + "'", 3);

            if (gchmembership != null) {
                if ( !gchmembership.equals("1")) {
                    btneditegroup.setVisibility(View.GONE);
                }
            } else {
                btneditegroup.setVisibility(View.GONE);
            }
        }
        btneditegroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                Intent intent = new Intent(GroupProfile.this, EditGroup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("groupuid", gchid);
                intent.putExtra("groupName", gchname);
                intent.putExtra("groupDesc", gchdescription);
                intent.putExtra("groupavatarlq", gchavatar);
                intent.putExtra("groupavatarhq", gchavatarHq);
                intent.putExtra("groupmembership", gchmembership);
                startActivity(intent);
                finish();

            }
        });

    }


    class groupinfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                String[] splited = gchid.split("@");
                String id = splited[0];
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deletegroup + id, params, "GET", G.basicAuth, null);
                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {
                        JSONObject result = json.getJSONObject("result");
                        String name = result.getString("name");
                        String description = result.getString("description");
                        String avatarHq = result.getString("avatarHq");
                        if (avatarHq != null && !avatarHq.isEmpty() && !avatarHq.equals("null")) {

                        } else {
                            avatarHq = "empty";
                        }
                        String avatarLq = result.getString("avatarLq");
                        if (avatarLq != null && !avatarLq.isEmpty() && !avatarLq.equals("null")) {

                        } else {
                            avatarLq = "empty";
                        }
                        String role = result.getString("role");
                        if (role != null && !role.isEmpty() && !role.equals("null")) {

                        } else {
                            role = "empty";
                        }
                        String current_user = result.getString("current_user");

                        G.cmd.updategroupchat(gchid, name, current_user, avatarLq, description);
                    } else {

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(GroupProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(GroupProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            new getmembers().execute();
        }

    }


    public class Utility {

        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }


    private void dialogimageview() {

        dialog = new Dialog(GroupProfile.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_imageview, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        Button btnBack = (Button) dialogView.findViewById(R.id.btn_back);
        btnBack.setTypeface(G.fontAwesome);

        Button btnSetting = (Button) dialogView.findViewById(R.id.btn_setting);
        btnSetting.setTypeface(G.fontAwesome);

        Button btnShare = (Button) dialogView.findViewById(R.id.btn_share);
        btnShare.setTypeface(G.fontAwesome);

        TextView txtOf = (TextView) dialogView.findViewById(R.id.txt_of);
        txtOf.setTypeface(G.robotoBold);

        TextView txtUserName = (TextView) dialogView.findViewById(R.id.txt_user_name);
        txtUserName.setTypeface(G.robotoBold);

        TextView txtSendDate = (TextView) dialogView.findViewById(R.id.txt_send_date);
        txtSendDate.setTypeface(G.robotoLight);

        final LinearLayout llTop = (LinearLayout) dialogView.findViewById(R.id.ll_top);

        final LinearLayout llBottom = (LinearLayout) dialogView.findViewById(R.id.ll_bottom);

        final ImageSquareProgressBarDialog imgDialogImage = (ImageSquareProgressBarDialog) dialogView.findViewById(R.id.img_dialog_image);

        btnSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptionsImage(v, imgDialogImage);
            }
        });

        imgDialogImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (llBottom.getVisibility() == View.VISIBLE)
                {
                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);

                    llTop.setVisibility(ViewGroup.GONE);
                    llBottom.setVisibility(ViewGroup.GONE);

                }
                else
                {

                    AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);

                    llTop.setVisibility(ViewGroup.VISIBLE);
                    llBottom.setVisibility(ViewGroup.VISIBLE);
                }
            }
        });

        final OnDownloadListener listener = new OnDownloadListener() {

            @Override
            public void onProgressDownload(final int percent, int downloadedSize, int fileSize, boolean completeDownload) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        imgDialogImage.setProgress(percent);
                    }
                });
            }
        };

        String fileName = HelperString.getFileName(gchavatarHq);
        final String filePath = G.DIR_TEMP + "/" + gchid + "_" + fileName;
        String profileImagePath = G.DIR_DIALOG + "/" + gchid + "_" + fileName;
        File imgFile = new File(profileImagePath);

        if (imgFile.exists()) { // agar file ghblan download shode namayesh bede
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgDialogImage.setImageBitmap(bitmap);
        } else {
            int loader = R.drawable.difaultimage;
            OnLoadPhotoListener loadPhotoListener = new OnLoadPhotoListener() {

                @Override
                public void onLoadPhotoListener() {
                    if (gchavatarHq == null) {
                        gchavatarHq = gchavatar;
                    }
                    final ImageLoaderProfile imgLoaderProfile = new ImageLoaderProfile(GroupProfile.this, gchavatarHq, G.basicAuth, filePath, listener, dmDialog, imgDialogImage);

                    imgLoaderProfile.getBitmap();

                }
            };
            imageLoaderProfileLq.DisplayImage(gchavatar, loader, imgDialogImage, loadPhotoListener);
        }

        txtSendDate.setVisibility(View.GONE);
        txtUserName.setText(gchname);
        txtOf.setVisibility(View.GONE);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        LayoutParams lp = new LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    class kickuser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(GroupProfile.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            LayoutParams layoutParams = new LayoutParams();
            layoutParams.copyFrom(pDialog.getWindow().getAttributes());
            layoutParams.width = LayoutParams.WRAP_CONTENT;
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
                String id = gchid.split("@")[0];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.addmembertogroupchat + id + "/members", params, "DELETE-BODY", G.basicAuth, array1.toString());

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    success = json.getBoolean(G.TAG_SUCCESS);

                    if (success == true) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(GroupProfile.this, getString(R.string.user_successfully_kicked_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(GroupProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (final JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(GroupProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (final Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(GroupProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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

                lytKickUser.setVisibility(View.GONE);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        namearray.clear();
                        avatarlqarray.clear();
                        avatarhqarray.clear();
                        lastseenarray.clear();
                        usernamearray.clear();
                        uidarray.clear();

                        selectedItem.clear();

                        for (int i = 0; i < namearray.size(); i++) {

                            selectedItem.add(false);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
                new getmembers().execute();
            }
        }

    }


    @SuppressWarnings("deprecation")
    private void popupoptionsImage(View v, final ImageSquareProgressBarDialog imageView) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_image_dialog, null);
        popUp = new PopupWindow(GroupProfile.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btndeleteavatar = (Button) mView.findViewById(R.id.btn_delete_avatar);
        btndeleteavatar.setVisibility(View.GONE);

        Button btnSaveAvatar = (Button) mView.findViewById(R.id.btn_save_avatar);
        btnSaveAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                try {

                    if (Utils.SavePicToDownLoadFolder((Bitmap) imageView.getTag()))
                        Toast.makeText(getApplicationContext(), getString(R.string.picture_saved_en), Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    Log.e("pic  setting", e.toString());
                }

            }
        });

    }


    private void updatesoundwithuid(String uid, String value) {
        Intent intent = new Intent("updateSoundGroup");
        intent.putExtra("uid", uid);
        intent.putExtra("value", value);
        LocalBroadcastManager.getInstance(GroupProfile.this).sendBroadcast(intent);

        //======UpdateSoundForPageAll

        Intent intentAll = new Intent("updateSoundAll");
        intentAll.putExtra("MODEL", "2");
        intentAll.putExtra("UID", uid);
        intentAll.putExtra("VALUE", value);
        LocalBroadcastManager.getInstance(GroupProfile.this).sendBroadcast(intentAll);
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }

}
