// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.customviews.TouchImageView;
import com.iGap.helpers.HelperAnimation;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.ImageLoaderProfileHq;
import com.iGap.instruments.ImageLoaderProfileLq;
import com.iGap.instruments.ImagePagerAdapter;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnColorChangedListenerSelect;


/**
 * 
 * namayesh va virayesh etelaate chat karbar
 *
 */

public class UserProfile extends Activity {

    private ArrayList<String>     imageUrlsLq = new ArrayList<String>();
    private ArrayList<String>     imageUrlsHq = new ArrayList<String>();
    private ArrayList<String>     ids         = new ArrayList<String>();
    private ArrayList<String>     img         = new ArrayList<String>();

    private int                   exist;
    private int                   iscontact;
    private int                   imgCount;

    private String                sound;
    private String                userchat;
    private String                userchatname;
    private String                userchatavatar;
    private String                uid;
    private String                chatroomid;
    private String                mobile;

    private Button                btnBack;
    private Button                btnNav;

    private TextView              txtMarker;
    private TextView              txtSendDate;
    private TextView              txtOf;
    private TextView              txtUserName;
    private TextView              txtListUl;
    private TextView              txtNotifi;
    private TextView              txtShared;
    private TextView              txtuserfirstname;

    private ImageView             defaultImage;
    private ImageView             imgusericon;

    private Dialog                dialog;
    private Dialog                pDialog;
    private ImageLoader1          il;
    private ImagePagerAdapter     adapter;
    private ImageLoaderProfileLq  imageProfileLq;
    private ImageLoaderProfileHq  imageProfileHq;
    private DrawableManagerDialog dmDialog;
    private PopupWindow           popUp;
    private ViewPager             pager;
    private JSONParser            jParser     = new JSONParser();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_user_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatroomid = extras.getString("chatroomid");
            userchat = extras.getString("userchat");
            userchatname = extras.getString("userchatname");
            userchatavatar = extras.getString("userchatavatar");
        }

        uid = G.cmd.namayesh4(7, "Contacts");
        dmDialog = new DrawableManagerDialog(UserProfile.this);
        String userch = userchat.split("/")[0];

        exist = G.cmd.getRowCountChatroom(userch, "Chatrooms");

        if (exist != 0) {
            sound = G.cmd.getchatroominfo(userch, 4);
        }

        if (chatroomid != null && !chatroomid.isEmpty() && !chatroomid.equals("null") && !chatroomid.equals("")) {
            try {
                chatroomid = G.cmd.getchatroominfo(userch, 0);
            }
            catch (Exception e) {}
        }

        il = new ImageLoader1(UserProfile.this, G.basicAuth);
        imageProfileLq = new ImageLoaderProfileLq(UserProfile.this, G.basicAuth);
        imageProfileHq = new ImageLoaderProfileHq(UserProfile.this, G.basicAuth);

        intiUi();

        mobile = userchat.split("@")[0];
        iscontact = G.cmd.iscontact(mobile);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        popupoptions(findViewById(R.id.btn_nav));

        return false;
    }


    private void intiUi() {

        final SwitchCompat switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);

        if (exist != 0) {
            if (sound.equals("0")) {
                switchNotification.setChecked(true);
            } else {
                switchNotification.setChecked(false);
            }
        } else {
            switchNotification.setChecked(false);
        }

        switchNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int isexist = G.cmd.getRowCountChatroom(userchat, "Chatrooms");
                if (isexist != 0) {
                    if (sound.equals("1")) {
                        G.cmd.updatechatsound(chatroomid, "0");
                        sound = "0";
                        updateSoundWithUid(userchat, "0");
                    } else {
                        G.cmd.updatechatsound(chatroomid, "1");
                        sound = "1";
                        updateSoundWithUid(userchat, "1");
                    }
                } else {
                    Toast.makeText(UserProfile.this, getString(R.string.you_donot_have_en), Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView txtUserLastTime = (TextView) findViewById(R.id.txt_user_last_time);
        txtUserLastTime.setTypeface(G.robotoLight);
        txtUserLastTime.setText(G.cmd.getLastSeen(userchat.split("@")[0]));

        TextView txtLastLocation = (TextView) findViewById(R.id.txt_last_location);
        txtLastLocation.setTypeface(G.robotoLight);

        TextView txtUserAddress = (TextView) findViewById(R.id.txt_user_address);
        txtUserAddress.setTypeface(G.robotoLight);

        LinearLayout layoutSharedMedia = (LinearLayout) findViewById(R.id.up_ll_shared_media);
        layoutSharedMedia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatroomid != null && !chatroomid.isEmpty() && !chatroomid.equals("null") && !chatroomid.equals("") && !chatroomid.equals("empty")) {
                    Intent intent = new Intent(UserProfile.this, SharedMedia.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("uid", chatroomid);
                    intent.putExtra("basicAuth", G.basicAuth);
                    intent.putExtra("name", userchatname);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });

        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setTypeface(G.fontAwesome);

        btnNav = (Button) findViewById(R.id.btn_nav);
        btnNav.setTypeface(G.fontAwesome);

        txtMarker = (TextView) findViewById(R.id.txt_location_icon);
        txtMarker.setTypeface(G.fontAwesome);

        txtListUl = (TextView) findViewById(R.id.txt_list_ul);
        txtListUl.setTypeface(G.fontAwesome);

        txtNotifi = (TextView) findViewById(R.id.txt_bell_icon);
        txtNotifi.setTypeface(G.fontAwesome);

        txtuserfirstname = (TextView) findViewById(R.id.txt_user_first_name);
        txtuserfirstname.setTypeface(G.robotoLight);
        txtuserfirstname.setText(userchatname);

        TextView txtphonenum = (TextView) findViewById(R.id.txt_phone_num);
        txtphonenum.setTypeface(G.robotoLight);

        TextView txtusername = (TextView) findViewById(R.id.txt_user_name);
        txtusername.setTypeface(G.robotoLight);

        txtShared = (TextView) findViewById(R.id.txt_puzzle_icon);
        txtShared.setTypeface(G.fontAwesome);

        imgusericon = (ImageView) findViewById(R.id.img_user_icon);

        imgusericon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogimageview();
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        if (userchatavatar != null && !userchatavatar.isEmpty() && !userchatavatar.equals("null") && !userchatavatar.equals("") && !userchatavatar.equals("empty")) {
            il.DisplayImage(userchatavatar, R.drawable.difaultimage, imgusericon);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(UserProfile.this, userchatname, imgusericon);
            imgusericon.setImageBitmap(bm);

        }

        String[] splited1 = userchat.split("@");
        String mobile = splited1[0];
        txtphonenum.setText(mobile);

        if ( !uid.equals("0") && uid != null && !uid.isEmpty() && !uid.equals("null")) {
            txtusername.setText(uid);
        } else {
            txtusername.setVisibility(View.GONE);
        }

        btnNav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptions(v);

            }
        });

    }


    @Override
    protected void onDestroy() {
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        super.onDestroy();
    }


    private void dialogimageview() {

        dialog = new Dialog(UserProfile.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_imageview_avatar, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        defaultImage = (ImageView) dialogView.findViewById(R.id.img_dialog_image);

        String defaultPath = G.DIR_DIALOG + "/" + mobile + ".jpg";
        File defaulImageFile = new File(defaultPath);
        if (defaulImageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(defaulImageFile.getAbsolutePath());
            defaultImage.setImageBitmap(bitmap);
        }

        img.add(userchatavatar);
        pager = (ViewPager) dialogView.findViewById(R.id.pager);

        HelperAnimation.helperAnimation(pager);

        Button btnBack = (Button) dialogView.findViewById(R.id.btn_back);
        btnBack.setTypeface(G.fontAwesome);

        Button btnSetting = (Button) dialogView.findViewById(R.id.btn_setting);
        btnSetting.setTypeface(G.fontAwesome);

        btnSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupoptionsImage(v);
            }
        });

        Button btnShare = (Button) dialogView.findViewById(R.id.btn_share);
        btnShare.setTypeface(G.fontAwesome);

        txtOf = (TextView) dialogView.findViewById(R.id.txt_of);
        txtOf.setTypeface(G.robotoBold);

        txtUserName = (TextView) dialogView.findViewById(R.id.txt_user_name);
        txtUserName.setTypeface(G.robotoBold);

        txtSendDate = (TextView) dialogView.findViewById(R.id.txt_send_date);
        txtSendDate.setVisibility(View.GONE);

        final LinearLayout llTop = (LinearLayout) dialogView.findViewById(R.id.ll_top);

        final LinearLayout llBottom = (LinearLayout) dialogView.findViewById(R.id.ll_bottom);

        TouchImageView imgDialogImage = (TouchImageView) dialogView.findViewById(R.id.img_dialog_image);
        imgDialogImage.setMaxZoom(4f);

        imgDialogImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (llBottom.getVisibility() == View.VISIBLE)
                {
                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                    animation1.setDuration(400);
                    llTop.setAnimation(animation1);
                    llBottom.setAnimation(animation1);

                    llTop.setVisibility(ViewGroup.INVISIBLE);
                    llBottom.setVisibility(ViewGroup.INVISIBLE);

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

        txtUserName.setText(userchatname);
        txtOf.setText("1 " + getString(R.string.of_en) + " 1");
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

        new GetUserImages().execute();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    @SuppressWarnings("deprecation")
    private void popupoptionsImage(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_image_dialog, null);
        popUp = new PopupWindow(UserProfile.this);
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
                    View currView = pager.getChildAt(pager.getCurrentItem());
                    ImageSquareProgressBar ims = (ImageSquareProgressBar) currView.findViewById(R.id.image);
                    ImageView image = ims.getImageView();

                    if (Utils.SavePicToDownLoadFolder((Bitmap) image.getTag()))
                        Toast.makeText(getApplicationContext(), getString(R.string.picture_saved_en), Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    Log.e("user profile  ", e.toString());
                }

            }
        });

    }


    @SuppressWarnings("deprecation")
    private void popupoptions(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_single_chat, null);
        popUp = new PopupWindow(UserProfile.this);
        popUp.setContentView(mView);
        popUp.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(1);
        popUp.setWidth(1);
        popUp.setTouchable(true);
        popUp.setFocusable(false);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(new BitmapDrawable());
        popUp.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]);

        Button btnaddcontact = (Button) mView.findViewById(R.id.btn_add_contact);
        Button btnclearchat = (Button) mView.findViewById(R.id.btn_clear_chat);
        Button btndeletechat = (Button) mView.findViewById(R.id.btn_delete_chat);
        Button btnblockcontact = (Button) mView.findViewById(R.id.btn_block_contact);

        if (iscontact != 0) {
            btnaddcontact.setVisibility(View.GONE);
        }

        if (chatroomid.equals("empty")) {
            btnclearchat.setVisibility(View.GONE);
            btndeletechat.setVisibility(View.GONE);
        }

        btnclearchat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.clearchathistory(chatroomid);
                G.cmd.updatechatrooms(userchat, "", "");
                clearWithUid(userchat);

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

            }
        });
        btnblockcontact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                int mobileExist = G.cmd.isMobileExist(mobile);

                if (mobileExist == 1) {
                    new BlockContact().execute(mobile, userchatname);
                } else {
                    addContactForBlock(mobile);
                }

            }
        });
        btndeletechat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }
                ConfirmationDialog cm = new ConfirmationDialog(UserProfile.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            G.cmd.deletechatrooms(chatroomid);
                            startActivity(new Intent(UserProfile.this, MainActivity.class));
                            finish();
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_chat));

            }
        });
    }


    private class GetUserImages extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                JSONObject jsonobj = jParser.getJSONFromUrl(G.url + "users/" + mobile + "/avatars", params, "GET", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                defaultImage.setImageResource(android.R.color.transparent);
                            }
                        });

                        final JSONArray result = json.getJSONArray("result");

                        imgCount = result.length();
                        imageUrlsLq.clear();
                        imageUrlsHq.clear();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);

                            String id = response.getString("id");
                            String lq = response.getString("lq");
                            String hq = response.getString("hq");

                            ids.add(id);
                            imageUrlsLq.add(lq);
                            imageUrlsHq.add(hq);
                        }

                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(UserProfile.this, getString(R.string.success_false_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(UserProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(UserProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    txtOf.setText("1 " + getString(R.string.of_en) + " " + imgCount);
                    if (imgCount == 0) {
                        txtOf.setText("1 " + getString(R.string.of_en) + " 1");
                    }

                    pager.setOnPageChangeListener(new OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int index) {
                            txtOf.setText((index + 1) + " " + getString(R.string.of_en) + " " + imgCount);
                        }


                        @Override
                        public void onPageScrolled(int startIndex, float percent, int pixel) {}


                        @Override
                        public void onPageScrollStateChanged(int arg0) {

                        }
                    });
                    adapter = new ImagePagerAdapter(mobile, dmDialog, ids, imageUrlsLq, imageUrlsHq, imageProfileLq, imageProfileHq, G.basicAuth, UserProfile.this);
                    pager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            });

            if (imgCount == 0) {

                if (userchatavatar == null || userchatavatar.isEmpty() || userchatavatar.equals("null") || userchatavatar.equals("")) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(UserProfile.this, getString(R.string.avatar_not_set_yet_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                G.cmd.updateChatroomAvatar1(imageUrlsLq.get(0), userchat);
                G.cmd.updateChatroomAvatar2(imageUrlsLq.get(0), imageUrlsHq.get(0), mobile);
            }
        }
    }


    private void addContactForBlock(String mobile) {

        String DisplayName = mobile;
        String MobileNumber = mobile;

        if (DisplayName != null & !DisplayName.equals("")) {

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (DisplayName != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
            }

            //------------------------------------------------------ Mobile Number                     
            if (MobileNumber != null) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Toast.makeText(UserProfile.this, getString(R.string.contact_added_en), Toast.LENGTH_SHORT).show();

                new BlockContact().execute(mobile, mobile);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UserProfile.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class BlockContact extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(UserProfile.this);
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

            final String mobile = args[0];

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", mobile));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.url + "fake/users/black-list", params, "POST", G.basicAuth, null);

                try {

                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                G.cmd.addBlockContact(userchatname, mobile, userchatavatar, null, userchat);
                                Toast.makeText(UserProfile.this, getString(R.string.blocked_user_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(UserProfile.this, getString(R.string.success_false_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(UserProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(UserProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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
        }
    }


    private void clearWithUid(String uid) {
        Intent intent = new Intent("cleareWithUidChat");
        intent.putExtra("uid", uid);
        LocalBroadcastManager.getInstance(UserProfile.this).sendBroadcast(intent);
        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(UserProfile.this).sendBroadcast(intent1);
    }


    private void updateSoundWithUid(String uid, String value) {
        Intent intent = new Intent("updateSoundWithUidChat");
        intent.putExtra("uid", uid);
        intent.putExtra("value", value);
        LocalBroadcastManager.getInstance(UserProfile.this).sendBroadcast(intent);
        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(UserProfile.this).sendBroadcast(intent1);
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
