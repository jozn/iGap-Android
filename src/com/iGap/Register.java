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
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iGap.adapter.CustomListAdapterExplorer;
import com.iGap.adapter.G;
import com.iGap.instruments.JSONParser;
import com.iGap.instruments.Utils;


/**
 * 
 * baraye sabtename karbar ba vorude etelaate shomere telefone va keshvar
 *
 */
public class Register extends Activity {

    private String              countryCode    = null;
    private String              mobile;
    private String              countryIsoCode = null;
    private String              countryName    = null;

    private EditText            edtMobile, edtSelectCountry;
    private boolean             mState         = true;

    private Dialog              pDialog, dialogTerms, di;

    private static final String TAG_USERNAME   = "username";
    private JSONParser          jParser        = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            try {
                countryCode = extras.getString("countryCode");
                countryIsoCode = extras.getString("countryIsoCode");
                countryName = extras.getString("countryName");
            }
            catch (Exception e) {}
        }

        Button btncontinue = (Button) findViewById(R.id.code_continue_btn);
        edtMobile = (EditText) findViewById(R.id.phone_num_etx);

        TextView gener_icon_tx = (TextView) findViewById(R.id.txt_gener_icon_register);
        gener_icon_tx.setTypeface(G.fontAwesome);

        TextView txtcirlce1 = (TextView) findViewById(R.id.txt_cirlce1);
        TextView txtcirlce2 = (TextView) findViewById(R.id.txt_cirlce2);
        TextView txtcirlce3 = (TextView) findViewById(R.id.txt_cirlce3);
        txtcirlce1.setTypeface(G.fontAwesome);
        txtcirlce2.setTypeface(G.fontAwesome);
        txtcirlce3.setTypeface(G.fontAwesome);

        edtSelectCountry = (EditText) findViewById(R.id.edt_selectcountry);
        edtSelectCountry.setTypeface(G.robotoBold);

        TextView txt_select_langg = (TextView) findViewById(R.id.txt_select_langg);
        txt_select_langg.setTypeface(G.robotoBold);
        TextView txt_select_country = (TextView) findViewById(R.id.txt_select_country);
        txt_select_country.setTypeface(G.robotoLight);

        TextView txt_your_country = (TextView) findViewById(R.id.txt_your_country);
        txt_your_country.setTypeface(G.robotoBold);

        dialogterms();

        TextView txt_phone_num = (TextView) findViewById(R.id.txt_phone_num);
        txt_phone_num.setTypeface(G.robotoBold);

        TextView txtterms = (TextView) findViewById(R.id.txt_terms);
        txtterms.setTypeface(G.robotoLight);
        txtterms.setPaintFlags(txtterms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtterms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialogTerms.show();
            }
        });

        TextView txt_rec = (TextView) findViewById(R.id.txt_rec);
        txt_rec.setTypeface(G.robotoLight);
        TextView txt_rec2 = (TextView) findViewById(R.id.txt_rec2);
        txt_rec2.setTypeface(G.robotoLight);

        if (countryCode != null && !countryCode.isEmpty() && !countryCode.equals("null") && !countryCode.equals("")) {
            edtSelectCountry.setText("(" + countryIsoCode + ") " + countryName);
        }

        edtSelectCountry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogCountrySelect();

            }
        });

        btncontinue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mobile = edtMobile.getText().toString();

                if (countryIsoCode != null && !countryIsoCode.isEmpty() && !countryIsoCode.equals("null") && !countryIsoCode.equals("")) {
                    if (mobile != null && !mobile.isEmpty() && !mobile.equals("null") && !mobile.equals("")) {

                        if (mobile.length() == 10) {

                            if (mobile.substring(0, 1).equals("9"))
                                dialogtaeed();
                            else {
                                Toast.makeText(G.context, getString(R.string.your_phone_number_is_not_correct_en), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(G.context, getString(R.string.phone_number_must_be_ten_en), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(G.context, getString(R.string.please_enter_phone_number_en), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(G.context, getString(R.string.please_enter_country_code_en), Toast.LENGTH_SHORT).show();
                }

            }
        });

        gener_icon_tx.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogCountrySelect();

            }
        });
    }


    private void dialogterms() {
        dialogTerms = new Dialog(this);
        dialogTerms.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogTerms.setContentView(R.layout.dialog_termsdialog);

        dialogTerms.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final Button btnPrivacy = (Button) dialogTerms.findViewById(R.id.btn_harim);
        btnPrivacy.setTypeface(G.robotoBold);
        final Button btnTerms = (Button) dialogTerms.findViewById(R.id.btn_khadamat);
        btnTerms.setTypeface(G.robotoBold);

        final WebView webView = (WebView) dialogTerms.findViewById(R.id.dtd_webview);
        webView.loadUrl(getString(R.string.link_term));

        btnTerms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                btnTerms.setBackgroundResource(R.drawable.roundedtrans);
                btnPrivacy.setBackgroundResource(R.drawable.roundedtrans2);

                webView.loadUrl(getString(R.string.link_term));

            }
        });

        btnPrivacy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btnTerms.setBackgroundResource(R.drawable.roundedtrans2);
                btnPrivacy.setBackgroundResource(R.drawable.roundedtrans);

                webView.loadUrl(getString(R.string.link_privacy));
            }
        });

        Button btnAgree = (Button) dialogTerms.findViewById(R.id.btn_agree);
        btnAgree.setTypeface(G.robotoBold);
        btnAgree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dialogTerms != null && dialogTerms.isShowing()) {
                    dialogTerms.dismiss();
                }

            }
        });
        TextView txtTermAgree = (TextView) dialogTerms.findViewById(R.id.txt_term_agree);
        txtTermAgree.setTypeface(G.robotoLight);

        LayoutParams lp = new LayoutParams();
        lp.copyFrom(dialogTerms.getWindow().getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        dialogTerms.getWindow().setAttributes(lp);

    }


    private void dialogtaeed() {
        di = new Dialog(this);
        di.requestWindowFeature(Window.FEATURE_NO_TITLE);
        di.getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        di.setContentView(R.layout.dialog_phoneverification);

        di.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtVerify = (TextView) di.findViewById(R.id.txt_verify_verivfy_no);
        txtVerify.setTypeface(G.robotoBold);

        TextView txtPhoneNo = (TextView) di.findViewById(R.id.txt_phone_no_verivfy_no);
        txtPhoneNo.setTypeface(G.robotoLight);
        txtPhoneNo.setText(mobile);
        TextView txtApproveText = (TextView) di.findViewById(R.id.txt_approve_text_verivfy_no);
        txtApproveText.setTypeface(G.robotoLight);

        Button btnApproveNo = (Button) di.findViewById(R.id.btn_approve_verivfy_no);
        btnApproveNo.setTypeface(G.robotoBold);
        btnApproveNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (di != null && di.isShowing()) {
                    di.dismiss();
                }
                new LoadAllgoroh().execute();

            }
        });
        Button btnCancle = (Button) di.findViewById(R.id.btn_cancle_verivfy_no);
        btnCancle.setTypeface(G.robotoBold);
        btnCancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (di != null && di.isShowing()) {
                    di.dismiss();
                }

            }
        });

        LayoutParams lp = new LayoutParams();
        lp.copyFrom(di.getWindow().getAttributes());
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        di.getWindow().setAttributes(lp);
        di.show();
    }


    class LoadAllgoroh extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new Dialog(Register.this);
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
                params.add(new BasicNameValuePair("mobile", mobile));
                params.add(new BasicNameValuePair("countryIsoCode", countryIsoCode));
                JSONObject jsonobj = jParser.getJSONFromUrl(G.register, params, "POST", "0", null);

                try {
                    String statuscode = jsonobj.getString("statuscode");
                    String jsonstring = jsonobj.getString("json");
                    JSONObject json = new JSONObject(jsonstring);
                    Boolean success = json.getBoolean(G.TAG_SUCCESS);
                    JSONObject result = json.getJSONObject("result");
                    if (success) {

                        G.cmd.addCountry(countryName);

                        String username = result.getString(TAG_USERNAME);
                        Intent intent = new Intent(G.context, Activation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("username", username);
                        intent.putExtra("country", countryName);
                        startActivity(intent);
                        finish();

                    } else {

                        if (statuscode.equals("403")) {
                            String errorStatus = result.getString("errorStatus");
                            if (errorStatus.equals("1")) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Register.this, getString(R.string.cannot_test_code_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Register.this, getString(R.string.cannot_test_code_en), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else if (statuscode.equals("400")) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, getString(R.string.illegal_characters), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (statuscode.equals("500")) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, getString(R.string.Error_occurred_en), Toast.LENGTH_SHORT).show();
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


    private void showDialogCountrySelect() {

        final Dialog dialogSelectCountry = new Dialog(Register.this);
        mState = true;
        dialogSelectCountry.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelectCountry.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogSelectCountry.setContentView(R.layout.dialog_select_country);
        dialogSelectCountry.setCancelable(true);

        final ListView lvDialogCountryList = (ListView) dialogSelectCountry.findViewById(R.id.ListView_dialog_country_list);
        final EditText edtDialogSearchCountry = (EditText) dialogSelectCountry.findViewById(R.id.editText_dialog_search_country);
        final ImageView ivDialogCountryPic = (ImageView) dialogSelectCountry.findViewById(R.id.ImageView_dialog_country_pic);

        setDialogListAdapter(lvDialogCountryList, "");

        setDialogSize(dialogSelectCountry);

        edtDialogSearchCountry.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (mState) {
                    Animation anim = AnimationUtils.loadAnimation(Register.this, R.anim.tw);
                    ivDialogCountryPic.startAnimation(anim);

                    android.widget.FrameLayout.LayoutParams p = new android.widget.FrameLayout.LayoutParams((int) android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, (int) android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
                    p.gravity = Gravity.LEFT;
                    ivDialogCountryPic.setLayoutParams(p);
                    edtDialogSearchCountry.setText("   ");
                }
            }
        });

        edtDialogSearchCountry.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setDialogListAdapter(lvDialogCountryList, edtDialogSearchCountry.getText().toString().trim());
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


            @Override
            public void afterTextChanged(Editable s) {
                if (edtDialogSearchCountry.getText().length() < 3) {
                    edtDialogSearchCountry.setText("   ");
                    edtDialogSearchCountry.setSelection(2);
                }
            }
        });

        lvDialogCountryList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                try {

                    String id = lvDialogCountryList.getItemAtPosition(arg2).toString().split(";;")[0];

                    String isocode = G.cmd.getCountryInfo1(1, "Countries", id);
                    String isoname = G.cmd.getCountryInfo1(2, "Countries", id);
                    String country = G.cmd.getCountryInfo1(3, "Countries", id);

                    countryName = country;

                    countryIsoCode = isoname;
                    edtSelectCountry.setText("(" + isocode + ") " + lvDialogCountryList.getItemAtPosition(arg2).toString().split(";;")[1]);

                }
                catch (Exception e) {}

                dialogSelectCountry.cancel();
            }
        });

    }


    private void setDialogSize(final Dialog dialogSelectCountry) {
        Display display = getWindowManager().getDefaultDisplay();
        @SuppressWarnings("deprecation") int screenWidth = display.getWidth();
        @SuppressWarnings("deprecation") int screenHeight = display.getHeight();
        dialogSelectCountry.getWindow().setLayout((4 * screenWidth / 5), (4 * screenHeight / 5));

        dialogSelectCountry.show();
    }


    private void setDialogListAdapter(final ListView lvDialogCountryList, String SelectCountry) {
        ArrayList<String> list;
        try {
            list = G.cmd.namayeshCountries(SelectCountry);
            if (list != null) {
                lvDialogCountryList.setAdapter(new CustomListAdapterExplorer(Register.this, list));
            }
            else
                lvDialogCountryList.setAdapter(null);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onDestroy() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (dialogTerms != null && dialogTerms.isShowing()) {
            dialogTerms.dismiss();
        }
        if (di != null && di.isShowing()) {
            di.dismiss();
        }
        super.onDestroy();
    }
}
