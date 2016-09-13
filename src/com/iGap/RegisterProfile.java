// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.Crop.Crop;
import com.iGap.adapter.G;
import com.iGap.customviews.CircularImageView;
import com.iGap.helpers.CorrectImageRotate;
import com.iGap.helpers.HelperGetFileInformation;
import com.iGap.instruments.AndroidMultiPartEntity;
import com.iGap.instruments.AndroidMultiPartEntity.ProgressListener;
import com.iGap.instruments.Config;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;


/**
 * 
 * sabtename karbar ba vorude etelaate name va email va jensiat va picture
 *
 */

public class RegisterProfile extends Activity {

    private String[]           lastSeen;
    private String[]           fullname;
    private String[]           mobile;
    private String[]           registered;
    private String[]           avatar_hq;
    private String[]           avatar_lq;
    private String[]           uid;
    private String[]           contactid;

    private String             lq;
    private String             hq;
    private String             selectedImagePath;
    private String             uname;
    private String             email;
    private String             username;
    private String             token;
    private String             json                    = "";
    private String             filePath                = "0";
    private String             mGender                 = "1";
    public static final String TAG_LQ                  = "lq";
    public static final String TAG_HQ                  = "hq";

    private String             imageFileName;
    private String             filePathWithoutCrop;

    private boolean            showDefaultImage        = false;
    private boolean            showDefaultImageCapture = false;

    private int                REQUEST_CAMERA          = 0, SELECT_FILE = 1;
    private long               totalSize               = 0;

    private EditText           mUserName, mEmailAddress;

    private Uri                fileUri;
    private InputStream        is;
    private JSONObject         jObj                    = null;
    private TextView           gener_icon_tx;
    private Spinner            selectgenersp;
    private CircularImageView  imgChooseAvatar;
    private JSONObject         Parent;
    private Dialog             pDialog;
    private Dialog             dialog;
    private ArrayList<String>  list2, list3;
    private JSONArray          array;
    private JSONArray          products                = null;
    private JSONParser         jParser                 = new JSONParser();

    protected static final int SELECT_PICTURE          = 1;
    protected static final int CAMERA_PIC_REQUEST      = 0;
    public static final int    MEDIA_TYPE_IMAGE        = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_register_profile);

        gener_icon_tx = (TextView) findViewById(R.id.gener_icon_tx);
        gener_icon_tx.setTypeface(G.fontAwesome);

        TextView txtcirlce1 = (TextView) findViewById(R.id.txt_cirlce1);
        txtcirlce1.setTypeface(G.fontAwesome);
        TextView txtcirlce2 = (TextView) findViewById(R.id.txt_cirlce2);
        txtcirlce2.setTypeface(G.fontAwesome);
        TextView txtcirlce3 = (TextView) findViewById(R.id.txt_cirlce3);
        txtcirlce3.setTypeface(G.fontAwesome);
        TextView txtProfileReg = (TextView) findViewById(R.id.txt_profile_reg);
        txtProfileReg.setTypeface(G.robotoBold);

        TextView txtLine1 = (TextView) findViewById(R.id.txt_line1);
        txtLine1.setTypeface(G.robotoLight);

        TextView txtLine2 = (TextView) findViewById(R.id.txt_line2);
        txtLine2.setTypeface(G.robotoLight);

        TextView txtLine3 = (TextView) findViewById(R.id.txt_line3);
        txtLine3.setTypeface(G.robotoLight);

        TextView txtYourName = (TextView) findViewById(R.id.txt_your_name);
        txtYourName.setTypeface(G.robotoBold);

        TextView txtEmail = (TextView) findViewById(R.id.txt_email);
        txtEmail.setTypeface(G.robotoBold);

        TextView txtGener = (TextView) findViewById(R.id.txt_gener);
        txtGener.setTypeface(G.robotoBold);

        Button mProfile_GoButton = (Button) findViewById(R.id.phone_continue_btn);
        mProfile_GoButton.setTypeface(G.robotoBold);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            token = extras.getString("token");
            G.basicAuth = "Basic " + Base64.encodeToString((username + ":" + token).getBytes(), Base64.NO_WRAP);

        }

        mUserName = (EditText) findViewById(R.id.your_name_etx);
        mUserName.setTypeface(G.robotoLight);
        mEmailAddress = (EditText) findViewById(R.id.email_address_etx);
        mEmailAddress.setTypeface(G.robotoLight);
        selectgenersp = (Spinner) findViewById(R.id.select_gener_sp);
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.female_en));
        list.add(getString(R.string.male_en));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(G.context, R.layout.sp_item, R.id.sp_text, list);

        selectgenersp.setAdapter(dataAdapter);
        selectgenersp.setSelection(1);
        imgChooseAvatar = (CircularImageView) findViewById(R.id.img_choose_avatar);
        imgChooseAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showalert();

            }
        });

        mProfile_GoButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                uname = mUserName.getText().toString();
                email = mEmailAddress.getText().toString();

                if (selectgenersp.getSelectedItemPosition() == 0) {
                    mGender = "0";
                } else {
                    mGender = "1";
                }

                if (uname != null && !uname.isEmpty() && !uname.equals("null") && !uname.equals("")) {
                    if (email != null && !email.isEmpty() && !email.equals("null") && !email.equals("")) {
                        if (email.contains("@") && email.contains(".")) {
                            if (uname.length() >= 3) {

                                if (filePath != null && !filePath.isEmpty() && !filePath.equals("null") && !filePath.equals("") && !filePath.equals("0")) {
                                    alertdialogupfile();
                                } else {
                                    new LoadAllgoroh().execute();
                                }

                            } else {
                                Toast.makeText(RegisterProfile.this, getString(R.string.biger_three_chrecter_en), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterProfile.this, getString(R.string.please_enter_Correct_email_en), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterProfile.this, getString(R.string.please_enter_your_email_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterProfile.this, getString(R.string.please_enter_your_name_en), Toast.LENGTH_SHORT).show();
                }

            }
        });
        gener_icon_tx.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectgenersp.performClick();

            }
        });
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();
        list3 = new ArrayList<String>();
        Cursor phones = RegisterProfile.this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactId = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));

            list.add(name);
            list2.add(phoneNumber);
            list3.add(contactId);
        }
        phones.close();

        Parent = new JSONObject();
        array = new JSONArray();

        for (int i = 0; i < list.size(); i++)
        {
            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("fullname", list.get(i));
                jsonObj.put("mobile", list2.get(i));
                jsonObj.put("contactId", list3.get(i));
            }
            catch (JSONException e) {

            }

            array.put(jsonObj);
            try {
                Parent.put("contacts", array);
            }
            catch (JSONException e) {}
        }

        new sendcontacts().execute();
    }


    public void showalert() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_choosepic);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txt_choose = (TextView) dialog.findViewById(R.id.txt_choose);
        txt_choose.setTypeface(G.robotoBold);

        Button btn_choose_gallery_icon = (Button) dialog.findViewById(R.id.btn_choose_gallery_icon);
        btn_choose_gallery_icon.setTypeface(G.fontAwesome);

        Button btn_choose_gallery = (Button) dialog.findViewById(R.id.btn_choose_gallery);
        btn_choose_gallery.setTypeface(G.robotoLight);

        Button btn_take_photo_icon = (Button) dialog.findViewById(R.id.btn_take_photo_icon);
        btn_take_photo_icon.setTypeface(G.fontAwesome);

        Button btn_take_photo = (Button) dialog.findViewById(R.id.btn_take_photo);
        btn_take_photo.setTypeface(G.robotoLight);

        Button btn_use_avatar_icon = (Button) dialog.findViewById(R.id.btn_use_avatar_icon);
        btn_use_avatar_icon.setTypeface(G.fontAwesome);

        Button btn_use_avatar = (Button) dialog.findViewById(R.id.btn_use_avatar);
        btn_use_avatar.setTypeface(G.robotoLight);

        Button btn_not_now = (Button) dialog.findViewById(R.id.btn_not_now);
        btn_not_now.setTypeface(G.robotoBold);

        btn_not_now.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

        btn_choose_gallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture_en)), SELECT_FILE);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });
        btn_choose_gallery_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture_en)), SELECT_FILE);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });

        btn_take_photo_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQUEST_CAMERA);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        });
        btn_take_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQUEST_CAMERA);

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


    private void beginCrop(Uri source, Uri destination) {
        Crop.of(source, destination).asSquare().start(this);
    }


    private void onCaptureImageResultCrop() {
        imgChooseAvatar.setImageBitmap(CorrectImageRotate.correct(filePath));
    }


    private void onCaptureImageResult() {
        filePath = fileUri.getPath();
        imgChooseAvatar.setImageBitmap(CorrectImageRotate.correct(filePath));
    }


    private void onSelectFromGalleryResultCrop() {
        imgChooseAvatar.setImageBitmap(CorrectImageRotate.correct(filePath));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (showDefaultImage) {
                showDefaultImage = false;
                filePath = filePathWithoutCrop;
                try {
                    onSelectFromGalleryResultCrop();
                }
                catch (Exception e) {
                    Toast.makeText(RegisterProfile.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;

                try {
                    onCaptureImageResult();
                }
                catch (Exception e) {
                    Toast.makeText(RegisterProfile.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Crop.REQUEST_CROP) {
                showDefaultImage = false;
                showDefaultImageCapture = false;
                try {
                    onCaptureImageResultCrop();
                }
                catch (Exception e) {
                    Toast.makeText(RegisterProfile.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (requestCode == SELECT_FILE) {
                showDefaultImage = true;

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            filePathWithoutCrop = HelperGetFileInformation.getFilePathFromUri(data.getData());
                            showDefaultImage = true;
                        }
                        catch (Exception e) {
                            Toast.makeText(RegisterProfile.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                imageFileName = "" + System.currentTimeMillis();
                filePath = G.DIR_CROP + "/" + imageFileName + ".jpg";
                Uri destination = Uri.fromFile(new File(filePath));
                beginCrop(data.getData(), destination);

            } else if (requestCode == REQUEST_CAMERA) {

                imageFileName = "" + System.currentTimeMillis();
                filePath = G.DIR_CROP + "/" + imageFileName + ".jpg";
                Uri destinationCapture = Uri.fromFile(new File(filePath));
                beginCrop(fileUri, destinationCapture);
                showDefaultImageCapture = true;

            }
        }
    }


    @Override
    protected void onDestroy() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        super.onDestroy();
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if ( !mediaStorageDir.exists()) {
            if ( !mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    private void alertdialogupfile() {
        new UploadFileToServer().execute();
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new Dialog(RegisterProfile.this);
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
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }


        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
            httppost.setHeader("Authorization", G.basicAuth);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                File sourceFile = new File(filePath);
                entity.addPart("avatar", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                is = r_entity.getContent();
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 201) {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        json = sb.toString();

                    }
                    catch (Exception e) {

                    }

                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    }
                    catch (JSONException e) {

                    }

                    try {
                        Boolean success = jObj.getBoolean(G.TAG_SUCCESS);
                        if (success == true) {
                            JSONObject result = jObj.getJSONObject("result");
                            lq = result.getString(TAG_LQ);
                            hq = result.getString(TAG_HQ);

                        } else {
                            responseString = "Error occurred! Http Status Code: " + statusCode;
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            catch (ClientProtocolException e) {
                responseString = e.toString();
            }
            catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }


        @Override
        protected void onPostExecute(String result) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            new LoadAllgoroh().execute();
            super.onPostExecute(result);
        }

    }


    class LoadAllgoroh extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(RegisterProfile.this);
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
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fullname", uname));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("gender", mGender));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.registerprofile, params, "PUT", G.basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {
                        G.cmd.resetTables("info");
                        G.cmd.adduserinfo(username, token, G.basicAuth, lq, hq, uname, mGender);
                        G.username = username;
                        G.name = uname;
                        Intent intent = new Intent(RegisterProfile.this, MainActivity.class);
                        intent.putExtra("firstTime", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {

                        if (statuscode.equals("401")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(RegisterProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (statuscode.equals("400")) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(RegisterProfile.this, getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (statuscode.equals("500")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(RegisterProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(RegisterProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(G.context, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(G.context, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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


    class sendcontacts extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.allcontact, params, "BODY", G.basicAuth, array.toString());

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean("success");

                    if (success) {
                        JSONObject result = json.getJSONObject("result");

                        products = result.getJSONArray("contacts");
                        int save = products.length();

                        contactid = new String[save];
                        mobile = new String[save];
                        registered = new String[save];
                        avatar_hq = new String[save];
                        avatar_lq = new String[save];
                        uid = new String[save];
                        lastSeen = new String[save];
                        fullname = new String[save];

                        for (int i = 0; i < save; i++) {
                            JSONObject c = products.getJSONObject(i);
                            contactid[i] = c.getString("contactId");
                            mobile[i] = c.getString("mobile");
                            registered[i] = c.getString("registered");
                            avatar_hq[i] = c.getString("avatar_hq");
                            avatar_lq[i] = c.getString("avatar_lq");
                            uid[i] = c.getString("uid");
                            lastSeen[i] = c.getString("lastSeen");

                            Uri uri = Uri.parse(Contacts.CONTENT_LOOKUP_URI + "/" + contactid[i]);
                            String[] projection = new String[]{ Contacts._ID, Contacts.DISPLAY_NAME, };
                            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                            if ( !cursor.moveToNext()) // move to first (and only) row.
                                throw new IllegalStateException("contact no longer exists for key");
                            String name = cursor.getString(1);

                            cursor.close();
                            fullname[i] = name;

                            int exist = G.cmd.iscontact(mobile[i]);

                            if (exist > 0) {
                                G.cmd.updateContacts2(fullname[i], mobile[i], "1", avatar_lq[i], avatar_hq[i], lastSeen[i], uid[i]);
                            } else {
                                if (registered[i].equals("true")) {
                                    G.cmd.AddContacts(fullname[i], mobile[i], "1", avatar_lq[i], avatar_hq[i], lastSeen[i], uid[i]);
                                } else {
                                    G.cmd.AddContacts(fullname[i], mobile[i], "0", avatar_lq[i], avatar_hq[i], lastSeen[i], uid[i]);
                                }
                            }

                        }

                    } else {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(RegisterProfile.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(RegisterProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(RegisterProfile.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
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
