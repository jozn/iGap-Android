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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.SwitchCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.Crop.Crop;
import com.iGap.adapter.DrawableManagerDialog;
import com.iGap.adapter.G;
import com.iGap.customviews.ImageSquareProgressBar;
import com.iGap.customviews.TouchImageView;
import com.iGap.helpers.CorrectImageRotate;
import com.iGap.helpers.HelperDrawAlphabet;
import com.iGap.instruments.AndroidMultiPartEntity;
import com.iGap.instruments.AndroidMultiPartEntity.ProgressListener;
import com.iGap.instruments.ColorPiker;
import com.iGap.instruments.Config;
import com.iGap.instruments.ImageLoader1;
import com.iGap.instruments.ImageLoaderProfileHq;
import com.iGap.instruments.ImageLoaderProfileLq;
import com.iGap.instruments.ImagePagerAdapter;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnColorChangedListenerSelect;


/**
 * sabte tanzimat karbar braye mohit barnameh
 */

public class Setting extends Activity {

    //************************* ArrayLists
    private ArrayList<String>     imageUrlsLq             = new ArrayList<String>();
    private ArrayList<String>     imageUrlsHq             = new ArrayList<String>();
    private ArrayList<String>     imageID                 = new ArrayList<String>();
    private ArrayList<String>     img                     = new ArrayList<String>();

    //************************* private values
    private int                   ring;
    private int                   imgCount;
    private int                   pos;
    public static final int       MEDIA_TYPE_IMAGE        = 2;

    private String                gender;
    private String                fullname;
    private String                lq;
    private String                hq;
    private String                avatar;
    private String                selectedImagePath;
    private String                imageFileName;
    private String                imageFileNameWithoutCrop;
    private String                filePathWithoutCrop;
    private String                json                    = "";
    private String                filePath                = "0";

    //************************* private variables
    private TextView              txtOf;
    private TextView              txtUserName;
    private TextView              txtSendDate;

    private ImageView             imgmanage;
    private ImageView             imgSettingimage;

    private EditText              editTextname;

    private boolean               needCrop                = false;
    private boolean               showDefaultImage        = false;
    private boolean               showDefaultImageCapture = false;

    private Dialog                dialog;
    private Dialog                pDialog;
    private ImageLoaderProfileHq  imageProfileHq;
    private ImageLoaderProfileLq  imageProfileLq;
    private ImageLoader1          il;
    private Uri                   fileUri;
    private PopupWindow           popUp;
    private InputStream           is;
    private ImagePagerAdapter     adapter;
    private MediaPlayer           mp;
    private ViewPager             pager;
    private DrawableManagerDialog dmDialog;
    private Spinner               spselectlang;
    private JSONObject            jObj                    = null;
    private JSONParser            jParser                 = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_setting);

        if (G.username.equals("")) {
            G.username = G.cmd.namayesh4(1, "info");
        }

        avatar = G.cmd.namayesh4(5, "info");
        if (G.gender.equals("1")) {
            gender = "Female";
        } else {
            gender = "Male";
        }
        il = new ImageLoader1(Setting.this, G.basicAuth);
        imageProfileLq = new ImageLoaderProfileLq(Setting.this, G.basicAuth);
        imageProfileHq = new ImageLoaderProfileHq(Setting.this, G.basicAuth);
        mp = new MediaPlayer();
        dmDialog = new DrawableManagerDialog(Setting.this);
        init();
    }


    private void init() {

        Button btnsave = (Button) findViewById(R.id.btn_save);
        btnsave.setTypeface(G.fontAwesome);
        Button btnsettingback = (Button) findViewById(R.id.btn_setting_back);
        btnsettingback.setTypeface(G.fontAwesome);
        editTextname = (EditText) findViewById(R.id.editText_name);

        imgmanage = (ImageView) findViewById(R.id.img_Setting_image);
        imgSettingimage = (ImageView) findViewById(R.id.img_upimg);

        TextView txtsettingphonenumber = (TextView) findViewById(R.id.txt_setting_phone_number);
        txtsettingphonenumber.setText(G.username);

        TextView txtsettingprivacy = (TextView) findViewById(R.id.txt_setting_privacy);
        TextView txtmessagetextsize = (TextView) findViewById(R.id.txt_setting_message_text_size);
        TextView txtmessagetextcolor = (TextView) findViewById(R.id.txt_setting_message_text_color);
        TextView txtsetdefaultsize = (TextView) findViewById(R.id.txt_set_default_size);
        TextView txtsetdefaultcolor = (TextView) findViewById(R.id.txt_set_default_color);
        TextView txtnotificationring = (TextView) findViewById(R.id.txt_notification_ring);
        TextView txtsettingquestion = (TextView) findViewById(R.id.txt_notification_ring);
        imgmanage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogimageview();

            }
        });
        txtsetdefaultsize.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.updatetextsize(0);

            }
        });
        txtsettingquestion.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, SupportChat.class);
                startActivity(intent);
                finish();

            }
        });
        txtsetdefaultcolor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.updatetextcolor("");
                G.textColor = "";

            }
        });

        imgSettingimage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showalert();

            }
        });

        btnsettingback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        txtnotificationring.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ringdialog();

            }
        });
        setuserimage();

        final SwitchCompat switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);
        final SwitchCompat switchAutoDownload = (SwitchCompat) findViewById(R.id.switch_auto_download);
        final SwitchCompat switchSendByEnter = (SwitchCompat) findViewById(R.id.switch_send_by_enter);
        final SwitchCompat switchHijriDate = (SwitchCompat) findViewById(R.id.switch_hijri_date);
        final SwitchCompat switchCropImage = (SwitchCompat) findViewById(R.id.switch_crop);

        switchCropImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if ( !isChecked) {
                    G.cmd.updateCropSetting(0);
                    G.crop = "0";
                } else {
                    G.cmd.updateCropSetting(1);
                    G.crop = "1";
                }
            }
        });

        switchHijriDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if ( !isChecked) {
                    G.cmd.updateHijriDate(0);
                    updateDateType("0");
                    G.hijriDate = "0";
                } else {
                    G.cmd.updateHijriDate(1);
                    updateDateType("1");
                    G.hijriDate = "1";
                }
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if ( !isChecked) {
                    G.cmd.updatenotification(1);
                    G.notification = "1";
                } else {
                    G.cmd.updatenotification(0);
                    G.notification = "0";
                }
            }
        });
        switchAutoDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    G.cmd.updateautodownload(0);
                    G.autoDownload = "0";
                } else {
                    G.cmd.updateautodownload(1);
                    G.autoDownload = "1";
                }
            }
        });
        switchSendByEnter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( !isChecked) {
                    G.cmd.updatesendbyenter(0);
                    G.sendByEnter = "0";
                } else {
                    G.cmd.updatesendbyenter(1);
                    G.sendByEnter = "1";
                }
            }
        });

        if (G.name.equals("")) {
            G.name = G.cmd.namayesh4(6, "info");
        }

        editTextname.setText(G.name);

        if (G.notification.equals("0")) {
            switchNotification.setChecked(true);
        } else {
            switchNotification.setChecked(false);
        }

        if (G.autoDownload.equals("0")) {
            switchAutoDownload.setChecked(true);
        } else {
            switchAutoDownload.setChecked(false);
        }
        if (G.sendByEnter.equals("0")) {
            switchSendByEnter.setChecked(false);
        } else {
            switchSendByEnter.setChecked(true);
        }

        if (G.hijriDate.equals("0")) { // disable
            switchHijriDate.setChecked(false);
        } else {
            switchHijriDate.setChecked(true);
        }

        if (G.crop.equals("0")) { // disable
            switchCropImage.setChecked(false);
        } else {
            switchCropImage.setChecked(true);
        }

        btnsave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                fullname = editTextname.getText().toString();
                if (fullname != null && !fullname.isEmpty() && !fullname.equals("null") && !fullname.equals("")) {

                    if (fullname.equals(G.name)) {
                        finish();
                    } else {
                        new editprofile().execute();
                    }

                } else {
                    Toast.makeText(Setting.this, getString(R.string.your_full_name_is_empty_en), Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        });

        txtsettingprivacy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, Privacy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        txtmessagetextsize.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textsizedialog();

            }
        });
        txtmessagetextcolor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogSelectColor();
            }
        });

        TextView txt_gener_icon = (TextView) findViewById(R.id.txt_gener_icon);
        txt_gener_icon.setTypeface(G.fontAwesome);

        spselectlang = (Spinner) findViewById(R.id.select_language_sp);
        ArrayList<String> list = new ArrayList<String>();

        if (G.language.equals("0")) { // english
            list.add("English");
            list.add("فارسی");
        } else {
            list.add("فارسی");
            list.add("English");
        }

        final String currentLanguage = G.SelectedLanguage;

        spselectlang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lang = spselectlang.getSelectedItem().toString();

                if (lang.equals("English")) {
                    G.cmd.updatelang(0);
                    G.language = "0";
                    G.SelectedLanguage = "en";

                } else {
                    G.cmd.updatelang(1);
                    G.language = "1";
                    G.SelectedLanguage = "fa";
                }

                if (currentLanguage.equals(G.SelectedLanguage)) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                } else {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spselectlang.setAdapter(new SpinnerLanguage(list));

    }


    //=======================B
    private void updateDateType(String hijri) { // send to pageMessagings
        Intent intent = new Intent("updateDateType");
        intent.putExtra("hijri", hijri);
        LocalBroadcastManager.getInstance(Setting.this).sendBroadcast(intent);
    }


    private void showDialogSelectColor() {

        ColorPiker d = new ColorPiker(Setting.this, Color.BLUE, new OnColorChangedListenerSelect() {

            @Override
            public void colorChanged(String key, int color) {

                if (key.equals("ok")) {
                    String strColor = String.format("#%06X", 0xFFFFFF & color);
                    G.cmd.updatetextcolor(strColor);
                    G.textColor = strColor;
                }
            }


            @Override
            public void Confirmation(Boolean result) {}
        });

        Display display = getWindowManager().getDefaultDisplay();
        @SuppressWarnings("deprecation") int screenWidth = display.getWidth();

        d.getWindow().setLayout(screenWidth, LayoutParams.WRAP_CONTENT);
        d.setCancelable(true);
        d.show();
    }


    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (popUp != null && popUp.isShowing()) {
            popUp.dismiss();
        }
        super.onDestroy();
    }


    private void setuserimage() {
        if (avatar != null && !avatar.isEmpty() && !avatar.equals("null") && !avatar.equals("") && !avatar.equals("empty")) {
            il.DisplayImage(avatar, R.drawable.difaultimage, imgmanage);
        } else {
            HelperDrawAlphabet pf = new HelperDrawAlphabet();
            Bitmap bm = pf.drawAlphabet(Setting.this, G.name, imgmanage);
            imgmanage.setImageBitmap(bm);
        }
    }


    private class SpinnerLanguage extends BaseAdapter {

        private ArrayList<String> list;
        LayoutInflater            mInflater;


        public SpinnerLanguage(ArrayList<String> list) {
            this.list = list;
            mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            view = mInflater.inflate(R.layout.sp_item, null);
            TextView txt = (TextView) view.findViewById(R.id.sp_text);
            txt.setText(list.get(position));
            return view;
        }
    }


    public void showalert() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_chooseavatar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                //new deleteavatar().execute();

            }
        });
        btn_delete_avatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                //new deleteavatar().execute();

            }
        });

        btn_choose_gallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture_en)), G.request_code_FILE);

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
                startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture_en)), G.request_code_FILE);

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
                startActivityForResult(intent, G.CAMERA_PIC_REQUEST);

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
                startActivityForResult(intent, G.CAMERA_PIC_REQUEST);

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


    public void ringdialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_ring);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioButton radio_aAooow = (RadioButton) dialog.findViewById(R.id.radio_aAooow);
        RadioButton radio_BBAlert = (RadioButton) dialog.findViewById(R.id.radio_BBAlert);
        RadioButton radio_Boom = (RadioButton) dialog.findViewById(R.id.radio_Boom);
        RadioButton radio_Bounce = (RadioButton) dialog.findViewById(R.id.radio_Bounce);
        RadioButton radio_DooDoo = (RadioButton) dialog.findViewById(R.id.radio_DooDoo);
        RadioButton radio_igap = (RadioButton) dialog.findViewById(R.id.radio_igap);
        RadioButton radio_Jing = (RadioButton) dialog.findViewById(R.id.radio_Jing);
        RadioButton radio_LiLi = (RadioButton) dialog.findViewById(R.id.radio_LiLi);
        RadioButton radio_Msg = (RadioButton) dialog.findViewById(R.id.radio_Msg);
        RadioButton radio_New = (RadioButton) dialog.findViewById(R.id.radio_New);
        RadioButton radio_onelime = (RadioButton) dialog.findViewById(R.id.radio_onelime);
        RadioButton radio_Tone = (RadioButton) dialog.findViewById(R.id.radio_Tone);
        RadioButton radio_woow = (RadioButton) dialog.findViewById(R.id.radio_woow);
        RadioButton radio_none = (RadioButton) dialog.findViewById(R.id.radio_none);

        Button btnsave = (Button) dialog.findViewById(R.id.btn_save);
        Button btncancel = (Button) dialog.findViewById(R.id.btn_cancel);

        radio_aAooow.setTypeface(G.robotoLight);
        radio_BBAlert.setTypeface(G.robotoLight);
        radio_Boom.setTypeface(G.robotoLight);
        radio_Bounce.setTypeface(G.robotoLight);
        radio_DooDoo.setTypeface(G.robotoLight);
        radio_igap.setTypeface(G.robotoLight);
        radio_Jing.setTypeface(G.robotoLight);
        radio_LiLi.setTypeface(G.robotoLight);
        radio_Msg.setTypeface(G.robotoLight);
        radio_New.setTypeface(G.robotoLight);
        radio_onelime.setTypeface(G.robotoLight);
        radio_Tone.setTypeface(G.robotoLight);
        radio_woow.setTypeface(G.robotoLight);
        radio_none.setTypeface(G.robotoLight);

        btnsave.setTypeface(G.fontAwesome);
        btncancel.setTypeface(G.fontAwesome);

        radio_aAooow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.aooow);
                mp.start();
                ring = 1;

            }
        });
        radio_BBAlert.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.bbalert);
                mp.start();
                ring = 2;
            }
        });
        radio_Boom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.boom);
                mp.start();
                ring = 3;
            }
        });
        radio_Bounce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.bounce);
                mp.start();
                ring = 4;
            }
        });
        radio_DooDoo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.doodoo);
                mp.start();
                ring = 5;
            }
        });
        radio_igap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.igap);
                mp.start();
                ring = 0;
            }
        });
        radio_Jing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.jing);
                mp.start();
                ring = 7;
            }
        });
        radio_LiLi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.lili);
                mp.start();
                ring = 8;
            }
        });
        radio_Msg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.msg);
                mp.start();
                ring = 9;
            }
        });
        radio_New.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.newa);
                mp.start();
                ring = 10;
            }
        });
        radio_onelime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.onelime);
                mp.start();
                ring = 11;
            }
        });
        radio_Tone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.tone);
                mp.start();
                ring = 12;
            }
        });
        radio_woow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.reset();
                }
                mp = MediaPlayer.create(Setting.this, R.raw.woow);
                mp.start();
                ring = 13;
            }
        });
        radio_none.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ring = 6;
            }
        });

        btnsave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.cmd.updateringtone(ring);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btncancel.setOnClickListener(new OnClickListener() {

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


    private boolean getFilePath(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] proj = null;
        Cursor cursor = getContentResolver().query(selectedImageUri, proj, null, null, null);

        String filename = "";
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int columnIndexname = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            filename = cursor.getString(columnIndexname);
            imageFileNameWithoutCrop = filename;
            selectedImagePath = cursor.getString(column_index);
        }
        filePathWithoutCrop = selectedImagePath;
        return true;
    }


    private void beginCrop(Uri source, Uri destination) {
        Crop.of(source, destination).asSquare().start(this);
    }


    private void onCaptureImageResultCrop() {
        imgmanage.setImageBitmap(CorrectImageRotate.correct(filePath));
        alertdialogupfile();
    }


    private void onCaptureImageResult() {
        filePath = fileUri.getPath();
        imgmanage.setImageBitmap(CorrectImageRotate.correct(filePath));
        alertdialogupfile();
    }


    private void onSelectFromGalleryResultCrop() {
        imgmanage.setImageBitmap(CorrectImageRotate.correct(filePath));
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
                    onSelectFromGalleryResultCrop();
                }
                catch (Exception e) {
                    Toast.makeText(Setting.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (showDefaultImageCapture) {
                showDefaultImageCapture = false;

                try {
                    onCaptureImageResult();
                }
                catch (Exception e) {
                    Toast.makeText(Setting.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Setting.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (requestCode == G.request_code_FILE) {
                showDefaultImage = true;

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            needCrop = getFilePath(data);
                            showDefaultImage = true;
                        }
                        catch (Exception e) {
                            Toast.makeText(Setting.this, getString(R.string.cannot_load_data_en), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                imageFileName = "" + System.currentTimeMillis();
                filePath = G.DIR_CROP + "/" + imageFileName + ".jpg";
                Uri destination = Uri.fromFile(new File(filePath));
                beginCrop(data.getData(), destination);

            } else if (requestCode == G.CAMERA_PIC_REQUEST) {

                imageFileName = "" + System.currentTimeMillis();
                filePath = G.DIR_CROP + "/" + imageFileName + ".jpg";
                Uri destinationCapture = Uri.fromFile(new File(filePath));
                beginCrop(fileUri, destinationCapture);
                showDefaultImageCapture = true;
            }
        }
    }


    private void onCaptureImageResult(Intent data) {

        filePath = fileUri.getPath();
        File imgFile = new File(filePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imgmanage.setImageBitmap(myBitmap);
        alertdialogupfile();
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();

        selectedImagePath = cursor.getString(column_index);
        filePath = selectedImagePath;

        File imgFile = new File(filePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imgmanage.setImageBitmap(myBitmap);
        alertdialogupfile();
    }


    private void alertdialogupfile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Setting.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upfile, null);
        dialogBuilder.setView(dialogView);
        new UploadFileToServer().execute();
        pDialog = new Dialog(Setting.this);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
            httppost.setHeader("Authorization", G.basicAuth);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

                    @Override
                    public void transferred(long num) {

                    }
                });

                File sourceFile = new File(filePath);
                entity.addPart("avatar", new FileBody(sourceFile));

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
                            lq = result.getString(G.TAG_LQ);
                            hq = result.getString(G.TAG_HQ);
                            G.cmd.updateuseravatar(lq, hq);
                        } else {
                            responseString = "Error occurred! Http Status Code: " + statusCode;
                        }

                    }
                    catch (JSONException e) {}
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Setting.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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
            il.DisplayImage(hq, R.drawable.difaultimage, imgmanage);
            super.onPostExecute(result);
        }

    }


    private void textsizedialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_brush_paint);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView textViewbrushsize = (TextView) dialog.findViewById(R.id.textView_brush_size);
        final SeekBar seekBarbrushsize = (SeekBar) dialog.findViewById(R.id.seekBar_brush_size);
        seekBarbrushsize.setMax(72);

        seekBarbrushsize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewbrushsize.setText(String.valueOf(progress));
            }
        });

        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                int textsize = seekBarbrushsize.getProgress();
                G.cmd.updatetextsize(textsize);
                G.textSize = textsize + "";
            }
        });

        LayoutParams lp = new LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


    class editprofile extends AsyncTask<String, String, String> {

        private Boolean success1 = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(Setting.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                params.add(new BasicNameValuePair("fullname", fullname));
                params.add(new BasicNameValuePair("gender", gender));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.registerprofile, params, "PUT", G.basicAuth, null);
                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    success1 = json.getBoolean(G.TAG_SUCCESS);

                    if ( !success1) {
                        if (statuscode.equals("400")) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Setting.this, getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Setting.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Setting.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Setting.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
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
            if (success1 == true) {
                G.cmd.updateprofile(fullname);
                finish();
            }
        }

    }


    private void dialogimageview() {

        dialog = new Dialog(Setting.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_imageview_avatar, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);

        //==========
        img.add(avatar);
        pager = (ViewPager) dialogView.findViewById(R.id.pager);
        //==========

        Button btnBack = (Button) dialogView.findViewById(R.id.btn_back);
        btnBack.setTypeface(G.fontAwesome);

        Button btnSetting = (Button) dialogView.findViewById(R.id.btn_setting);
        btnSetting.setTypeface(G.fontAwesome);

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

        txtUserName.setText(G.name);
        txtOf.setText("1 " + getString(R.string.of_en) + " 1");
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btnSetting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int po = pager.getCurrentItem();
                popupoptions(v, po);

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new GetUserImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new GetUserImages().execute();
        }

        LayoutParams lp = new LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
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
                JSONObject jsonobj = jParser.getJSONFromUrl(G.url + "users/" + G.username + "/avatars", params, "GET", G.basicAuth, null);

                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);

                    if (success) {

                        final JSONArray result = json.getJSONArray("result");

                        imgCount = result.length();
                        imageUrlsLq.clear();
                        imageUrlsHq.clear();
                        imageID.clear();
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject response = result.getJSONObject(i);
                            String lq = response.getString("lq");
                            String hq = response.getString("hq");
                            String id = response.getString("id");
                            imageUrlsLq.add(lq);
                            imageUrlsHq.add(hq);
                            imageID.add(id);
                        }

                        if (G.cmd.namayesh4(4, "info") == null) {

                            try {
                                G.cmd.updateuseravatar(imageUrlsLq.get(0), imageUrlsHq.get(0));
                            }
                            catch (Exception e) {}
                        }

                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(Setting.this, getString(R.string.success_false_en), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Setting.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Setting.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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
                    adapter = new ImagePagerAdapter(G.username, dmDialog, imageID, imageUrlsLq, imageUrlsHq, imageProfileLq, imageProfileHq, G.basicAuth, Setting.this);
                    pager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            });

            if (imgCount == 0) {
                if (avatar == null || avatar.isEmpty() || avatar.equals("null") || avatar.equals("")) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Setting.this, getString(R.string.avatar_not_set_yet_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                G.cmd.updateChatroomAvatar1(imageUrlsLq.get((imageUrlsLq.size() - 1)), G.username);
                G.cmd.updateChatroomAvatar2(imageUrlsLq.get((imageUrlsLq.size() - 1)), imageUrlsHq.get((imageUrlsHq.size() - 1)), G.username);
            }

        }
    }


    @SuppressWarnings("deprecation")
    private void popupoptions(View v, final int position) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.popup_image_dialog, null);
        popUp = new PopupWindow(Setting.this);
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
        btndeleteavatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pos = position;

                if (popUp != null && popUp.isShowing()) {
                    popUp.dismiss();
                }

                new deleteavatar().execute();

            }
        });

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

                }

            }
        });

    }


    class deleteavatar extends AsyncTask<String, String, String> {

        private Boolean success1 = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(Setting.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                JSONObject jsonobj = jParser.getJSONFromUrl(G.deleteavatar + imageID.get(pos), params, "DELETE", G.basicAuth, null);
                try {
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    success1 = json.getBoolean(G.TAG_SUCCESS);

                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Setting.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Setting.this, getString(R.string.internet_connection_problem_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {

            if (success1 == true) {
                //bayad check she age avalin item bod bayad az db ham update she ye avatar dge beshine jash age na ke hichi faghat az list hazf she
                if (pos == 0) {
                    imageUrlsLq.remove(pos);
                    imageUrlsHq.remove(pos);
                    imageID.remove(pos);
                    //adapter = new ImagePagerAdapter(img, img, il, il1, Setting.this);
                    //pager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pager.setAdapter(adapter);
                    pager.setCurrentItem(0);
                    imgCount = imgCount - 1;
                    txtOf.setText("1 " + getString(R.string.of_en) + " " + imgCount);

                    if (imageUrlsLq.size() == 0) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        avatar = null;
                        G.cmd.updateuseravatar(null, null);
                    } else {
                        avatar = imageUrlsLq.get(0);
                        G.cmd.updateuseravatar(imageUrlsLq.get(0), imageUrlsHq.get(0));
                    }
                    setuserimage();
                } else {
                    imageUrlsLq.remove(pos);
                    imageUrlsHq.remove(pos);
                    imageID.remove(pos);
                    adapter.notifyDataSetChanged();
                    pager.setAdapter(adapter);
                    pager.setCurrentItem(0);
                    imgCount = imgCount - 1;
                    txtOf.setText("1 " + getString(R.string.of_en) + " " + imgCount);
                    avatar = imageUrlsLq.get(0);
                    setuserimage();
                    if (imageUrlsLq.size() == 0) {

                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            }

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

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
        finishActivity(100);
        super.onPause();
        G.appIsShowing = false;
    }
}
