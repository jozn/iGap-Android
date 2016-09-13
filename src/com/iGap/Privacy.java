// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.instruments.ConfirmationDialog;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnColorChangedListenerSelect;
import com.iGap.services.PublicService;


/**
 * 
 * namayesh va tanzimat layeh privacy
 *
 */
public class Privacy extends Activity {

    private Dialog     dialog, pDialog;
    private JSONParser jParser = new JSONParser();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_privacy);
        init();
    }


    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        super.onDestroy();
    }


    private void init() {

        Button btnBack = (Button) findViewById(R.id.btn_privacy_back);
        btnBack.setTypeface(G.fontAwesome);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        TextView txtStrPrivacySecurity = (TextView) findViewById(R.id.txt_str_privacy_security);
        txtStrPrivacySecurity.setTypeface(G.robotoBold);

        TextView txtStrPrivacy = (TextView) findViewById(R.id.txt_str_privacy);
        txtStrPrivacy.setTypeface(G.robotoBold);

        TextView txtStrBlockedUser = (TextView) findViewById(R.id.txt_privacy_blocked_user);
        txtStrBlockedUser.setTypeface(G.robotoLight);

        TextView txtStrLastSeen = (TextView) findViewById(R.id.txt_privacy_last_seen);
        txtStrLastSeen.setTypeface(G.robotoLight);

        TextView txtLastSeen = (TextView) findViewById(R.id.txt_privacy_last_seen_selection);
        txtLastSeen.setTypeface(G.robotoLight);

        TextView txtWhoCanSee = (TextView) findViewById(R.id.txt_privacy_who_can_see);
        txtWhoCanSee.setTypeface(G.robotoLight);

        TextView txtStrSecurity = (TextView) findViewById(R.id.txt_str_security);
        txtStrSecurity.setTypeface(G.robotoBold);

        TextView txtPasscode = (TextView) findViewById(R.id.txt_privacy_passcode);
        txtPasscode.setTypeface(G.robotoLight);

        TextView txtTowStep = (TextView) findViewById(R.id.txt_privacy_tow_step_verification);
        txtTowStep.setTypeface(G.robotoLight);

        TextView txtActiveSesion = (TextView) findViewById(R.id.txt_privacy_active_sessions);
        txtActiveSesion.setTypeface(G.robotoLight);

        TextView txtControlSesion = (TextView) findViewById(R.id.txt_privacy_control_session);
        txtControlSesion.setTypeface(G.robotoLight);

        TextView txtStrAccountSelf = (TextView) findViewById(R.id.txt_str_account_self);
        txtStrAccountSelf.setTypeface(G.robotoBold);

        TextView txtStrAwayfor = (TextView) findViewById(R.id.txt_str_away);
        txtStrAwayfor.setTypeface(G.robotoLight);

        final TextView txtAway = (TextView) findViewById(R.id.txt_away_time);
        txtAway.setTypeface(G.robotoLight);

        String month = G.cmd.getsetting(11);
        txtAway.setText(month + " month");
        txtAway.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selfdeletetime(txtAway);

            }
        });

        TextView txtNote = (TextView) findViewById(R.id.txt_str_note);
        txtNote.setTypeface(G.robotoLight);

        TextView txtDeleteAccountUser = (TextView) findViewById(R.id.txt_delete_account);
        txtDeleteAccountUser.setTypeface(G.robotoLight);

        txtDeleteAccountUser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ConfirmationDialog cm = new ConfirmationDialog(Privacy.this, new OnColorChangedListenerSelect() {

                    @Override
                    public void colorChanged(String key, int color) {

                    }


                    @Override
                    public void Confirmation(Boolean result) {
                        if (result) {

                            new DeleteAccount().execute();
                        }
                    }
                });
                cm.showdialog(getString(R.string.do_you_want_delete_your_account));

            }
        });

        txtStrBlockedUser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Privacy.this, BlockList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
    }


    private void selfdeletetime(final TextView txtAway) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.dialog_brush_paint);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView textViewbrushsize = (TextView) dialog.findViewById(R.id.textView_brush_size);
        final SeekBar seekBarbrushsize = (SeekBar) dialog.findViewById(R.id.seekBar_brush_size);
        seekBarbrushsize.setMax(12);

        seekBarbrushsize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewbrushsize.setText(String.valueOf(progress));
            }
        });

        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                int textsize = seekBarbrushsize.getProgress();
                txtAway.setText(textsize + " months");
                new SelfDelete().execute("" + textsize);
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


    private class DeleteAccount extends AsyncTask<String, String, String> {

        private boolean success;
        private boolean result;


        @Override
        protected void onPreExecute() {
            pDialog = new Dialog(Privacy.this);
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
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject jsonobj = jParser.getJSONFromUrl(G.url + "/users", params, "DELETE", G.basicAuth, null);
                try {
                    jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    success = json.getBoolean(G.TAG_SUCCESS);
                    result = json.getBoolean(G.TAG_RESULT);
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Privacy.this, getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Privacy.this, getString(R.string.message_error2_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String arg) {

            if (success && result) {

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                PublicService.stopService = true;

                G.cmd.deleteAllInfo("info");

                Intent intent = new Intent(Privacy.this, Splash.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isclose", "close");
                startActivity(intent);
                finish();

            }
        }
    }


    private class SelfDelete extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new Dialog(Privacy.this);
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
        protected String doInBackground(String... args) {

            String selfRemove = args[0];
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("selfRemove", selfRemove));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.selfdelete, params, "PUT", G.basicAuth, null);
                try {
                    jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    boolean success = json.getBoolean(G.TAG_SUCCESS);
                    boolean result = json.getBoolean(G.TAG_RESULT);

                    if (success && result) {
                        G.cmd.updateselfremove(selfRemove);
                    }
                }
                catch (JSONException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Privacy.this, getString(R.string.message_error1_en), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(Privacy.this, getString(R.string.message_error2_en), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String arg) {

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
    protected void onPause() {
        super.onPause();
        G.appIsShowing = false;
    }

}
