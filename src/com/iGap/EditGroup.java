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
import java.util.Date;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.Crop.Crop;
import com.iGap.adapter.G;
import com.iGap.adapter.GroupAdapter;
import com.iGap.helpers.CorrectImageRotate;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.helpers.HelperGetFileInformation;
import com.iGap.instruments.AndroidMultiPartEntity;
import com.iGap.instruments.AndroidMultiPartEntity.ProgressListener;
import com.iGap.instruments.Config;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.interfaces.OnComplet;
import com.iGap.interfaces.OnDeleteComplete;


/**
 * 
 * namayesh va virayesh etlate groh
 *
 */
public class EditGroup extends Activity {

    private EditText            editTextgroupdescription;
    private EditText            editTextgroupname;

    private String              lq;
    private String              hq;
    private String              groupName;
    private String              groupDescription;
    private String              groupAvatarLq;
    private String              groupAvatarHq;
    private String              groupMembership;
    private String              newGroupName;
    private String              newGroupDescription;
    private String              groupuid;
    private String              filePath;
    private String              imageFileName;
    private String              filePathWithoutCrop;
    private String              json                    = "";
    private static final String TAG_LQ                  = "lq";
    private static final String TAG_HQ                  = "hq";

    private boolean             showDefaultImage        = false;
    private boolean             showDefaultImageCapture = false;

    private ImageView           imageViewpic;
    private Dialog              dialog;
    private Dialog              pDialog;
    public static final int     MEDIA_TYPE_IMAGE        = 1;
    private int                 REQUEST_CAMERA          = 0, SELECT_FILE = 1;
    private Uri                 fileUri;
    private InputStream         is;
    private JSONObject          jObj                    = null;
    private ImageLoader1        il;
    private GroupAdapter        GA;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_edit_group);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupuid = extras.getString("groupuid");
            groupName = extras.getString("groupName");
            groupDescription = extras.getString("groupDesc");
            groupAvatarLq = extras.getString("groupavatarlq");
            groupAvatarHq = extras.getString("groupavatarhq");
            groupMembership = extras.getString("groupmembership");
        }

        GA = new GroupAdapter(EditGroup.this);
        il = new ImageLoader1(EditGroup.this, G.basicAuth);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setTypeface(G.fontAwesome);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setTypeface(G.fontAwesome);
        TextView txttabongroup = (TextView) findViewById(R.id.txt_tabongroup);
        txttabongroup.setTypeface(G.robotoLight);
        TextView txtchannelname = (TextView) findViewById(R.id.txt_groupname);
        txtchannelname.setTypeface(G.robotoLight);
        TextView txtchanneldesc = (TextView) findViewById(R.id.txt_group_desc);
        txtchanneldesc.setTypeface(G.robotoLight);
        TextView txtallmessage = (TextView) findViewById(R.id.txt_all_message);
        txtallmessage.setTypeface(G.robotoLight);

        editTextgroupname = (EditText) findViewById(R.id.editText_group_name);
        editTextgroupdescription = (EditText) findViewById(R.id.editText_group_description);
        editTextgroupname.setTypeface(G.robotoBold);
        editTextgroupdescription.setTypeface(G.robotoBold);
        imageViewpic = (ImageView) findViewById(R.id.imageView_pic);

        editTextgroupname.setText(groupName);
        editTextgroupdescription.setText(groupDescription);

        if (groupAvatarLq != null && !groupAvatarLq.isEmpty() && !groupAvatarLq.equals("null") && !groupAvatarLq.equals("") && !groupAvatarLq.equals("empty")) {
            il.DisplayImage(groupAvatarHq, R.drawable.difaultimage, imageViewpic);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(EditGroup.this, groupName, imageViewpic);
            imageViewpic.setImageBitmap(bm);
        }

        Button btndeletegroup = (Button) findViewById(R.id.btn_delete_group);
        btndeletegroup.setTypeface(G.robotoBold);

        String deleteGroup = getString(R.string.delet_group);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btndeletegroup.setAllCaps(false);
        } else {
            deleteGroup = deleteGroup.substring(0, 1).toUpperCase() + deleteGroup.substring(1, 7).toLowerCase() + deleteGroup.substring(7, 8).toUpperCase() + deleteGroup.substring(8).toLowerCase();
        }
        btndeletegroup.setText(deleteGroup);

        imageViewpic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                choosePhoto();

            }
        });
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                newGroupName = editTextgroupname.getText().toString();
                newGroupDescription = editTextgroupdescription.getText().toString();

                if (newGroupName != null && !newGroupName.isEmpty() && !newGroupName.equals("null") && !newGroupName.equals("")) {

                    if (newGroupDescription != null && !newGroupDescription.isEmpty() && !newGroupDescription.equals("null") && !newGroupDescription.equals("")) {

                        GA.editGroup(groupuid, newGroupName, newGroupDescription, new OnComplet() {

                            @Override
                            public void complet(Boolean result, String message) {

                                if (result) {
                                    if ( !message.equals("")) {
                                        Toast.makeText(EditGroup.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(EditGroup.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(EditGroup.this, message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(EditGroup.this, getString(R.string.please_enter_group_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditGroup.this, getString(R.string.please_enter_group_name_en), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btndeletegroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConfirmationDialog cm = new ConfirmationDialog(EditGroup.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            GA.deleteGroup(groupuid, G.username, groupMembership, new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {
                                        Intent intent = new Intent(EditGroup.this, MainActivity.class);
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
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

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


    public void choosePhoto() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_chooseavatar);
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

        Button btn_delete_avatar_icon = (Button) dialog.findViewById(R.id.btn_delete_avatar_icon);
        btn_delete_avatar_icon.setTypeface(G.fontAwesome);

        Button btn_delete_avatar = (Button) dialog.findViewById(R.id.btn_delete_avatar);
        btn_delete_avatar.setTypeface(G.robotoLight);

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

        btn_delete_avatar_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                GA.deleteAvatar(groupAvatarHq, new OnComplet() {

                    @Override
                    public void complet(Boolean result, String message) {
                        if (result)
                            setdeletedavatar();
                    }
                });

            }
        });
        btn_delete_avatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                GA.deleteAvatar(groupAvatarHq, new OnComplet() {

                    @Override
                    public void complet(Boolean result, String message) {
                        if (result)
                            setdeletedavatar();
                    }
                });

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


    private void onCaptureImageResult() {
        filePath = fileUri.getPath();
        alertdialogupfile();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (showDefaultImage) {
                showDefaultImage = false;
                filePath = filePathWithoutCrop;
                try {
                    alertdialogupfile();
                }
                catch (Exception e) {
                    Toast.makeText(EditGroup.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;

                try {
                    onCaptureImageResult();
                }
                catch (Exception e) {
                    Toast.makeText(EditGroup.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Crop.REQUEST_CROP) {
                showDefaultImage = false;
                showDefaultImageCapture = false;
                try {
                    alertdialogupfile();
                }
                catch (Exception e) {
                    Toast.makeText(EditGroup.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditGroup.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                CorrectImageRotate.correct(HelperGetFileInformation.getFilePathFromUri(fileUri));
                beginCrop(fileUri, destinationCapture);
                showDefaultImageCapture = true;
            }
        }
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditGroup.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upfile, null);
        dialogBuilder.setView(dialogView);

        new UploadFileToServer().execute();

        pDialog = new Dialog(EditGroup.this);
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


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {

        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }


        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            String[] splited1 = groupuid.split("@");
            String id = splited1[0];
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.GROUPCHAT_AVATAR + id + "/avatars");
            httppost.setHeader("Authorization", G.basicAuth);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

                    @Override
                    public void transferred(long num) {

                    }
                });

                File sourceFile = new File(filePath);
                entity.addPart("avatar", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
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
                    catch (Exception e) {}

                    // try parse the string to a JSON object
                    try {
                        jObj = new JSONObject(json);
                    }
                    catch (JSONException e) {}

                    try {
                        Boolean success = jObj.getBoolean(G.TAG_SUCCESS);
                        if (success == true) {
                            JSONObject result = jObj.getJSONObject("result");
                            lq = result.getString(TAG_LQ);
                            hq = result.getString(TAG_HQ);
                            G.cmd.updategroupavatar(groupuid, lq, hq);
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(EditGroup.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                            responseString = "Error occurred! Http Status Code: " + statusCode;
                        }

                    }
                    catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(EditGroup.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
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
            il.DisplayImage(hq, R.drawable.difaultimage, imageViewpic);
            sendgroupMessageToActivity1(EditGroup.this, hq);
            super.onPostExecute(result);
        }

    }


    private void setdeletedavatar() {
        HelperDrawAlphabet pf = new HelperDrawAlphabet();
        Bitmap bm = pf.drawAlphabet(EditGroup.this, groupName, imageViewpic);
        imageViewpic.setImageBitmap(bm);

        sendgroupMessageToActivity1(EditGroup.this, "");
    }


    private void sendgroupMessageToActivity1(Context mContext, String newAvatar) {
        Intent intent = new Intent("SetAvatarChange");
        intent.putExtra("newAvatar", newAvatar);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);

        updateAvatarGroup(groupuid, newAvatar);
    }


    private void updateAvatarGroup(String uid, String avatarlq) {
        Intent intent2 = new Intent("updateAvatarGroup");
        intent2.putExtra("uid", uid);
        intent2.putExtra("avatarlq", avatarlq);
        LocalBroadcastManager.getInstance(EditGroup.this).sendBroadcast(intent2);
        updateAvatarAll(uid, avatarlq);
    }


    private void updateAvatarAll(String uid, String avatar) {
        Intent intent2 = new Intent("updateAvatarAll");
        intent2.putExtra("MODEL", "2");
        intent2.putExtra("AVATAR", avatar);
        intent2.putExtra("UID", uid);
        LocalBroadcastManager.getInstance(EditGroup.this).sendBroadcast(intent2);
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