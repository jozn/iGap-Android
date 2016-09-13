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
import android.annotation.SuppressLint;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.Crop.Crop;
import com.iGap.adapter.ChannelAdapter;
import com.iGap.adapter.G;
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
 * namayesh va virayesh etlate kanal
 *
 */
public class EditChannel extends Activity {

    private EditText            editTextchannelname;
    private EditText            editTextchanneldescription;

    private String              channelName;
    private String              channelDescription;
    private String              channelAvatarLq;
    private String              channelAvatarHq;
    private String              newChannelName;
    private String              newChannelDescription;
    private String              channeluid;
    private String              filePath;
    private String              imageFileName;
    private String              filePathWithoutCrop;
    private static final String TAG_LQ                  = "lq";
    private static final String TAG_HQ                  = "hq";

    private boolean             showDefaultImage        = false;
    private boolean             showDefaultImageCapture = false;

    private ImageView           imageViewpic;
    private Dialog              dialog;
    private Dialog              pDialog;
    private static final int    MEDIA_TYPE_IMAGE        = 1;
    private int                 REQUEST_CAMERA          = 0, SELECT_FILE = 1;
    private Uri                 fileUri;
    private InputStream         is;
    private String              json                    = "";
    private JSONObject          jObj                    = null;
    private ImageLoader1        imageloader;
    private ChannelAdapter      ChA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_edit_channel);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            channeluid = extras.getString("channeluid");
            channelName = extras.getString("channelName");
            channelDescription = extras.getString("channelDesc");
            channelAvatarLq = extras.getString("channelAvatarLq");
            channelAvatarHq = extras.getString("channelAvatarHq");
        }
        imageloader = new ImageLoader1(EditChannel.this, G.basicAuth);
        ChA = new ChannelAdapter(EditChannel.this);

        Button btn_save = (Button) findViewById(R.id.btn_save);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        TextView txttabonchanel = (TextView) findViewById(R.id.txt_tabonchanel);
        TextView txtchannelname = (TextView) findViewById(R.id.txt_channelname);
        TextView txtchanneldesc = (TextView) findViewById(R.id.txt_channel_desc);
        TextView txtigap = (TextView) findViewById(R.id.txt_igap);
        TextView txtlink = (TextView) findViewById(R.id.txt_link);
        TextView txtchlink = (TextView) findViewById(R.id.txt_chlink);
        TextView txtallmessage = (TextView) findViewById(R.id.txt_all_message);

        txtigap.setTypeface(G.robotoBold);
        txtlink.setTypeface(G.robotoBold);
        txttabonchanel.setTypeface(G.robotoLight);
        txtchannelname.setTypeface(G.robotoLight);
        txtchanneldesc.setTypeface(G.robotoLight);
        txtchlink.setTypeface(G.robotoLight);
        txtallmessage.setTypeface(G.robotoLight);
        btn_save.setTypeface(G.fontAwesome);
        btn_back.setTypeface(G.fontAwesome);
        editTextchannelname = (EditText) findViewById(R.id.editText_channel_name);
        editTextchanneldescription = (EditText) findViewById(R.id.editText_channel_description);
        editTextchannelname.setTypeface(G.robotoBold);
        editTextchanneldescription.setTypeface(G.robotoBold);
        imageViewpic = (ImageView) findViewById(R.id.imageView_pic);

        Button btndeletechannel = (Button) findViewById(R.id.btn_delete_channel);
        btndeletechannel.setTypeface(G.robotoBold);

        String deleteChannel = getString(R.string.delet_channel);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// allcaps is for api 14 and upper
            btndeletechannel.setAllCaps(false);
        } else {
            deleteChannel = deleteChannel.substring(0, 1).toUpperCase() + deleteChannel.substring(1, 7).toLowerCase() + deleteChannel.substring(7, 8).toUpperCase() + deleteChannel.substring(8).toLowerCase();
        }
        btndeletechannel.setText(deleteChannel);

        imageViewpic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                choosePhoto();

            }
        });
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                newChannelName = editTextchannelname.getText().toString();
                newChannelDescription = editTextchanneldescription.getText().toString();

                if (newChannelName != null && !newChannelName.isEmpty() && !newChannelName.equals("null") && !newChannelName.equals("")) {

                    if (newChannelDescription != null && !newChannelDescription.isEmpty() && !newChannelDescription.equals("null") && !newChannelDescription.equals("")) {

                        new ChannelAdapter(EditChannel.this).editChannel(newChannelName, newChannelDescription, channeluid, new OnComplet() {

                            @Override
                            public void complet(Boolean result, String message) {
                                if (result) {
                                    Intent intent = new Intent(EditChannel.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(EditChannel.this, getString(R.string.please_enter_channel_description_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditChannel.this, getString(R.string.please_enter_channel_name_en), Toast.LENGTH_SHORT).show();
                }

            }
        });
        btndeletechannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConfirmationDialog cm = new ConfirmationDialog(EditChannel.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {}


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {
                            ChA.deleteChannel("", channeluid, new OnDeleteComplete() {

                                @Override
                                public void deleteComplete(Boolean result) {
                                    if (result) {
                                        Intent intent = new Intent(EditChannel.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_this_channel));

            }
        });
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        if (channelAvatarLq != null && !channelAvatarLq.isEmpty() && !channelAvatarLq.equals("null") && !channelAvatarLq.equals("")) {
            imageloader.DisplayImage(channelAvatarHq, R.drawable.difaultimage, imageViewpic);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(EditChannel.this, channelName, imageViewpic);
            imageViewpic.setImageBitmap(bm);

        }
        editTextchannelname.setText(channelName);
        editTextchanneldescription.setText(channelDescription);
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
        //dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
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

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
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
                    Toast.makeText(EditChannel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;

                try {
                    onCaptureImageResult();
                }
                catch (Exception e) {
                    Toast.makeText(EditChannel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditChannel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditChannel.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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


    @SuppressLint("InflateParams")
    private void alertdialogupfile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditChannel.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upfile, null);
        dialogBuilder.setView(dialogView);

        new UploadFileToServer().execute();

        pDialog = new Dialog(EditChannel.this);
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


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }


        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.CHANNEL_AVATAR + channeluid + "/avatars");
            httppost.setHeader("Authorization", G.basicAuth);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        //publishProgress((int) ((num / (float) totalSize) * 100));
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
                            channelAvatarLq = result.getString(TAG_LQ);
                            channelAvatarHq = result.getString(TAG_HQ);
                            G.cmd.updatechannelavatar(channeluid, channelAvatarLq, channelAvatarHq);

                        } else {
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
                            Toast.makeText(EditChannel.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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

            imageloader.DisplayImage(channelAvatarHq, R.drawable.difaultimage, imageViewpic);
            sendgroupMessageToActivity1(EditChannel.this, channelAvatarHq);

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            super.onPostExecute(result);
        }

    }


    private void sendgroupMessageToActivity1(Context mContext, String newAvatar) {
        Intent intent = new Intent("SetAvatarChange");
        intent.putExtra("newAvatar", newAvatar);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent1 = new Intent("loadall");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);

        updateAvatarChannel(channeluid, newAvatar);
    }


    private void updateAvatarAll(String uid, String avatar) {
        Intent intent2 = new Intent("updateAvatarAll");
        intent2.putExtra("MODEL", "3");
        intent2.putExtra("AVATAR", avatar);
        intent2.putExtra("UID", uid);
        LocalBroadcastManager.getInstance(EditChannel.this).sendBroadcast(intent2);
    }


    private void updateAvatarChannel(String uid, String avatarlq) {
        Intent intent2 = new Intent("updateAvatarChannel");
        intent2.putExtra("uid", uid);
        intent2.putExtra("avatarlq", avatarlq);
        LocalBroadcastManager.getInstance(EditChannel.this).sendBroadcast(intent2);

        updateAvatarAll(uid, avatarlq);
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
