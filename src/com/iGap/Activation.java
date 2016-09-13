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
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.G;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;
import com.iGap.interfaces.OnComplet;
import com.iGap.receivers.IncomingSms;


/**
 * in class code sms taide sabtenam ra migirad
 *
 */

public class Activation extends Activity {

    private EditText            verification_code_edittext;
    private String              code, username, token, basicAuth;
    private Dialog              pDialog;
    private IncomingSms         smsReciver;
    private static final String TAG_TOKEN = "token";
    private JSONParser          jParser   = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_activation);

        TextView txtgetcodeprob = (TextView) findViewById(R.id.txt_getcodeprob);
        txtgetcodeprob.setTypeface(G.robotoLight);
        TextView linkpagetx = (TextView) findViewById(R.id.link_page_tx);
        linkpagetx.setTypeface(G.robotoLight);
        TextView txtcirlce1 = (TextView) findViewById(R.id.txt_cirlce1);
        txtcirlce1.setTypeface(G.fontAwesome);
        TextView txtcirlce2 = (TextView) findViewById(R.id.txt_cirlce2);
        txtcirlce2.setTypeface(G.fontAwesome);
        TextView txtcirlce3 = (TextView) findViewById(R.id.txt_cirlce3);
        txtcirlce3.setTypeface(G.fontAwesome);
        TextView txtSelectLangg = (TextView) findViewById(R.id.txt_select_langg);
        txtSelectLangg.setTypeface(G.robotoBold);

        TextView txt_pl_select = (TextView) findViewById(R.id.txt_pl_select);
        txt_pl_select.setTypeface(G.robotoLight);

        TextView txtCode = (TextView) findViewById(R.id.txt_code);
        txtCode.setTypeface(G.robotoBold);

        Button verification_continueButton = (Button) findViewById(R.id.code_continue_btn);
        verification_continueButton.setTypeface(G.robotoBold);

        Button verification_notgetButton = (Button) findViewById(R.id.not_code_btn);
        verification_notgetButton.setTypeface(G.robotoBold);

        linkpagetx.setPaintFlags(linkpagetx.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        linkpagetx.setPaintFlags(linkpagetx.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        linkpagetx.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_support))));
            }
        });

        verification_code_edittext = (EditText) findViewById(R.id.phone_num_etx);
        verification_code_edittext.setTypeface(G.robotoLight);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getString("code");
            username = extras.getString("username");

            verification_code_edittext.setText(code);
        }

        verification_notgetButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activation.this, Register.class));
                finish();

            }
        });

        verification_continueButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                code = verification_code_edittext.getText().toString();

                if (code != null && !code.isEmpty() && !code.equals("null") && !code.equals("")) {
                    new LoadAllgoroh().execute();

                } else {
                    Toast.makeText(G.context, getString(R.string.please_enter_code_en), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /**
     * 
     * ferestadan code be server va gerftan token braye aghaz chat
     *
     */

    class LoadAllgoroh extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(Activation.this);
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
                params.add(new BasicNameValuePair("verifyCode", code));
                params.add(new BasicNameValuePair("username", username));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.verification, params, "POST", basicAuth, null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);

                    boolean success = json.getBoolean(G.TAG_SUCCESS);
                    JSONObject result = json.getJSONObject(G.TAG_RESULT);

                    if (success == true) {
                        token = result.getString(TAG_TOKEN);
                        Intent intent = new Intent(G.context, RegisterProfile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("username", username);
                        intent.putExtra("token", token);
                        startActivity(intent);
                        finish();

                    } else {

                        if (statuscode.equals("403")) {
                            String errorStatus = result.getString("errorStatus");

                            if (errorStatus.equals("1")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Activation.this, getString(R.string.code_isnot_true_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Activation.this, getString(R.string.cannot_test_code_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else if (statuscode.equals("400")) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Activation.this, getString(R.string.Error_invalid_parameters_en), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (statuscode.equals("500")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Activation.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Activation.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {

        Utils.checkLanguage(this);
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        smsReciver = new IncomingSms("10004427", new OnComplet() {

            @Override
            public void complet(Boolean result, String message) {

                verification_code_edittext.setText(message);
                code = message;

                try {
                    if (message != null && !message.isEmpty() && !message.equals("null") && !message.equals("")) {
                        new LoadAllgoroh().execute();
                    }
                }
                catch (Exception e1) {}

                try {
                    unregisterReceiver(smsReciver);
                }
                catch (Exception e) {}
            }
        });

        registerReceiver(smsReciver, filter);

        super.onResume();
        G.appIsShowing = true;
    }


    @Override
    protected void onPause() {

        try {
            unregisterReceiver(smsReciver);
        }
        catch (Exception e) {}

        super.onPause();
        G.appIsShowing = false;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }
}
